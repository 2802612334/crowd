package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/member/do/register/page.html").setViewName("register");
        registry.addViewController("/member/do/login/page.html").setViewName("login");
        registry.addViewController("/member/to/center/page.html").setViewName("center");
    }

}
