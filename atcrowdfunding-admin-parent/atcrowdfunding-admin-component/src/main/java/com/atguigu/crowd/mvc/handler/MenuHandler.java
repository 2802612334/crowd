package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<Object> removeMenuById(Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithOutData();
    }

    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<Object> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithOutData();
    }

    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<Object> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithOutData();
    }

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree(){
        Menu root = null;
        // 获取全部的Menu节点
        List<Menu> menuList = menuService.getAll();
        // 将全部元素放入键值对中,因为id为Menu的主键，所以不可能重复，键值对不可能产生覆盖的情况
        Map<Integer,Menu> map = new HashMap<>();

        for (Menu menu:menuList) {
            map.put(menu.getId(),menu);
        }
        for (Menu menu:menuList) {
            // 存取对象的pid
            Integer pid = null;
            pid = menu.getPid();
            // 如果pid为null，说明当前节点没有父节点，当前节点为root根节点
            if(pid == null){
                root = menu;
                // 根节点没有父节点，无须执行之后的代码，continue跳出本次循环
                continue;
            }
            // 找到当前节点的父节点，并将该节点存入父节点的children(List<Menu>)属性中
            map.get(pid).getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }

    private ResultEntity<Menu> getOldWholeTree(){
        Menu root = null;
        // 获取全部的Menu节点
        List<Menu> menuList = menuService.getAll();

        for (Menu menu:menuList) {
            // 存取当前节点的父节点(pid)
            Integer pid = menu.getPid();
            // 如果pid为null,说名当前节点没有父节点，说明当前节点为根节点root
            if(pid == null){
                root = menu;
                // 根节点root没有父节点，所以找到该节点可以直接结束本次循环
                continue;
            }
            // 查找当前节点的父节点
            for(Menu maybeFather:menuList){
                // 如果maybeFather的id为当前节点的pid，说明maybeFather是当前节点(menu)的父节点
                if(maybeFather.getId().equals(pid)){
                    maybeFather.getChildren().add(menu);
                    // 一个节点只能有一个父节点，所以当 当前节点找到父节点之后，就可以跳出本次循环了
                    break;
                }
            }
        }
        return ResultEntity.successWithData(root);
    }
}


