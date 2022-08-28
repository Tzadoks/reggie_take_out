package com.tzadok.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.mapper.DishFlavorMapper;
import com.tzadok.pojo.DishFlavor;
import com.tzadok.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/25 10:19:17
 * @project reggie_take_out
 * @description:
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
}
