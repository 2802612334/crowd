package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Auth> getAllAuth();

    // 获取角色已分配权限
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    // 获取用户已分配权限名称
    List<String> getAssignedAuthNameByAdminId(Integer adminId);

    void saveRoleAuthRelathinship(Map<String, List<Integer>> map);
}
