package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/23 16:19:19
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
