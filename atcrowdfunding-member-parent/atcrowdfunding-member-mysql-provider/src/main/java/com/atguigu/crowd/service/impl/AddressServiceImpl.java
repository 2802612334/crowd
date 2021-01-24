package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.AddressPOExample;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public List<AddressPO> getAddressByMemberId(Integer memberId) {
        AddressPOExample example = new AddressPOExample();
        AddressPOExample.Criteria criteria = example.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        return addressPOMapper.selectByExample(example);
    }
}
