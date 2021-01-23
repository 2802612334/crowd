package com.atguigu.handler;

import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@RestController
public class MailRemoteHandler {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMailAddress;

    @RequestMapping("/send/register/mail")
    public ResultEntity<String> sendRegisterMail(@RequestParam("mailaddress") String mailAddress){

        // 1.生成随机验证码
        String code = CrowdUtil.makeVerificationCode();

        // 2.发送邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setSubject("【尚筹网】");
            mimeMessage.setText("【尚筹网】 验证码为：" + code + "，您正在进行尚筹网注册，请忽泄露。如非本人操作，请忽略。");

            InternetAddress from = new InternetAddress();
            from.setAddress(fromMailAddress);
            InternetAddress to = new InternetAddress();
            to.setAddress(mailAddress);

            mimeMessage.addFrom(new Address[]{from});
            mimeMessage.addRecipients(MimeMessage.RecipientType.TO,new Address[]{to});
            mailSender.send(mimeMessage);
            return ResultEntity.successWithData(code);
        } catch (Exception e) {
            log.error("邮件发送失败，请及时处理！");
            return ResultEntity.failed(e.getMessage());
        }
    }
}
