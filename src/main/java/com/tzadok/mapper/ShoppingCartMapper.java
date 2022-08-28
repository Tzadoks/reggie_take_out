package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/26 10:06:25
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
