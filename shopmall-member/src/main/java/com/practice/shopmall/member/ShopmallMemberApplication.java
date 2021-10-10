package com.practice.shopmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 想要遠程調用別的服務
 * 1. 引入 open-feign
 * 2. 編寫一個接口，告訴Spring Cloud這個接口需要調用遠程服務
 * 3. 聲明每一個方法都是調用哪個遠程服務的哪個請求
 * 4. 開啟遠程調用的功能
 */

@EnableFeignClients(basePackages = "com.practice.shopmall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class ShopmallMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmallMemberApplication.class, args);
	}

}
