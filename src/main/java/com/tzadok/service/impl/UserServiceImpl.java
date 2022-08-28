package com.tzadok.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.mapper.UserMapper;
import com.tzadok.pojo.User;
import com.tzadok.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/25 21:31:31
 * @project reggie_take_out
 * @description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
