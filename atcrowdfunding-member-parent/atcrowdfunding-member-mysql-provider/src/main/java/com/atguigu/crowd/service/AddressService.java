package com.atguigu.crowd.service;

import com.atguigu.crowd.entity.po.AddressPO;

import java.util.List;

public interface AddressService {
    List<AddressPO> getAddressByMemberId(Integer memberId);
}
