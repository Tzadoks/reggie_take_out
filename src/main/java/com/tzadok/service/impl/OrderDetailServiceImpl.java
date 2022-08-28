package com.tzadok.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.mapper.OrderDetailMapper;
import com.tzadok.pojo.OrderDetail;
import com.tzadok.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/26 11:23:29
 * @project reggie_take_out
 * @description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
