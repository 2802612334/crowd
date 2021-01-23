package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
// 启用全局方法权限控制功能，并且设置prePostEnabled = true，保证@PreAuthority、@PostAuthority、@PreFilter、@PostFilter 生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //允许多请求地址多加斜杠  比如 /msg/list   //msg/list
    @Bean
    public DefaultHttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 基于内存创建用户
//        builder.inMemoryAuthentication().withUser("root").password("root").roles("ADMIN");
        // 使用数据库的方式做登录验证
        builder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        List<String> antMatchers = new ArrayList<>();
        antMatchers.add("/bootstrap/**");
        antMatchers.add("/crowd/**");
        antMatchers.add("/css/**");
        antMatchers.add("/fonts/**");
        antMatchers.add("/img/**");
        antMatchers.add("/jquery/**");
        antMatchers.add("/layer/**");
        antMatchers.add("/script/**");
        antMatchers.add("/ztree/**");
        antMatchers.add("/security/to/login/page.html");

        // 对静态资源和登录页面进行放行
        for (String antMatcher : antMatchers) {
            security.authorizeRequests().antMatchers(antMatcher).permitAll();
        }

        // 关闭_csrf功能
        security.csrf().disable();

        // 其他请求需要做登录验证
        security.authorizeRequests().anyRequest().authenticated();

        // 指定登录页面
        security.formLogin()
                .loginPage("/security/to/login/page.html")
                .loginProcessingUrl("/security/do/login.html")          // 指定登录请求
                .failureHandler(new AuthenticationFailureHandler() {    // 登录失败异常处理
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        // 抛出登录异常，交给异常处理器来处理
                        httpServletRequest.setAttribute(CrowdConstant.ATTR_NAME_EXCEPTION,CrowdConstant.MESSAGE_LOGIN_FAILED);
                        httpServletRequest.getRequestDispatcher("/WEB-INF/view/admin-login.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .defaultSuccessUrl("/admin/to/main/page.html")          // 登录成功跳转页面
                .usernameParameter("loginAcct")                         // 登录参数设置
                .passwordParameter("userPswd");                         // 登录参数设置

        // 注销登录
        security.logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/security/to/login/page.html");

        /*
        * 权限不足异常处理：在配置文件中配置的请求，不经过SpringMVC的处理，所有异常处理映射不会接受到DelegatingFilterProxy发出的403异常
        *       所以，需要在SpringSecuriy中配置权限不足（403）异常处理。
        * */
        security.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                       AccessDeniedException exception) throws IOException, ServletException {
                         httpServletRequest.setAttribute(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
                         httpServletRequest.getRequestDispatcher("/WEB-INF/view/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                });
    }

}
