package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorPO {

    // 发起人用户id
    private String memberId;

    // 发起人用户名
    private String username;

    // 发起人认证信息
    private Integer authstatus;

    // 简单介绍
    private String descriptionSimple;

    // 客服电话
    private String serviceNum;
}
