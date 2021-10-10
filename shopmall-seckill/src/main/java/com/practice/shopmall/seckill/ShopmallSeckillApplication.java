package com.practice.shopmall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 整合 Sentinel
 * 導入依賴
 *         <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
 *         </dependency>
 *         <dependency>
 *             <groupId>com.alibaba.csp</groupId>
 *             <artifactId>sentinel-web-servlet</artifactId>
 *         </dependency>
 * 下載 sentinel 控制台 jar
 * 執行 java -jar /pathTo/sentinel.jar --server.port=
 *
 * 配置 sentinel 控制台地址信息
 * 在控制台調整參數 「默認所有的流量控制設置保存在內容中 重啟項目後就會失效」
 *
 * 每個微服務都導入
 * 		<dependency>
 * 			<groupId>org.springframework.boot</groupId>
 * 			<artifactId>spring-boot-starter-actuator</artifactId>
 * 		</dependency>
 * 並且配置 management.endpoints.web.exposure.include=*
 * 自定義 sentinel 流量控制返回數據
 *
 * 使用 sentinel 來保護 feign 遠程調用 熔斷：
 * 	調用方的熔斷保護 feign.sentinel.enabled=true
 * 	調用方手動指定遠程服務的降級策略
 * 	調用方手動指定遠程服務的降級策略 遠程服務被降級處理 觸發我們的熔斷回調方法
 * 	超大流量的時候 必須犧牲一些遠程服務 在服務的提供方（遠程服務）指定降級策略
 * 	提供方是在運行 但是不運行自己的業務邏輯 返回的是默認的熔斷數據（限流的數據）
 *
 * 自定義受保護的資源
 * 	        try (Entry entry = SphU.entry("seckillSkus")) {
 * 	            業務邏輯
 * 	        } catch (Exception e) {}
 * 基於註解
 *     @SentinelResource(value = "getCurrentSeckillSkusResource", blockHandler = "blockHandler")
 *	要配置腺瘤以後的默認返回
 *	url 請求可以設置統一返回
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ShopmallSeckillApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmallSeckillApplication.class, args);
	}

}
