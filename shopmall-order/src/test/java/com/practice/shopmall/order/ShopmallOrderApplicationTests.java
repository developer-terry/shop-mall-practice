package com.practice.shopmall.order;

import com.practice.shopmall.order.entity.OrderEntity;
import com.practice.shopmall.order.entity.OrderReturnReasonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class ShopmallOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void sendMessageTest(){
        for (int i = 0; i < 10; i++) {
            if(i % 2 == 0) {
                OrderReturnReasonEntity orderReturnReasonEntity = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setName("哈哈");
                orderReturnReasonEntity.setCreateTime(new Date());
                //如果發送的消息是個對象 我們會使用序列化機制將對象寫出去 因此對象必須實現 Serializable
//                String message = "Hello Java!";
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderReturnReasonEntity, new CorrelationData(UUID.randomUUID().toString()));
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderEntity, new CorrelationData(UUID.randomUUID().toString()));
            }
        }

//        System.out.println("message send successfully " + orderReturnReasonEntity);
    }

    /**
     * 如何創建 exchange queue binding
     * 使用 AmqpAdmin 進行創建
     * 如何收發消息
     */
    @Test
    void createExchange() {
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("exchange[hello-java-exchange] build successfully");
    }

    @Test
    void createQueue() {
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        System.out.println("queue[hello-java-queue] build successfully");
    }

    @Test
    void createBinding() {
        Binding binding = new Binding(
                "hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello.java",
                null
        );

        amqpAdmin.declareBinding(binding);
        System.out.println("bind successfully");
    }
}