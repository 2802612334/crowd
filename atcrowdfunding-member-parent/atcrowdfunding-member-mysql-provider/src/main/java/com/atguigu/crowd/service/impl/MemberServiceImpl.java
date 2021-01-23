package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.entity.po.ProjectItemPicPO;
import com.atguigu.crowd.entity.vo.PortalProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import com.atguigu.crowd.mapper.TagPOMapper;
import com.atguigu.crowd.mapper.TypePOMapper;
import com.atguigu.crowd.service.MemberService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    @Value("${picture.httpaddress.prefix}")
    private String pictureHttpAddressPrefix;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    public MemberPO getMemberPOByLoginAcctRemote(String loginAcct) {
        // 1.封装查询条件
        MemberPOExample memberPOExample = new MemberPOExample();
        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        // 2.执行查询
        List<MemberPO> memberPOs = memberPOMapper.selectByExample(memberPOExample);
        // 3.返回结果
        return memberPOs.get(0);
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.NESTED,rollbackFor = Exception.class)
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }

    @Override
    public List<PortalTypeVO> getProtalTypeAndProtalProject() throws ParseException {
        List<PortalTypeVO> protalTypeProject = memberPOMapper.selectProtalTypeAndProtalProject();
        String pictrueHttpAddress = "";
        for (PortalTypeVO portalTypeVO : protalTypeProject) {
            for (PortalProjectVO portalProjectVO : portalTypeVO.getPortalProjectVOList()) {
                // 1.添加头图地址的http访问地址
                pictrueHttpAddress = CrowdUtil.getPictrueHttpAdress(pictureHttpAddressPrefix, portalProjectVO.getHeaderPicturePath());
                portalProjectVO.setPictureHttpAddress(pictrueHttpAddress);
                // 2.设置截至日期
                Date start = simpleDateFormat.parse(portalProjectVO.getDeploydate());
                // 处理整形数字切记用long!long!long!!!处理数字过大，会算错
                long time = start.getTime() + (portalProjectVO.getDay() * 24 * 60 * 60 * 1000L);
                Date end = new Date(time);
                String endStr = simpleDateFormat.format(end);
                portalProjectVO.setDeadline(endStr);
            }
        }
        return protalTypeProject;
    }

}
