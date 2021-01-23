package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.exception.RoleNameExistException;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        // 1.调用分页插件，配置分页
        PageHelper.startPage(pageNum,pageSize);
        // 2.查询数据
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);
        // 3.封装为PageInfo对象
        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        try{
            roleMapper.insert(role);
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                // 说明该角色名已存在抛出异常
                throw new RoleNameExistException(CrowdConstant.MESSAGE_SYSTEM_ERROR_ROLE_NAME_NOT_UNIQUE);
            }
        }

    }

    @Override
    public void updateRole(Role role) {
        // 方案一
        // 1.查询更新角色名在数据库中是否存在
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andNameEqualTo(role.getName());
        long count = roleMapper.countByExample(roleExample);
        // 2.根据查询结果，做出相应处理
        if(count != 0){
            // 说明该角色名已存在
            throw new RoleNameExistException(CrowdConstant.MESSAGE_SYSTEM_ERROR_ROLE_NAME_NOT_UNIQUE);
        }
        // 说明该角色名不存在，执行更新
        roleMapper.updateByPrimaryKey(role);

        // 方案二：使用try...catch...捕获异常，因为数据设置了唯一约束，当程序抛出异常，证明角色已存在，做出相应的异常处理
    }

    @Override
    public void removeRole(List<Integer> roleIdList) {
        // 设置删除条件 where id in (roleIdList)
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleIdList);
        // 执行删除
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        List<Role> assignRole = roleMapper.selectAssignedRole(adminId);
        return assignRole;
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        List<Role> unAssignRole = roleMapper.selectUnAssignedRole(adminId);
        return unAssignRole;
    }
}
