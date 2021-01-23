package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.www.protocol.http.AuthenticationHeader;

import java.util.List;

@Controller
public class AuthHandler {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private AuthService authService;

    /*
    * 获取所有权限
    * */
    @ResponseBody
    @RequestMapping("/assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
        List<Auth> authList = authService.getAllAuth();
        return ResultEntity.successWithData(authList);
    }

}
