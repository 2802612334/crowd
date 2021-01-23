package com.atguigu.crowd.mvc.interceptor;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前用户Session
        HttpSession session = request.getSession();
        // 获取当前用户信息
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // 如果admin为null，说明服务器没有该用户的信息，抛出异常，拦截请求
        if(admin == null){
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_SYSTEM_LOGIN_OUT);
        }
        // 验证通过，请求放行
        return true;
    }

}
