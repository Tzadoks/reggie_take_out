package com.tzadok.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.common.BaseContext;
import com.tzadok.mapper.ShoppingCartMapper;
import com.tzadok.pojo.ShoppingCart;
import com.tzadok.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/26 10:07:43
 * @project reggie_take_out
 * @description:
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Override
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
////        shoppingCartService.remove(queryWrapper);
//        shoppingCartMapper.deleteById(queryWrapper);
        shoppingCartMapper.delete(queryWrapper);

    }
}
