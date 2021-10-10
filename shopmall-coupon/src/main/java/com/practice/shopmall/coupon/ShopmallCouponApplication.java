package com.practice.shopmall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1. 加入依賴
 *         <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
 *         </dependency>
 *
 *         <dependency>
 *             <groupId>org.springframework.cloud</groupId>
 *             <artifactId>spring-cloud-starter-bootstrap</artifactId>
 *             <version>3.0.3</version>
 *         </dependency>
 * 2. 創建一個 bootstrap.properties
 * 		spring.application.name=shopmall-coupon
 * 		spring.cloud.nacos.config.server-addr=127.0.0.1:8848
 * 3. 需要給配置中心默認一個叫做數據集(Data id) + properties
 * 		默認規則：應用名.properties (shopmall-coupon.properties)
 * 4. 於 shopmall-coupon.properties 添加設置
 * 5. @RefreshScope + @Value 動態進行修改
 */

/**
 * 1. 命名空間：配置隔離
 * 		默認 public (保留空間)：默認所有新增的配置都在 public 空間
 * 		可以自己新增 開發/測試/生產 等命名空間 利用命名空間來做環境隔離
 * 		注意 bootstrap.properties 配置上需要使用哪個命名空間下的配置
 * 		spring.cloud.nacos.config.namespace=b511b14b-590d-46b6-bb60-ad757b3f684a
 *
 * 		基於每個微服務之間互相隔離配置，每一個微服務都創建自己的命名空間，只加載自己命名空間下的所有配置
 *
 * 	2. 配置集：所有配置的集合
 * 	3. 配置集Id：類似文件名(Data Id)
 * 	4. 配置分組：默認所有的配置集都屬於 DEFAULT_GROUP
 * 		bootstrap.properties 中加上
 * 		spring.cloud.nacos.config.group=1111
 *
 * 	命名空間切微服務
 * 	分組切環境
 *
 * 	同時加載多個配置集
 * 		微服務任何配置信息，任何配置文件都可以放在廢置中心
 * 		只需要在 bootstrap.properties 說明加載配置中心哪些配置文件即可
 * 		以往 springboot 任何方法從配置文件中獲取值，都能使用
 * 		配置中心有的配置將被優先使用
 */
@EnableDiscoveryClient
@MapperScan("com.practice.shopmall.coupon.dao")
@SpringBootApplication
public class ShopmallCouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmallCouponApplication.class, args);
	}

}
