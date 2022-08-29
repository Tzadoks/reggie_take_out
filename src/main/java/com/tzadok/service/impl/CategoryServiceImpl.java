package com.tzadok.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzadok.common.CustomException;
import com.tzadok.mapper.CategoryMapper;
import com.tzadok.pojo.Category;
import com.tzadok.pojo.Dish;
import com.tzadok.pojo.Setmeal;
import com.tzadok.service.CategoryService;
import com.tzadok.service.DishService;
import com.tzadok.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tzadok
 * @date 2022/8/24 16:11:25
 * @project reggie_take_out
 * @description:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前你需要判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前那分类是否管理了菜品，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);

        int dishCount = (int) dishService.count(dishLambdaQueryWrapper);

        if (dishCount > 0){
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }
        //查询当前那分类是否管理了套餐，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int setmealCount = (int) setmealService.count(setmealLambdaQueryWrapper);

        if (setmealCount > 0){
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
