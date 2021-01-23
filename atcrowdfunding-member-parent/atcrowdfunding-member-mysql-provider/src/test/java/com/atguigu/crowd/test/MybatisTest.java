package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import com.atguigu.crowd.service.MemberService;
import com.netflix.discovery.converters.Auto;
import net.minidev.json.JSONUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.ls.LSOutput;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Test
    public void testMapper(){
        System.out.println(dataSource);
    }

    @Test
    public void testGetProtalTypeAndProtalProject() throws ParseException {
        List<PortalTypeVO> protalTypeAndProtalProject = memberService.getProtalTypeAndProtalProject();
        protalTypeAndProtalProject.forEach(type -> {
            System.out.println(type);
        });
    }

    @Test
    public void test(){
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectReturn(188);
        System.out.println(detailProjectVO);
    }

    @Test
    public void insetData(){
        ProjectPO projectPO = new ProjectPO();
        projectPO.setMemberid(2);
        projectPO.setProjectName("尚筹网");
        projectPO.setProjectDescription("让天下没有难做的生意！");
        projectPO.setMoney(500000L);
        projectPO.setDay(50);
        projectPO.setStatus(1);
        projectPO.setDeploydate("2021/1/20");
        projectPO.setSupportmoney(100000L);
        projectPO.setSupporter(300);
        double a = (double)projectPO.getSupportmoney() / projectPO.getMoney();
        projectPO.setCompletion((int)(a * 100));
        projectPO.setCreatedate("2021/1/14");
        projectPO.setFollower(53);
        projectPO.setHeaderPicturePath("D:\\JavaIDE\\code\\Java\\atcrowdfunding-member-parent\\atcrowdfunding-member-authentication-consumer\\target\\classes\\static\\crowd-project-picture\\headerpicture\\2021-01-20\\b32dd902dfdc4476bd1749162bcce6d5.png");

        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0;i < 30;i++){
            if(i != 0){
                projectPO.setId(1 + projectPO.getId());
            }
            projectPOMapper.insert(projectPO);
            String sql = "insert into t_project_type(projectid,typeid) values(" + projectPO.getId() + ","+(i + 1)%5+")";

            try {
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
