package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(loginAcct);

        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        Admin admin = adminList.get(0);
        return admin;
    }

    @Override
    public void saveAdmin(Admin admin) {
        // 生成创建时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = dateFormat.format(date);
        admin.setCreateTime(createTime);
//        // 密码明文加密
//        String md5Password = CrowdUtil.md5(admin.getUserPswd());
        String password = bCryptPasswordEncoder.encode(admin.getUserPswd());
        admin.setUserPswd(password);
        // 添加用户
        try{
            adminMapper.insert(admin);
        }catch (Exception e){
            // 如果抛出异常 DuplicateKeyException 说明账号重复了
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
            }
        }
    }

    /**
     * 登录验证
     * */
    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录账号查询Admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        // 2.判断Admin对象是否为null
        if(list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if(list.size() > 1){
            throw new LoginFailedException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 3.如果Admin对象为null则抛出异常
        if(list.get(0) == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        Admin admin = list.get(0);
        // 5.将表单提交的明文密码进行加密
        String md5UserPswd = CrowdUtil.md5(userPswd);
        // 6.对密码进行比较
        // 7.如果比较结果是不一致则抛出异常
        if(!md5UserPswd.equals(admin.getUserPswd())){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 8.如果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用分页插件，查询数据
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminMapper.selectAdminListByKeyword(keyword);
        // 2.将数据封装为PageInfo对象
        PageInfo<Admin> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public boolean removeAdminById(Integer id) {
        return adminMapper.deleteByPrimaryKey(id) > 0 ? true : false;
    }

    @Override
    public void updateAdmin(Admin admin,HttpServletRequest request) {
        /*
        * 登录账号不可重复
        * */
        // 1.查询当前账号是否重复
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(admin.getLoginAcct());
        long count = adminMapper.countByExample(adminExample);

        // 2.账号无重复，修改用户信息
        if(count == 0){
            adminMapper.updateByPrimaryKeySelective(admin);
        }else{
            // 3.账号重复，抛出异常信息,将查询对象的信息放入
            admin = getAdminById(admin.getId());
            request.setAttribute(CrowdConstant.ATTR_NAME_EDIT_ADMIN,admin);
            throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 1.删除该管理员的所有角色
        adminMapper.deleteOldRelationship(adminId);
        // 2.保存该用户的所有角色
        if(roleIdList != null && roleIdList.size() > 0){
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }
    }
}
