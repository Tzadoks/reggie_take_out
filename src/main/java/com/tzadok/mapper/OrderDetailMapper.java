package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/26 11:22:30
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
