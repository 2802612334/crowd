package com.atguigu.crowd.service;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.util.ResultEntity;

import java.text.ParseException;
import java.util.List;

public interface MemberService {

    MemberPO getMemberPOByLoginAcctRemote(String loginAcct);

    void saveMember(MemberPO memberPO);

    List<PortalTypeVO> getProtalTypeAndProtalProject() throws ParseException;
}
