package com.tzadok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzadok.dto.SetmealDto;
import com.tzadok.pojo.Setmeal;

import java.util.List;

/**
 * @author Tzadok
 * @date 2022/8/24 16:40:37
 * @project reggie_take_out
 * @description:
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐并保存套餐和菜品的关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
