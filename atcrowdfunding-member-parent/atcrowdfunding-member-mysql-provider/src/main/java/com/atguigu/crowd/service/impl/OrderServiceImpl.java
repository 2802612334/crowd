package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer returnId) {
        OrderProjectVO orderProjectVO  = orderPOMapper.selectOrderProjectVOByReturnId(returnId);
        return orderProjectVO;
    }
}
