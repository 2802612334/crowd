package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProjectMain5000 {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMain5000.class,args);
    }
}
