package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO implements Serializable {

    private Integer addressId;

    private String receiveName;

    private String phoneNum;

    private String address;
}
