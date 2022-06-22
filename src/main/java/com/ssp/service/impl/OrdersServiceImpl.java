package com.ssp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssp.entities.Orders;
import com.ssp.service.OrdersService;
import com.ssp.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




