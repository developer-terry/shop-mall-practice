package com.practice.shopmall.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.practice.common.to.mq.SeckillOrderTo;
import com.practice.common.utils.R;
import com.practice.common.vo.MemberResponseVo;
import com.practice.shopmall.seckill.feign.CouponFeignService;
import com.practice.shopmall.seckill.feign.ProductFeignService;
import com.practice.shopmall.seckill.interceptor.LoginUserInterceptor;
import com.practice.shopmall.seckill.service.SeckillService;
import com.practice.shopmall.seckill.to.SeckillSkuRedisTo;
import com.practice.shopmall.seckill.vo.SeckillSessionsWithSkus;
import com.practice.shopmall.seckill.vo.SeckillSkuVo;
import com.practice.shopmall.seckill.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions";
    private final String SKUKILL_CACHE_PREFIX = "seckill:skus";
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void uoloadSeckillSkuLatest3Days() {
        //掃描最近三天需要參與秒殺的活動
        R session = couponFeignService.getLatest3DaysSession();
        if (session.getCode() == 0) {
            //上架商品
            List<SeckillSessionsWithSkus> seckillSessionsWithSkuses = session.getData(new TypeReference<List<SeckillSessionsWithSkus>>() {
            });
            //緩存到 redis
            //緩存活動信息
            saveSessionInfos(seckillSessionsWithSkuses);
            //緩存活動的關聯商品信息
            saveSessionSkuInfos(seckillSessionsWithSkuses);

        }
    }

    private void saveSessionInfos(List<SeckillSessionsWithSkus> seckillSessionsWithSkus) {
        if (seckillSessionsWithSkus != null) {
            seckillSessionsWithSkus.stream().forEach(session -> {
                Long startTime = session.getStartTime().getTime();
                Long endTime = session.getEndTime().getTime();
                String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
                Boolean hasKey = stringRedisTemplate.hasKey(key);

                if (!hasKey) {
                    List<String> skuIds = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId().toString() + "_" + item.getSkuId().toString()).collect(Collectors.toList());
                    stringRedisTemplate.opsForList().leftPushAll(key, skuIds);
                }
            });
        }
    }

    private void saveSessionSkuInfos(List<SeckillSessionsWithSkus> seckillSessionsWithSkus) {
        seckillSessionsWithSkus.stream().forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            session.getRelationSkus().stream().forEach(seckillSkuVo -> {

                String token = UUID.randomUUID().toString().replace("-", "");
                String skuInfoKey = seckillSkuVo.getPromotionSessionId().toString() + "_" + seckillSkuVo.getSkuId().toString();

                if (!ops.hasKey(skuInfoKey)) {
                    //緩存商品
                    SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                    //sku 的基本數據
                    R skuInfo = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                    if (skuInfo.getCode() == 0) {
                        SkuInfoVo skuInfoVo = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        seckillSkuRedisTo.setSkuInfoVo(skuInfoVo);
                    }
                    //sku 的秒殺訊息
                    BeanUtils.copyProperties(seckillSkuVo, seckillSkuRedisTo);

                    //設置當前商品的秒殺時間信息
                    seckillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                    seckillSkuRedisTo.setEndTime(session.getEndTime().getTime());

                    //隨機碼
                    seckillSkuRedisTo.setRandomCode(token);

                    String s = JSON.toJSONString(seckillSkuRedisTo);
                    ops.put(skuInfoKey, s);

                    //如果當前這個場次的商品的庫存信息已經上架才不需要上架
                    //引入分布式信號量 使用庫存作為分布式的信號量
                    RSemaphore rSemaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    //商品可以秒殺的最大件數做為信號量
                    rSemaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                }
            });
        });
    }

    @SentinelResource(value = "getCurrentSeckillSkusResource", blockHandler = "blockHandler")
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //確定當前時間屬於哪個秒殺場次
        Long timeStamp = new Date().getTime();

        try (Entry entry = SphU.entry("seckillSkus")) {
            Set<String> keys = stringRedisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
            for (String key : keys) {
                String timeInterval = key.replace(SESSIONS_CACHE_PREFIX, "");
                String[] timeIntervals = timeInterval.split("_");
                Long start = Long.parseLong(timeIntervals[0]);
                Long end = Long.parseLong(timeIntervals[1]);

                if (timeStamp >= start && timeStamp <= end) {
                    //獲取這個秒殺場次需要的所有商品信息
                    //相當於獲取所有
                    List<String> range = stringRedisTemplate.opsForList().range(key, -100, 100);
                    BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    List<String> list = hashOps.multiGet(range);
                    if (list != null) {
                        List<SeckillSkuRedisTo> collection = list.stream().map(item -> {
                            SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(item.toString(), SeckillSkuRedisTo.class);
                            return seckillSkuRedisTo;
                            //當前秒殺開始就需要隨機碼
//                        seckillSkuRedisTo.setRandomCode(null);
                        }).collect(Collectors.toList());

                        return collection;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.error("資源被限制流量：" + e.getMessage());
        }

        return null;
    }

    public List<SeckillSkuRedisTo> blockHandler(BlockException e) {
        log.info("getCurrentSeckillSkusResource被限流了");
        return null;
    }

    @Override
    public SeckillSkuRedisTo skuSeckillInfo(Long skuId) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (keys != null && keys.size() > 0) {

            String regx = "\\d_" + skuId;

            for (String key : keys) {
                if (Pattern.matches(regx, key)) {
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);

                    Long current = new Date().getTime();
                    if (current >= seckillSkuRedisTo.getStartTime() && current <= seckillSkuRedisTo.getEndTime()) {

                    } else {
                        seckillSkuRedisTo.setRandomCode(null);
                    }

                    return seckillSkuRedisTo;
                }
            }
        }

        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        long s1 = System.currentTimeMillis();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        //獲取當前秒殺商品的詳細訊息
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String seckill = hashOps.get(killId);

        if (StringUtils.isEmpty(seckill)) {
            return null;
        }

        SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(seckill, SeckillSkuRedisTo.class);
        //合法性校驗
        Long startTime = seckillSkuRedisTo.getStartTime();
        Long endTime = seckillSkuRedisTo.getEndTime();
        Long currentTime = new Date().getTime();

        Long ttl = endTime - currentTime;
        //也可以給一個 redis 過期時間
        if (currentTime >= startTime && currentTime <= endTime) {
            String randomCode = seckillSkuRedisTo.getRandomCode();
            String skuId = seckillSkuRedisTo.getPromotionSessionId() + "_" + seckillSkuRedisTo.getSkuId();
            if (randomCode.equals(key) && killId.equals(skuId)) {
                if (num <= seckillSkuRedisTo.getSeckillLimit().intValue()) {
                    // 驗證此人是否已經買過 如果秒殺成功就去佔位 userId_sessionId_skuId
                    //SETNX
                    String redisKey = memberResponseVo.getId() + "_" + skuId;
                    //自動過期
                    Boolean firstPurchased = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                    //佔位成功 表示這個人從來沒買過
                    if (firstPurchased) {
                        RSemaphore rSemaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
//                        try {
                        boolean purchase = rSemaphore.tryAcquire(num);
                        //沒丟異常就是秒殺成功
                        //快速下單 發送 MQ 消息
                        if (purchase) {
                            String timeId = IdWorker.getTimeId();
                            SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                            seckillOrderTo.setOrderSn(timeId);
                            seckillOrderTo.setMemberId(memberResponseVo.getId());
                            seckillOrderTo.setNum(num);
                            seckillOrderTo.setPromotionSessionId(seckillSkuRedisTo.getPromotionSessionId());
                            seckillOrderTo.setSkuId(seckillSkuRedisTo.getSkuId());
                            seckillOrderTo.setSeckillPrice(seckillSkuRedisTo.getSeckillPrice());
                            rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", seckillOrderTo);
                            long s2 = System.currentTimeMillis();
                            log.info("耗時...", (s2 - s1));
                            return timeId;
                        }

                        return null;
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        return null;
                    }
                } else {

                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        return null;
    }
}
