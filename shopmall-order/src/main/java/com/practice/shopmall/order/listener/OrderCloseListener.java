package com.practice.shopmall.order.listener;

import com.practice.shopmall.order.entity.OrderEntity;
import com.practice.shopmall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = "order.release.order.queue")
    public void listen(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到過期訂單信息 準備關閉訂單:" + orderEntity.getOrderSn());
        try {
            orderService.closeOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
