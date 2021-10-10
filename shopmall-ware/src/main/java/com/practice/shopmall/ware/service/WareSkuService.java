package com.practice.shopmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.to.mq.OrderTo;
import com.practice.common.to.mq.StockLockedTo;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.ware.entity.WareSkuEntity;
import com.practice.shopmall.ware.vo.LockStockResult;
import com.practice.shopmall.ware.vo.SkuHasStockVo;
import com.practice.shopmall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 22:28:00
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo wareSkuLockVo);

    void unlockStock(StockLockedTo stockLockedTo);

    void unlockStock(OrderTo orderTo);
}

