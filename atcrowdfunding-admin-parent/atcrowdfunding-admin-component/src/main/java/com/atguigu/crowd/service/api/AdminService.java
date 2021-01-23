package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminService {

    Admin getAdminById(Integer id);

    Admin getAdminByLoginAcct(String loginAcct);

    void saveAdmin(Admin admin);

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize);

    boolean removeAdminById(Integer id);

    void updateAdmin(Admin admin, HttpServletRequest request);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
