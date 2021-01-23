package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.service.MemberService;
import com.atguigu.crowd.service.ProjectService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class MySQLRemoteHandler {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getProtalTypeProject() {
        List<PortalTypeVO> protalTypeProject = null;
        try{
            protalTypeProject = memberService.getProtalTypeAndProtalProject();
            return ResultEntity.successWithData(protalTypeProject);
        }catch (Exception e){
            if(e instanceof ParseException){
                log.info("时间格式转换错误");
            }
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginAcct){
        try{
            MemberPO memberPO = memberService.getMemberPOByLoginAcctRemote(loginAcct);
            return ResultEntity.successWithData(memberPO);
        }catch (Exception e){
            log.info("------------->没有获取到该用户" + loginAcct + "信息");
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithOutData();
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                return ResultEntity.failed(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/project/vo/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberid") Integer memberId){
        try {
            projectService.saveProjectVORemote(projectVO,memberId);
            return ResultEntity.successWithOutData();
        }catch (Exception e){
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/detail/project/return/remote/{projectid}")
    public ResultEntity<DetailProjectVO> getDetailProjectReturnRemote(@PathVariable("projectid")Integer projectId){
        DetailProjectVO detailProjectVO = null;
        try {
            detailProjectVO = projectService.getDetailProjectReturn(projectId);
        } catch (Exception e) {
            if(e instanceof ParseException){
                log.info("时间格式转换错误");
            }
            return ResultEntity.failed(e.getMessage());
        }
        return ResultEntity.successWithData(detailProjectVO);
    }
}
