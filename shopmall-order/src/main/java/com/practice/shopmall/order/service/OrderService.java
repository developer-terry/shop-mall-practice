package com.practice.shopmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.practice.common.to.mq.SeckillOrderTo;
import com.practice.common.utils.PageUtils;
import com.practice.shopmall.order.entity.OrderEntity;
import com.practice.shopmall.order.vo.OrderConfirmVo;
import com.practice.shopmall.order.vo.OrderSubmitVo;
import com.practice.shopmall.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author TerryLee
 * @email p134030772@gmail.com
 * @date 2021-06-20 22:13:55
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity orderEntity);

    void createSeckillOrder(SeckillOrderTo seckillOrderTo);
}

