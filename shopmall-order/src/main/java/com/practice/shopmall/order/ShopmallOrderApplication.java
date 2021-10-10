package com.practice.shopmall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 使用 RabbitMQ
 * 引入ampq場景 RabbitAutoConfiguration 就會自動生效
 * 給容器中自動配置了 RabbitTemplate, AmpqAdmin, CachingConnectionFactory, RabbitMessageTemplate
 * 所有的屬性都是 spring.rabbitmq
 * spring.rabbitmq.host=127.0.0.1
 * spring.rabbitmq.port=5672
 * spring.rabbitmq.virtual-host=/
 * @EnableRabbit
 * 監聽消息 使用 @RabbitListener 必須先有 @EnableRabbit
 *
 *
 * 本地事務失效問題
 * 同一個對象內事務方法互調默認失效 原因 繞過了代理對象 事物使用代理對象來控制的
 * 解決：使用代理對象來調用事務方法
 * 	引入 spring-boot-starter-aop
 * 	開啟 @EnableAspectJAutoProxy(exposeProxy = true) 對外暴露代理對象: 開啟 AspectJ 動態代理功能 以後所有動態代理都是 AspectJ 創建的 (即使沒有接口也可以創建動態代理)
 * 	本類互調用代理對象
 * 	Class class = (Class) AopContext.currentProxy();
 *
 * 	Seata 控制分布式事務
 * 		每一個微服務先必須創建 undo_log 資料表
 * 		安裝事務協調器 seata-server: https://github.com/seata/seata/releases
 * 		整合
 * 			導入依賴
* 		    <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
 *          </dependency>
 *
 *          解壓並啟動 seata-server:
 *          	registry.conf 註冊中心配置 修改 registry type=nacos
 *          	file.conf
 *
 *          所有想要用到分布式事務的微服務都應該使用 seata DataSourceProxy 代理自己的資料庫
 *
 *        	每個微服務都必須在 resources 放入 seata 下載包裡面的 registry.conf / file.conf
 *
 *        	啟動測試分布式事務
 *
 *        	給分佈式大事務的入口標注 @GlobalTransactional
 *        	每一個遠程的小事務用 @Transactional
 */

@EnableFeignClients(basePackages = "com.practice.shopmall.order.feign")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableDiscoveryClient
@SpringBootApplication
public class ShopmallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmallOrderApplication.class, args);
	}

}
