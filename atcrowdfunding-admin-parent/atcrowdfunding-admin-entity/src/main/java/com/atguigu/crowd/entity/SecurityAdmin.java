package com.atguigu.crowd.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

public class SecurityAdmin extends User {

    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, Set<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        // 擦除originalAdmin属性的密码信息
        originalAdmin.setUserPswd(null);
        originalAdmin = this.originalAdmin;
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

}
