package com.tzadok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzadok.pojo.Orders;

/**
 * @author Tzadok
 * @date 2022/8/26 11:20:16
 * @project reggie_take_out
 * @description:
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);

}
