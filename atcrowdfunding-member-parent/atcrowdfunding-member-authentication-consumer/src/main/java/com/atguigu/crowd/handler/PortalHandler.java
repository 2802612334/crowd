package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteFeignService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteFeignService mySQLRemoteFeignService;

    @RequestMapping("/")
    public String showPortalPage(Model model){
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteFeignService.getProtalTypeProject();
        if(resultEntity.getResult() == ResultEntity.getFAILED()){
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_PORTAL_INIT_ERROR);
        }else {
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,resultEntity.getData());
        }
        return "index";
    }
}
