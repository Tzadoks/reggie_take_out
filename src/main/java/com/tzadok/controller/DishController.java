package com.tzadok.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzadok.common.Result;
import com.tzadok.dto.DishDto;
import com.tzadok.pojo.Category;
import com.tzadok.pojo.Dish;
import com.tzadok.pojo.DishFlavor;
import com.tzadok.service.CategoryService;
import com.tzadok.service.DishFlavorService;
import com.tzadok.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Tzadok
 * @date 2022/8/24 16:36:46
 * @project reggie_take_out
 * @description: 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
//    http://localhost/dish/page?page=1&pageSize=10
    @GetMapping("page")
    public Result getDishPage(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类ID

            //根据ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

        //清理所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");

        //清理某个分类下面的菜品数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";

        redisTemplate.delete(key);

        return Result.success("新增菜品成功");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getDishById(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品内容
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

        //清理所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");

        //清理某个分类下面的菜品数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";

        redisTemplate.delete(key);

        return Result.success("修改成功");
    }

    /**
     * 逻辑删除菜品
     * @param ids
     * @return
     */
//    http://localhost/dish?ids=1562636893433131010
    @DeleteMapping
    public Result<String> deleteDish(@RequestParam List<Long> ids){
//        log.info(ids);
        dishService.deleteDish(ids);
        return Result.success("删除成功");
    }

    /**
     * 更新菜品状态
     * @param ids
     * @return
     */
//    http://localhost/dish/status/0?ids=1562636893433131010,1413384757047271425
    @PostMapping("/status/0")
    public Result<String> stopStatus(@RequestParam List<Long> ids){
        UpdateWrapper<Dish> dishUpdateWrapper = new UpdateWrapper<>();
        dishUpdateWrapper.lambda()
                .in(Dish::getId,ids)
                .set(Dish::getStatus,0);
        dishService.update(dishUpdateWrapper);
        return Result.success("状态更改成功");
    }

    @PostMapping("/status/1")
    public Result<String> startStatus(@RequestParam List<Long> ids){
        UpdateWrapper<Dish> dishUpdateWrapper = new UpdateWrapper<>();
        dishUpdateWrapper.lambda()
                .in(Dish::getId,ids)
                .set(Dish::getStatus,1);
        dishService.update(dishUpdateWrapper);
        return Result.success("状态更改成功");
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
//    http://localhost/dish/list?categoryId=1397844263642378242
/*    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //起售状态的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        if (dish.getName() != null){
            dishLambdaQueryWrapper.like(Dish::getName,dish.getName());
        }
        //添加排序条件
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        return Result.success(list);
    }*/

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList = null;

        //动态构造redis中的ID
        String key = "dish_" + dish.getCategoryId() + "_" +dish.getStatus();

        //先从redis中获取缓存数据
        dishDtoList= (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (dishDtoList != null){
            //如果存在，直接返回，无需查询数据库
            return Result.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //起售状态的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        if (dish.getName() != null){
            dishLambdaQueryWrapper.like(Dish::getName,dish.getName());
        }
        //添加排序条件
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);

        dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            //根据ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return Result.success(dishDtoList);
    }
}
