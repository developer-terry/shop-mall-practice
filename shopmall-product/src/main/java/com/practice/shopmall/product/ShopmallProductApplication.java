package com.practice.shopmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 引入 redis starter
 * 配置 redis host port 信息
 * 使用 spring boot 自動配置好的 StringRedisTemplate 來操作 redis
 *
 * 引入 spring-boot-starter-cache spring-boot-starter-data-redis
 * 寫配置
 * 	CacheAutoConfiguration 會導入 RedisCacheConfiguration
 * 	自動配置了緩存管理器
 * 	配置使用 redis 進行緩存 spring.cache.type=redis
 * @Cacheable
 * @CacheEvict
 * @CachePut
 * @Caching
 * @CacheConfig
 *
 *	開啟緩存功能 @EnableCaching
 *	只需要使用註解就可以完成緩存工作
 *
 * 	原理
 *    CacheAutoConfiguration 導入 RedisCacheConfiguration 自動配置了緩存管理器 RedisCacheManager
 *    初始化所有的緩存 每個緩存決定使用什麼配置 如果 RedisCacheConfiguration 有就用已經有的 否則就用默認的設置
 *    如果要改緩存的配置 只需要在容器中加上 RedisCacheConfiguration 即可應用到當前 RedisCacheManager 管理的所有緩存分區中
 *
 *
 */
@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.practice.shopmall.product.feign")
@EnableDiscoveryClient
@MapperScan("com.practice.shopmall.product.dao")
@SpringBootApplication
public class ShopmallProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmallProductApplication.class, args);
	}

}
