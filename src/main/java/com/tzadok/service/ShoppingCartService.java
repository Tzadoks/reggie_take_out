package com.tzadok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzadok.pojo.ShoppingCart;

/**
 * @author Tzadok
 * @date 2022/8/26 10:07:14
 * @project reggie_take_out
 * @description:
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    void clean();
}
