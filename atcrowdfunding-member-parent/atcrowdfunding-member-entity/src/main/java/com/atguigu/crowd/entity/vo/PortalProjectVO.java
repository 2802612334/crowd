package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalProjectVO implements Serializable {
    // 项目id
    private Integer id;

    // 项目名称
    private String projectName;

    // 筹集金额
    private Long money;

    // 发起时间
    private String deploydate;

    // 筹集天数
    private Integer day;

    // 已筹集金额
    private Long supportmoney;

    // 支持人数
    private Integer supporter;

    // 完成百分比
    private Integer completion;

    // 头图路径
    private String headerPicturePath;

    // 头图http地址
    private String pictureHttpAddress;

    // 截至日期
    private String deadline;

}
