package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalTypeVO implements Serializable {

    // 分类Id
    private Integer id;

    // 分类名
    private String name;

    // 分类介绍
    private String remark;

    // 子项目
    private List<PortalProjectVO> portalProjectVOList;
}
