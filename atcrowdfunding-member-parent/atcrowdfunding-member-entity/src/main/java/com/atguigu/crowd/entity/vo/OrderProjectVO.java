package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProjectVO implements Serializable {

    // 工程名
    private String projectName;

    // 发起人信息
    private String launchName;

    // 回报信息
    private String returnContent;

    // 是否限购
    private Integer signalpurchase;

    // 限购次数
    private Integer purchase;

    // 单笔金额
    private Double supportPrice;

    // 邮费
    private Double freight;

    // 实际回报数量
    private Integer returnNum;
}
