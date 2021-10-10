package com.practice.shopmall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 核心原理
 * @EnableRedisHttpSession 導入 RedisHttpSessionConfiguration
 *	給容器中添加一個組件
 *		RedisOperationsSessionRepository:redis操作session的增刪改查的封裝類
 *		SessionRepositoryFilter: session 存儲過濾器 每個請求近來都必須經過 filter
 *			創建的時候 就自動從容器中獲取到了 SessionRepository
 *			原生的request response都被包裝 SessionRepositoryRequestWrapper, SessionRepositoryResponseWrapper
 *			以後獲取session request.getSession()
 *			wrapperRequest.getSession() SessionRepository 中獲取到的
 *
 *
 */


@EnableRedisHttpSession
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
