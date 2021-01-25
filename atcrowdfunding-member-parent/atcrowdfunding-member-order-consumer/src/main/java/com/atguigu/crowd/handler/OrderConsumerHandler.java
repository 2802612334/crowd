package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteFeignService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderConsumerHandler {

    @Autowired
    private MySQLRemoteFeignService mySQLRemoteFeignService;

    @RequestMapping("/confirm/return/info/{returnid}")
    public String getReturnInfo(@PathVariable("returnid") Integer returnId, HttpSession session){
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteFeignService.getOrderProjectVORemote(returnId);
        if(resultEntity.getResult().equals(ResultEntity.getFAILED())){
            log.info("获取回报信息失败，失败原因：" + resultEntity.getMessage());
        }
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT,resultEntity.getData());
        return "redirect:/order/pay/step/1.html";
    }

    @RequestMapping("/confirm/return/address")
    public String getReturnAddressInfo(Integer returnNum,HttpSession session){
        // 1.将回报数量添加到session对象中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        orderProjectVO.setReturnNum(returnNum);
        // 2.由于使用了SpingSession，所以需要再次放入session作用域，刷新redis数据库中的信息
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT,orderProjectVO);
        // 3.获取当前登录用户的id，查询用户的地址信息
        MemberLoginVO memberLoginVO = (MemberLoginVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteFeignService.getAddressByMemberIdRemote(memberLoginVO.getId());
        // 4.对查询结果进行判断
        if(resultEntity.getResult().equals(ResultEntity.getFAILED())){
            log.info("获取回报地址信息失败，失败原因：" + resultEntity.getMessage());
        }
        session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_MEMBER,resultEntity.getData());
        return "redirect:/order/pay/step/2.html";
    }

    @RequestMapping("/confirm/save/address")
    public String saveAddress(AddressVO addressVO,HttpSession session){
        // 1.将当前用户登录的id放入AddressVO
        MemberLoginVO memberLoginVO = (MemberLoginVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        addressVO.setMemberId(memberLoginVO.getId());
        // 2.添加地址信息
        ResultEntity<String> resultEntity = mySQLRemoteFeignService.saveAddressRemote(addressVO);
        if(resultEntity.getResult().equals(ResultEntity.getFAILED())){
            log.info("插入地址信息失败：" + resultEntity.getData());
        }
        // 3.重新查询该用户地址信息，将新加入的地址信息添加到Session域中
        ResultEntity<List<AddressVO>> addressResultEntity = mySQLRemoteFeignService.getAddressByMemberIdRemote(memberLoginVO.getId());
        if(addressResultEntity.getResult().equals(ResultEntity.getFAILED())){
            log.info("获取回报地址信息失败，失败原因：" + addressResultEntity.getMessage());
        }
        session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESS_MEMBER,addressResultEntity.getData());
        return "redirect:/order/pay/step/2.html";
    }
}
