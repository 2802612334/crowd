package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.*;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.ProjectService;
import com.atguigu.crowd.util.CrowdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    @Value("${picture.httpaddress.prefix}")
    private String pictureHttpAddressPrefix;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPO;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private SimpleDateFormat simpleDateFormat;


    @Override
    @Transactional(readOnly = false,propagation = Propagation.NESTED,rollbackFor = Exception.class)
    public void saveProjectVORemote(ProjectVO projectVO,Integer memberId) {
        // 1.先将ProjectVO中与ProjectPO中相同的属性进行存储
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO,projectPO);
        // 设置项目状态和发起人
        projectPO.setStatus(0);
        projectPO.setMemberid(memberId);
        projectPOMapper.insertSelective(projectPO);
        // 取出项目Id
        Integer projectId = projectPO.getId();

        // 2.存储项目类型
        List<Integer> typeIdList = projectVO.getTypeIdList();
        if(typeIdList.size() != 0){
            projectPOMapper.insertTypeRelationship(projectId,typeIdList);
        }

        // 3.存储项目标签
        List<Integer> tagIdList = projectVO.getTagIdList();
        if(tagIdList.size() != 0){
            projectPOMapper.insertTagRelationship(projectId,tagIdList);
        }

        // 4.存储详情图片路径
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        if(detailPicturePathList.size() != 0){
            projectItemPicPOMapper.insertBatchItemPictureByProjectId(projectId,detailPicturePathList);
        }

        // 5.存储介绍信息
        MemberLaunchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        memberLaunchInfoPO.setMemberid(memberId);
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPOMapper.insertSelective(memberLaunchInfoPO);

        // 6.存储回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        if(returnVOList.size() != 0){
            ReturnPO returnPO = new ReturnPO();
            returnPO.setProjectid(projectId);
            for (ReturnVO returnVO : returnVOList) {
                BeanUtils.copyProperties(returnVO,returnPO);
                returnPOMapper.insertSelective(returnPO);
            }
        }

        // 7.存储MemberConfirmInfoVO
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insertSelective(memberConfirmInfoPO);
    }

    @Override
    public DetailProjectVO getDetailProjectReturn(Integer projectId) throws Exception {
        String pictrueHttpAddress = "";
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectReturn(projectId);
        // 1.算出项目剩余天数
        Date start = simpleDateFormat.parse(detailProjectVO.getCreatedate());
        Date end = new Date(start.getTime() + (detailProjectVO.getDay() * 24 * 60 * 60 * 1000L));
        long daysRemaining = (end.getTime() - new Date().getTime()) / (24 * 60 * 60 * 1000L);
        detailProjectVO.setDaysRemaining((int) daysRemaining);
        // 2.转换头图路径
        pictrueHttpAddress = CrowdUtil.getPictrueHttpAdress(pictureHttpAddressPrefix, detailProjectVO.getHeaderPicturePath());
        detailProjectVO.setHeadPictureHttpAddress(pictrueHttpAddress);
        // 3.获取详情图片
        ProjectItemPicPOExample example = new ProjectItemPicPOExample();
        ProjectItemPicPOExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        List<ProjectItemPicPO> projectItemPicPOS = projectItemPicPOMapper.selectByExample(example);
        List<String> httpDetailPicturePathList = new ArrayList<>();
        for (ProjectItemPicPO itemPicPO : projectItemPicPOS) {
            // 4.转换详情图片路径为Http路径
            pictrueHttpAddress = CrowdUtil.getPictrueHttpAdress(pictureHttpAddressPrefix,itemPicPO.getItemPicPath());
            httpDetailPicturePathList.add(pictrueHttpAddress);
        }
        detailProjectVO.setHttpDetailPicturePathList(httpDetailPicturePathList);
        // 5.设置项目状态信息显示文字
        switch (detailProjectVO.getStatus()){
            case 0:
                detailProjectVO.setStatusText("即将开始");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败");
                break;
        }
        return detailProjectVO;
    }
}
