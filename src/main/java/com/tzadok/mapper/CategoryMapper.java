package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/24 16:10:36
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
