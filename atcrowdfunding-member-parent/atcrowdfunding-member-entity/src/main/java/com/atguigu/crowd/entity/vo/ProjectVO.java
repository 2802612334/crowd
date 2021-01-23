package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 分类信息
    private List<Integer> typeIdList = new ArrayList<>();

    // 标签组
    private List<Integer> tagIdList = new ArrayList<>();

    // 项目名称
    private String projectName;

    // 项目简介
    private String projectDescription;

    // 筹集金额
    private Long money;

    // 筹集天数
    private Integer day;

    // 项目创建日期
    private String createdate;

    // 头图的路径
    private String headerPicturePath;

    // 详情图片的路径
    private List<String> detailPicturePathList = new ArrayList<>();

    // 发起人信息
    private MemberLaunchInfoVO memberLauchInfoVO;

    // 回报信息
    private List<ReturnVO> returnVOList = new ArrayList<>();

    // 确认信息
    private MemberConfirmInfoVO memberConfirmInfoVO;
}
