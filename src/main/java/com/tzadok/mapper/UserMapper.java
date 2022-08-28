package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/25 21:30:32
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
