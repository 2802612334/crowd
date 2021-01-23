package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class RedisMain2000 {
    public static void main(String[] args) {
        SpringApplication.run(RedisMain2000.class,args);
    }
}
