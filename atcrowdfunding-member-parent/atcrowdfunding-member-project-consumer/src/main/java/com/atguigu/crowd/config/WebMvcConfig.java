package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/project/crowd/protocol/page.html").setViewName("show");       // 众筹首页
        registry.addViewController("/project/agree/protocol/page.html").setViewName("agree");      // 协议页面
        registry.addViewController("/project/launch/protocol/page.html").setViewName("launch");      // 发起页面
        registry.addViewController("/project/return/protocol/page.html").setViewName("return");     // 回报页面
        registry.addViewController("/project/confirm/protocol/page.html").setViewName("confirm");     // 确认页面
        registry.addViewController("/project/create/success.html").setViewName("success");          // 项目上传成功页面
    }
}
