package com.practice.shopmall.seckill.schedule;

import com.practice.shopmall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
/**
 * 秒殺商品定時上架
 *  每天晚上三點 上架最近三天需要秒殺的商品
 *  當天00:00:00 - 23:59:59
 *  明天00:00:00 - 23:59:59
 *  後天00:00:00 - 23:59:59
 */
public class SeckillSkuScheduled {

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    private final String UPLOAD_LOCK = "seckill:upload:lock";

    //TODO 冪等性處理
    @Scheduled(cron = "0 * * * * ?")
    public void uoloadSeckillSkuLatest3Days(){
        log.info("上架秒殺的商品信息");
        //分佈式鎖 鎖的業務執行完成 狀態已經更新完成 釋放鎖之後 其他人會拿到最新的狀態
        RLock lock = redissonClient.getLock(UPLOAD_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        //重複上架無需處理
        try {
            seckillService.uoloadSeckillSkuLatest3Days();
        } finally {
            lock.unlock();
        }
    }

}
