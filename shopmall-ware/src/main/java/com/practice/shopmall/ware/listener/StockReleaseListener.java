package com.practice.shopmall.ware.listener;

import com.alibaba.fastjson.TypeReference;
import com.practice.common.to.mq.OrderTo;
import com.practice.common.to.mq.StockDetailTo;
import com.practice.common.to.mq.StockLockedTo;
import com.practice.common.utils.R;
import com.practice.shopmall.ware.dao.WareSkuDao;
import com.practice.shopmall.ware.entity.WareOrderTaskDetailEntity;
import com.practice.shopmall.ware.entity.WareOrderTaskEntity;
import com.practice.shopmall.ware.feign.OrderFeignService;
import com.practice.shopmall.ware.service.WareOrderTaskDetailService;
import com.practice.shopmall.ware.service.WareOrderTaskService;
import com.practice.shopmall.ware.service.WareSkuService;
import com.practice.shopmall.ware.vo.OrderVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    WareSkuDao wareSkuDao;

    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo stockLockedTo, Message message, Channel channel) throws IOException {

        System.out.println("收到解鎖庫存的消息");

        try {
            wareSkuService.unlockStock(stockLockedTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


    public void handleOrderCloseRelease(OrderTo orderTo, Message message, Channel channel) throws IOException {
        System.out.println("訂單關閉準備解鎖庫存");

        try {
            wareSkuService.unlockStock(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


}
