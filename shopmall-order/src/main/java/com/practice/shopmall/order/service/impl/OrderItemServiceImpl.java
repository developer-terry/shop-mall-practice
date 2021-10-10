package com.practice.shopmall.order.service.impl;

import com.practice.shopmall.order.entity.OrderEntity;
import com.practice.shopmall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.practice.common.utils.PageUtils;
import com.practice.common.utils.Query;

import com.practice.shopmall.order.dao.OrderItemDao;
import com.practice.shopmall.order.entity.OrderItemEntity;
import com.practice.shopmall.order.service.OrderItemService;

@RabbitListener(queues = {"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 參數可以寫以下類型
     * Message message 原生消息詳細信息 頭+體
     * T 發送的消息類型 OrderReturnReasonEntity content
     * Channel channel 當前傳輸數據的通動
     *
     * Queue: 可以多人監聽 只要收到消息後 就會刪除消息 只能有一個人收到此消息
     * 場景
     *  訂單服務啟動多個 同一個消息 只能有一個客戶端接收到
     *  只有一個消息完全處理完 方法運行結束 才能接受到下一個消息
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void recieveMessage(Message message, OrderReturnReasonEntity content, Channel channel){
        System.out.println("接收到消息...內容:" + message + "==>內容:" + content);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if(deliveryTag % 2 == 0) {
                channel.basicAck(deliveryTag, false);
                System.out.println("簽收了貨物..." + deliveryTag);
            } else {
                //requeue = false 拒收丟棄, requeue = true 發回服務器 服務器重新入隊
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void recieveMessage(Message message, OrderEntity content, Channel channel){
        System.out.println("接收到消息..." + content);
    }
}