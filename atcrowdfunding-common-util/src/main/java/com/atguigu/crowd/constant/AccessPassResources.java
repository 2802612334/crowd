package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {

    public static final Set<String> REQUEST_SET = new HashSet<>();              // 放行请求

    public static final Set<String> STATIC_REQUEST_SET = new HashSet<>();       // 放行静态资源

    static {
        REQUEST_SET.add("/");
        REQUEST_SET.add("/member/do/register/page.html");                       // 注册页面
        REQUEST_SET.add("/member/do/login/page.html");                          // 登陆页面
        REQUEST_SET.add("/member/do/login.html");                               // 登录请求
        REQUEST_SET.add("/member/do/register.html");                            // 注册请求
        REQUEST_SET.add("/member/send/code.json");                              // 发送验证码
    }

    static {
        STATIC_REQUEST_SET.add("bootstrap");
        STATIC_REQUEST_SET.add("css");
        STATIC_REQUEST_SET.add("fonts");
        STATIC_REQUEST_SET.add("img");
        STATIC_REQUEST_SET.add("jquery");
        STATIC_REQUEST_SET.add("layer");
        STATIC_REQUEST_SET.add("script");
        STATIC_REQUEST_SET.add("ztree");
    }

    public static boolean judgeCurrentServletPathWetherStaticResource(String servletPath){
        if(servletPath == null || "".equals(servletPath)){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        boolean flag = false;
        if(REQUEST_SET.contains(servletPath)){
            flag = true;
        }else {
            String[] staticSplit = servletPath.split("/");
            // 考虑到第一个斜杠左边经过拆分后得到一个空字符串是数组的第一个元素，所以需要使用下标 1 取第二个元素
            if(STATIC_REQUEST_SET.contains(staticSplit[1])){
                flag = true;
            }
        }
        return flag;
    }

}
