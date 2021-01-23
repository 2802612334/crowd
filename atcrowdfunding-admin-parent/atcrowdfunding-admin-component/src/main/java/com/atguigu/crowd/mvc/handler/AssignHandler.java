package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    /*
    * 管理员分配角色
    * */
    @RequestMapping("/assign/do/role/assign.html")
    public String doAssignRole(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList
    ){
        adminService.saveAdminRoleRelationship(adminId,roleIdList);
        return "redirect:/admin/to/user/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /*
    * 角色分配权限
    * */
    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<Object> doAssignAuth(
            // 使用map集合的方式接受前端传来的ajax对象
            @RequestBody Map<String,List<Integer>> map
            ){
        authService.saveRoleAuthRelathinship(map);
        return ResultEntity.successWithOutData();
    }

    /*
    * 获取管理员的角色
    * */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
            Model model
    ){
        // 获取当前用户已分配角色
        List<Role> assginRole = roleService.getAssignedRole(adminId);
        // 获取当前用户未分配角色
        List<Role> unAssginRole = roleService.getUnAssignedRole(adminId);
        model.addAttribute("assginRole",assginRole);
        model.addAttribute("unAssginRole",unAssginRole);
        return "assign-role";
    }

    /*
     * 获取角色的操作权限
     * */
    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<Object> getAssignAuth(Integer roleId){
        List<Integer> authList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authList);
    }

}
