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
        //?????????????????????????????????????????????
        R session = couponFeignService.getLatest3DaysSession();
        if (session.getCode() == 0) {
            //????????????
            List<SeckillSessionsWithSkus> seckillSessionsWithSkuses = session.getData(new TypeReference<List<SeckillSessionsWithSkus>>() {
            });
            //????????? redis
            //??????????????????
            saveSessionInfos(seckillSessionsWithSkuses);
            //?????????????????????????????????
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
                    //????????????
                    SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                    //sku ???????????????
                    R skuInfo = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                    if (skuInfo.getCode() == 0) {
                        SkuInfoVo skuInfoVo = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        seckillSkuRedisTo.setSkuInfoVo(skuInfoVo);
                    }
                    //sku ???????????????
                    BeanUtils.copyProperties(seckillSkuVo, seckillSkuRedisTo);

                    //???????????????????????????????????????
                    seckillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                    seckillSkuRedisTo.setEndTime(session.getEndTime().getTime());

                    //?????????
                    seckillSkuRedisTo.setRandomCode(token);

                    String s = JSON.toJSONString(seckillSkuRedisTo);
                    ops.put(skuInfoKey, s);

                    //??????????????????????????????????????????????????????????????????????????????
                    //???????????????????????? ???????????????????????????????????????
                    RSemaphore rSemaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    //????????????????????????????????????????????????
                    rSemaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                }
            });
        });
    }

    @SentinelResource(value = "getCurrentSeckillSkusResource", blockHandler = "blockHandler")
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //??????????????????????????????????????????
        Long timeStamp = new Date().getTime();

        try (Entry entry = SphU.entry("seckillSkus")) {
            Set<String> keys = stringRedisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
            for (String key : keys) {
                String timeInterval = key.replace(SESSIONS_CACHE_PREFIX, "");
                String[] timeIntervals = timeInterval.split("_");
                Long start = Long.parseLong(timeIntervals[0]);
                Long end = Long.parseLong(timeIntervals[1]);

                if (timeStamp >= start && timeStamp <= end) {
                    //???????????????????????????????????????????????????
                    //?????????????????????
                    List<String> range = stringRedisTemplate.opsForList().range(key, -100, 100);
                    BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    List<String> list = hashOps.multiGet(range);
                    if (list != null) {
                        List<SeckillSkuRedisTo> collection = list.stream().map(item -> {
                            SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(item.toString(), SeckillSkuRedisTo.class);
                            return seckillSkuRedisTo;
                            //????????????????????????????????????
//                        seckillSkuRedisTo.setRandomCode(null);
                        }).collect(Collectors.toList());

                        return collection;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.error("????????????????????????" + e.getMessage());
        }

        return null;
    }

    public List<SeckillSkuRedisTo> blockHandler(BlockException e) {
        log.info("getCurrentSeckillSkusResource????????????");
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

        //???????????????????????????????????????
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String seckill = hashOps.get(killId);

        if (StringUtils.isEmpty(seckill)) {
            return null;
        }

        SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(seckill, SeckillSkuRedisTo.class);
        //???????????????
        Long startTime = seckillSkuRedisTo.getStartTime();
        Long endTime = seckillSkuRedisTo.getEndTime();
        Long currentTime = new Date().getTime();

        Long ttl = endTime - currentTime;
        //?????????????????? redis ????????????
        if (currentTime >= startTime && currentTime <= endTime) {
            String randomCode = seckillSkuRedisTo.getRandomCode();
            String skuId = seckillSkuRedisTo.getPromotionSessionId() + "_" + seckillSkuRedisTo.getSkuId();
            if (randomCode.equals(key) && killId.equals(skuId)) {
                if (num <= seckillSkuRedisTo.getSeckillLimit().intValue()) {
                    // ?????????????????????????????? ?????????????????????????????? userId_sessionId_skuId
                    //SETNX
                    String redisKey = memberResponseVo.getId() + "_" + skuId;
                    //????????????
                    Boolean firstPurchased = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                    //???????????? ??????????????????????????????
                    if (firstPurchased) {
                        RSemaphore rSemaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
//                        try {
                        boolean purchase = rSemaphore.tryAcquire(num);
                        //??????????????????????????????
                        //???????????? ?????? MQ ??????
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
                            log.info("??????...", (s2 - s1));
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
