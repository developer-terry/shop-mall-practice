package com.practice.shopmall.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod="shutdown")
    RedissonClient redisson(@Value("${spring.redis.host}") String url,@Value("${spring.redis.port}") String port) throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + url + ":" + port);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
