package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan("com.atguigu.crowd.mapper")
public class MysqlMain3000 {
    public static void main(String[] args) {
        SpringApplication.run(MysqlMain3000.class,args);
    }
}
