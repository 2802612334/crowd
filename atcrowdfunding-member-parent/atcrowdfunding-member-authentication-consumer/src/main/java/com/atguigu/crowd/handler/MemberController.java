package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MailRemoteFeignService;
import com.atguigu.crowd.api.MySQLRemoteFeignService;
import com.atguigu.crowd.api.RedisRemoteFeignService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.RegisterVO;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MailRemoteFeignService mailRemoteFeignService;

    @Autowired
    private RedisRemoteFeignService redisRemoteFeignService;

    @Autowired
    private MySQLRemoteFeignService mySQLRemoteFeignService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/member/do/login/page.html";
    }

    @RequestMapping("/do/login.html")
    public String doLogin(@RequestParam("loginacct") String loginacct,
                          @RequestParam("userpswd") String userpswd,
                          Model model,
                          HttpSession httpSession){
        // 1.根据用户名获取当前用户信息
        ResultEntity<MemberPO> loginAcctEntity = mySQLRemoteFeignService.getMemberPOByLoginAcctRemote(loginacct);
        if(loginAcctEntity.getData() == null){
            // 说明系统中并无该用户
            model.addAttribute(CrowdConstant.ATTR_NAME_LOGIN_ERROR,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "login";
        }
        // 2.比对密码是否与数据库加盐密码一致
        boolean flag = bCryptPasswordEncoder.matches(userpswd,loginAcctEntity.getData().getUserpswd());
        if(!flag){
            // 说明用户密码输入错误
            model.addAttribute(CrowdConstant.ATTR_NAME_LOGIN_ERROR,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "login";
        }
        // 4.登录成功，将用户信息封装到MemberVo存入session域
        MemberLoginVO memberLoginVo = new MemberLoginVO();
        BeanUtils.copyProperties(loginAcctEntity.getData(),memberLoginVo);
        log.info("登录用户为：" + memberLoginVo.toString());
        httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVo);
        return "redirect:/member/to/center/page.html";
    }

    @RequestMapping("/do/register.html")
    public String doRegister(RegisterVO registerVO, Model model){
        // 1.比对验证码是否正确
        String key = CrowdConstant.SYSTEM_VERIFICATION_CODE + registerVO.getEmail();
        ResultEntity<String> redisEntity = redisRemoteFeignService.getRedisStringValueByKeyRemote(key);
        if(redisEntity.getData() == null){
            // redis中不存在该用户验证码信息
            model.addAttribute(CrowdConstant.MESSAGE_REGISTER_FAILED,CrowdConstant.CODE_IS_NOT_EXIS);
            return "register";
        }
        if(!registerVO.getCode().equals(redisEntity.getData())){
            // 验证码输入有误
            model.addAttribute(CrowdConstant.MESSAGE_REGISTER_FAILED,CrowdConstant.CODE_IS_ERROR);
            return "register";
        }
        // 2.使用beanUtil，实现视图实体到数据库实体的转换
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(registerVO,memberPO);
        // 3.对提交密码进行加密处理
        String encodePassword = bCryptPasswordEncoder.encode(memberPO.getUserpswd());
        memberPO.setUserpswd(encodePassword);
        // 4.保存注册用户到数据库
        ResultEntity<String> resultEntity = mySQLRemoteFeignService.saveMember(memberPO);
        if(ResultEntity.getFAILED().equals(resultEntity.getResult())){
            // 由于用户名重复或者服务器问题导致注册失败，返回注册页面
            model.addAttribute(CrowdConstant.MESSAGE_REGISTER_FAILED,resultEntity.getMessage());
            return "register";
        }
        // 注册成功，导航到登录页面，为了避免表单重复提交，这里使用重定向的方式返回
        return "redirect:/member/do/login/page.html";
    }

    /**
     * @author root
     * @param mailAddress 邮箱地址
     * */
    @ResponseBody
    @RequestMapping("/send/code.json")
    public ResultEntity<String> sendCode(@RequestParam("mailaddress") String mailAddress){
        // 1.检查是否为用户发送过邮件
        String key = CrowdConstant.SYSTEM_VERIFICATION_CODE + mailAddress;
        ResultEntity<String> isExisData = redisRemoteFeignService.getRedisStringValueByKeyRemote(key);
        if(isExisData.getData() != null){
            // 已发送邮件，无需发送
            return ResultEntity.failed(CrowdConstant.MESSAGE_SEND_MAILCODE_EXIS);
        }
        // 2.发送邮件
        ResultEntity<String> resultEntity = mailRemoteFeignService.sendRegisterMail(mailAddress);
        if(ResultEntity.getFAILED().equals(resultEntity.getResult())){
            // 邮件发送失败
            log.info("邮件服务发送失败，详细信息：" + resultEntity.getMessage());
            return ResultEntity.failed(CrowdConstant.MESSAGE_SEND_MAIL);
        }
        // 3.将返回的验证码存入redis中，默认过期时间15分钟
        String value = resultEntity.getData();
        ResultEntity<String> redisEntity = redisRemoteFeignService.setRedisKeyValueRemoteWithTimeout(key, value, 15, TimeUnit.MINUTES);
        if(ResultEntity.getFAILED().equals(redisEntity.getResult())){
            // redis存储失败
            log.info("redis服务存储验证码失败，详细信息：" + resultEntity.getMessage());
            return ResultEntity.failed(CrowdConstant.MESSAGE_SEND_MAIL);
        }
        // 4.返回结果
        return ResultEntity.successWithOutData();
    }
}
