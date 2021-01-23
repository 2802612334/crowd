package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAllAuth() {
        AuthExample authExample = new AuthExample();
        AuthExample.Criteria criteria = authExample.createCriteria();

        List<Auth> authList = authMapper.selectByExample(authExample);
        return authList;
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }


    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        List<String> authList = authMapper.selectAssignedAuthNameByAdminId(adminId);
        return authList;
    }

    @Override
    public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
        // 获取数据
        List<Integer> authIdArray = map.get("authIdArray");
        Integer roleId = map.get("roleId").get(0);

        // 删除角色本有的所有权限
        authMapper.deleteOldRelathinship(roleId);

        if(authIdArray == null || authIdArray.size() == 0){
            // 说明该角色并没有分配权限，退出本方法即可
            return;
        }

        authMapper.insertNewRelathinship(roleId,authIdArray);
    }
}
