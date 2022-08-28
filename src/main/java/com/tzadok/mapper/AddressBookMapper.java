package com.tzadok.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tzadok.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tzadok
 * @date 2022/8/26 09:53:15
 * @project reggie_take_out
 * @description:
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
