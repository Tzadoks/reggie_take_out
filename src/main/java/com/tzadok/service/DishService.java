package com.tzadok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzadok.dto.DishDto;
import com.tzadok.pojo.Dish;

import java.util.List;

/**
 * @author Tzadok
 * @date 2022/8/24 16:37:16
 * @project reggie_take_out
 * @description:
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteDish(List<Long> ids);
}
