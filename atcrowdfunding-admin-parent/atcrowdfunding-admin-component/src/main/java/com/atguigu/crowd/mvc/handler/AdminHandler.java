package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    AdminService adminService;

    /*
    * 用户登录
    * */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(String loginAcct, String userPswd, HttpSession session){
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        /*
        * 为了避免请求转发用户重复提交表单数据，降低服务器性能，使用重定向的方式，避免重复提交表单数据（将用户登录成功的
        * 数据放到session域中）。
        * */
        String view = "redirect:/admin/to/main/page.html";
        return view;
    }

    /*
    * 用户注销
    * */
    @RequestMapping("/admin/do/logout.html")
    public String logOut(HttpSession session){
        // 强制销毁session作用域的所有内容
        session.invalidate();
        String view = "redirect:/admin/to/login/page.html";
        return view;
    }

    /*
    * 获取用户分页信息
    * */
    @RequestMapping("/admin/to/user/page.html")
    public String getAdminPage(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "7") Integer pageSize,
            ModelMap modelMap){
        PageInfo<Admin> page = adminService.getAdminPage(keyword,pageNum,pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,page);
        return "admin-user";
    }

    /*
    * 删除单个用户
    * */
    @RequestMapping("/admin/to/remove/{id}/{pageNum}/{keyword}.html")
    public String removeAdmin(@PathVariable("id") Integer id,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable("keyword") String keyword){
        /*
        * 需求分析：用户单击删除，传入指定用户id，删除即可。重定向到当前页面
        * 参数需求：删除用户id，删除后指定页，模糊查询keyword
        * 使用restful风格
        * */
        boolean flag = adminService.removeAdminById(id);
        if(!flag){
              throw new RuntimeException("删除失败！");
        }

        return "redirect:/admin/to/user/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /*
    * 修改单个用户
    * */
    @RequestMapping("/admin/to/edit/update.html")
    public String editAdmin(Admin admin, Integer pageNum, String keyword, HttpServletRequest request){
        adminService.updateAdmin(admin,request);
        return "redirect:/admin/to/user/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /*
    * 保存用户，具有经理角色才可以访问
    * */
    @PreAuthorize("hasRole('经理')")
    @RequestMapping("/save/admin.html")
    public String saveAdmin(Admin admin
            /*,
                            @RequestParam(value = "pageNum",defaultValue = "") Integer pageNum,
                            @RequestParam(value = "keyword",defaultValue = "") String keyword*/
            ){
        adminService.saveAdmin(admin);
        // 添加成功返回最后一页
        /*
        if(pageNum.toString().equals("")){
            pageNum = Integer.MAX_VALUE;
        }
        return "redirect:/admin/to/user/page.html?pageNum="+pageNum+"&keyword="+keyword;
         */
        return "redirect:/admin/to/user/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /*
    * 用户更新
    * */
    @RequestMapping("admin/to/edit/page.html")
    public String editAdmin(@RequestParam("id") Integer id, Model model){
        Admin admin = adminService.getAdminById(id);
        model.addAttribute(CrowdConstant.ATTR_NAME_EDIT_ADMIN,admin);
        return "admin-edit";
    }
}
