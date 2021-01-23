package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {

    // 回报id
    private Integer returnId;

    // 支持金额
    private Integer supportmoney;

    // 回报内容
    private String content;

    // 回报产品限额   0为不限额
    private Integer count;

    // 是否设置单笔限额
    private Integer signalpurchase;

    // 具体单笔限购数量
    private Integer purchase;

    // 运费
    private Integer freight;

    // 项目结束多少天发送回报
    private Integer returndate;

    // 支持人数
    private Integer supporter;
}
