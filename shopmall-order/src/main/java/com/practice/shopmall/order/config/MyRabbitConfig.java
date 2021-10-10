package com.practice.shopmall.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

@EnableRabbit
@Configuration
public class MyRabbitConfig {

    RabbitTemplate rabbitTemplate;

//    public MyRabbitConfig(RabbitTemplate rabbitTemplate){
//        this.rabbitTemplate = rabbitTemplate;
//        this.initRabbitTemplate();
//    }

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 訂製 RabbitTemplate
     * spring.rabbitmq.publisher-confirm-type=simple
     * 服務收到消息回調 ConfirmCallback
     * spring.rabbitmq.publisher-returns=true
     * 設置確認回調 ReturnCallback
     *
     * 消費端確認(保證每個消息被正確接收 此時才可以 broker 刪除這個消息)
     *  spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *  默認是自動確認的 只要消息接受到 客戶端服務端就會移除這個消息
     *  問題 我們收到很多消息 自動回覆給服務器 ack 只有一個消息處理成功 萬一當機了 就發現所有消息丟失
     *  手動確認 只要我們沒有明確告訴 MQ 貨物被簽收, 沒有 ack, 消息就一直是 unack, 消息不會丟失, 會重新變為 Ready, 下一次有新的 Consumer 連接進來就發送給他
     *  如何簽收
     *      channel.basicAck(deliveryTag, false); 簽收 業務成功完成就應該簽收
     *      channel.basicNack(deliveryTag, false, true); 拒簽 業務失敗 拒簽
     */

//    @PostConstruct //MyRabbitConfig 對象創建完成以後 執行這個方法
    public void initRabbitTemplate(){
        //設置確認回調
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             *
             * @param correlationData 當前消息的唯一關聯數據 (這個消息的唯一 id)
             * @param b 消息是否成功收到
             * @param s 失敗的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                /**
                 * 做好消息確認機制 (publisher, comsumer [手動ack])
                 * 每一個發送的消息都在資料庫做好記錄 定期將失敗的消息再次發送
                 *
                 */

                System.out.println("confirm...correlationData[" + correlationData + "]==>ack[" + b + "]==>cause[" + s + "]");
            }
        });

        //設置消息抵達列隊確認回調
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 只要消息沒有投遞給指定的列隊 就會觸發這失敗回調
             * @param returnedMessage 投遞失敗的消息詳細信息
             */
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("Fail Message[" + returnedMessage + "]");
            }
        });
    }
}
