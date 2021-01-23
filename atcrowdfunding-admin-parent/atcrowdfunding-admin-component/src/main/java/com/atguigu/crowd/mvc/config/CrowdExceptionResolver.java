package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.exception.RoleNameExistException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@ControllerAdvice
public class CrowdExceptionResolver {

    // 权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView accessDeniedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException{
            String viewName = "system-error";
            return commonResolver(request,response,exception,viewName);
    }

    // 登录异常
    @ExceptionHandler(LoginFailedException.class)
    public ModelAndView loginFailedExceptionHandler(HttpServletRequest request,
                                             HttpServletResponse response,
                                             Exception exception) throws IOException{
        String viewName = "admin-login";
        return commonResolver(request,response,exception,viewName);
    }

    // 角色重复异常处理
    @ExceptionHandler(RoleNameExistException.class)
    public ModelAndView RoleNameExistExceptionHandler(HttpServletRequest request,
                                             HttpServletResponse response,
                                             Exception exception) throws IOException{
        return commonResolver(request,response,exception,null);
    }

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView nullPointExceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception exception) throws IOException {
        String viewName = "system-error";
        return commonResolver(request,response,exception,viewName);
    }

    /*
     * 用户名重复
     * */
    @ExceptionHandler(LoginAcctAlreadyInUseException.class)
    public ModelAndView loginAcctAlreadyInUseExceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception exception) throws IOException{
        String viewName = "admin-add";
        String url = request.getRequestURI().toString();
        if(url.equals("/crowd/admin/to/edit/update.html")){
            viewName = "admin-edit";
        }else if(url.equals("/save/admin.html")){
            viewName = "admin-add";
        }
        return commonResolver(request,response,exception,viewName);
    }

    // 判断当前请求是否为异步，并且做出相应处理
    private  ModelAndView commonResolver(HttpServletRequest request, HttpServletResponse response,
                                         Exception exception,String viewName) throws IOException {
        if(CrowdUtil.judgeRequestType(request)){
            // 设置响应头为json
            response.setContentType("application/json;charset=utf-8");
            //返回异常信息
            String message = exception.getMessage();
            ResultEntity<Object> resultEntity = ResultEntity.failed(message);
            //将异常对象转换为JSON
            Gson gson = new Gson();
            String dataJson = gson.toJson(resultEntity);
            Writer out = response.getWriter();
            out.write(dataJson);
            out.flush();
            out.close();
            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
