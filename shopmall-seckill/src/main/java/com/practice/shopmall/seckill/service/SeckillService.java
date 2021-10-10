package com.practice.shopmall.seckill.service;

import com.practice.shopmall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

public interface SeckillService {
    void uoloadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo skuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
