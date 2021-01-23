package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.SecurityAdmin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 1.根据用户名从数据库中获取当前用户
        Admin admin = adminService.getAdminByLoginAcct(userName);

        Integer adminId = admin.getId();

        // 2.获取该用户角色信息
        List<Role> roleList = roleService.getAssignedRole(adminId);

        // 3.获取该用户已分配权限
        List<String> authList =  authService.getAssignedAuthNameByAdminId(adminId);

        // 4.将用户信息封装到UserDetails对象
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();

        for (Role role : roleList) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        for (String authName : authList) {
            grantedAuthoritySet.add(new SimpleGrantedAuthority(authName));
        }

        SecurityAdmin user = new SecurityAdmin(admin,grantedAuthoritySet);
        return user;
    }

}
