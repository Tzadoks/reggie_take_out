package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/24 16:38:17
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
