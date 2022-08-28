package com.tzadok.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.common.BaseContext;
import com.tzadok.mapper.AddressBookMapper;
import com.tzadok.pojo.AddressBook;
import com.tzadok.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/26 09:54:21
 * @project reggie_take_out
 * @description:
 */
@Slf4j
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
