package com.atguigu.crowd.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Spring测试类的编写
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-tx.xml"})
public class test {

    @Autowired
    DruidDataSource dataSource;

    @Autowired
    AdminService adminService;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RoleMapper roleMapper;

    @Test
    public void testDataSource(){
        System.out.println(dataSource);
    }

    @Test
    public void initRoleInfo(){
        for(int i = 0;i < 281;i++){
            roleMapper.insert(new Role(null,"role"+i));
        }
    }
    @Test
    public void testGetAdminById(){
//        Admin admin = adminService.getAdminById(1);
//        System.out.println(admin.toString());
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo("eva");
        List<Admin> list = adminMapper.selectByExample(adminExample);
        list.forEach(admin -> System.out.println(admin.toString()));
    }

    @Test
    public void testTransaction(){
        for(int i = 0;i < 283;i++){
            adminService.saveAdmin(new Admin(null,"tom"+i,"123123","tom"+i,"tom"+i+"@qq.com",null));
        }
    }

    @Test
    public void testLogging(){
        /*
        * 使用传统的System.out.println打印日志，使用的是IO，效率低，不利于管理。
        * */
        Logger logger = LoggerFactory.getLogger(test.class);
        logger.debug("DEBUG level!!!");
        logger.debug("DEBUG level!!!");
        logger.debug("DEBUG level!!!");

        logger.info("INFO level!!!");
        logger.info("INFO level!!!");
        logger.info("INFO level!!!");

        logger.warn("WARN level!!!");
        logger.warn("WARN level!!!");
        logger.warn("WARN level!!!");

        logger.error("ERROR level!!!");
        logger.error("ERROR level!!!");
        logger.error("ERROR level!!!");

    }

    @Test
    public void testMD5(){
        String s = CrowdUtil.md5("root");
        System.out.println(s);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("root"));
    }

}
