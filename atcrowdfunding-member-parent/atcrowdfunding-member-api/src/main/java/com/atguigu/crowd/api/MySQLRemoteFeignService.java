package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("atguigu-crowd-mysql")
public interface MySQLRemoteFeignService {

    @RequestMapping("/get/detail/project/return/remote/{projectid}")
    ResultEntity<DetailProjectVO> getDetailProjectReturnRemote(@PathVariable("projectid")Integer projectId);

    @RequestMapping("/get/portal/type/project/data/remote")
    ResultEntity<List<PortalTypeVO>> getProtalTypeProject();

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginAcct);

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberid") Integer memberId);

    @RequestMapping("/get/order/project/vo/remote/{returnid}")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("returnid") Integer returnId);

    @RequestMapping("/get/order/address/vo/remote/{memberid}")
    ResultEntity<List<AddressVO>> getAddressByMemberIdRemote(@PathVariable("memberid") Integer memberId);

    @RequestMapping("/save/address")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);
}
