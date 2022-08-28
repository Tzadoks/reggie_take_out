package com.tzadok.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzadok.common.Result;
import com.tzadok.pojo.Category;
import com.tzadok.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tzadok
 * @date 2022/8/24 16:09:35
 * @project reggie_take_out
 * @description:
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("新增成功");
    }

    /**
     * 菜品的分页查询
     * @param page
     * @param pageSize
     * @return
     */
//    http://localhost/category/page?page=1&pageSize=10
    @GetMapping("/page")
    public Result getCategoryPage(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByAsc("sort");

        categoryService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long ids){
        log.info("删除分类，id为：{}",ids);

        //categoryService.removeById(id);
        categoryService.remove(ids);

        return Result.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
//    http://localhost/category
    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);

        return Result.success("修改分类信息成功");
    }

    /**
     * 根据条件查询
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return Result.success(list);
    }
}
