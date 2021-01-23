package com.atguigu.crowd.api;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("atguigu-crowd-mail")
public interface MailRemoteFeignService {

    @RequestMapping("/send/register/mail")
    public ResultEntity<String> sendRegisterMail(@RequestParam("mailaddress") String mailAddress);
}
