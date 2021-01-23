package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {

    // 项目id
    private Integer projectId;

    // 项目名称
    private String projectName;

    // 项目描述
    private String projectDescription;

    // 关注人数
    private Integer follower;

    // 项目状态
    private Integer status;

    // 已筹资金
    private Long supportmoney;

    // 目标资金
    private Long money;

    // 达成百分比
    private Integer completion;

    // 项目开始时间
    private String createdate;

    // 筹集天数
    private Integer day;

    // 剩余天数
    private Integer daysRemaining;

    // 支持人数
    private Integer supporter;

    // 头图路径
    private String headerPicturePath;

    // 头图Http地址
    private String headPictureHttpAddress;

    // 详情图路径
    private List<String> httpDetailPicturePathList;

    // 发起人信息(发起人用户名，简单介绍，发起人认证信息，客服电话)
    private SponsorPO sponsorPO;

    // 回报信息
    private List<DetailReturnVO> detailReturnVOS;
}
