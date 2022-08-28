package com.tzadok.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.mapper.SetmealDishMapper;
import com.tzadok.pojo.SetmealDish;
import com.tzadok.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/25 15:51:56
 * @project reggie_take_out
 * @description:
 */
@Slf4j
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;
}
