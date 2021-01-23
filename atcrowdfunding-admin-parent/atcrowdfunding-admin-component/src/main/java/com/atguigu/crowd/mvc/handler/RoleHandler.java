package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;


    /*
    * 查询角色信息
    * */
    @RequestMapping("/role/get/page/info.json")
    @ResponseBody
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "7") Integer pageSize
    ){
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum,pageSize,keyword);
        return ResultEntity.successWithData(pageInfo);
    }

    /*
    * 添加角色
    * */
    @RequestMapping("/role/save.json")
    @ResponseBody
    public ResultEntity<Object> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithOutData();
    }

    /*
    * 修改角色信息
    * */
    @RequestMapping("/role/edit.json")
    @ResponseBody
    public ResultEntity<Object> editRole(Role role){
        roleService.updateRole(role);
        return ResultEntity.successWithOutData();
    }

    /*
    * 删除角色信息
    * */
    @RequestMapping("/role/remove/role/id.json")
    @ResponseBody
    public ResultEntity<Object> removeById(@RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithOutData();
    }
}
