package com.tzadok.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzadok.common.Result;
import com.tzadok.dto.DishDto;
import com.tzadok.dto.SetmealDto;
import com.tzadok.pojo.Category;
import com.tzadok.pojo.Dish;
import com.tzadok.pojo.Setmeal;
import com.tzadok.pojo.SetmealDish;
import com.tzadok.service.CategoryService;
import com.tzadok.service.DishService;
import com.tzadok.service.SetmealDishService;
import com.tzadok.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tzadok
 * @date 2022/8/25 15:53:28
 * @project reggie_take_out
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return Result.success("套装增加成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
//    http://localhost/setmeal/page?page=1&pageSize=10
    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询,根据name进行like查询
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        //排序
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,setmealLambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Result.success(dtoPage);
    }

    /**
     * 根据id查询套餐及套餐内容
     * @param id
     */
//    http://localhost/setmeal/1415580119015145474
    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealById(@PathVariable("id") Long id){
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return Result.success(setmealDto);
    }

    /**
     * 菜品的更新
     * @param setmealDto
     * @return
     */
//    http://localhost/setmeal
    @PutMapping
    public Result<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return Result.success("菜品更新成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return Result.success("套餐数据删除成功");
    }

    /**
     * 更新套餐状态
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public Result<String> stopStatus(@RequestParam List<Long> ids){
        UpdateWrapper<Setmeal> setmealUpdateWrapper = new UpdateWrapper<>();
        setmealUpdateWrapper.lambda()
                .in(Setmeal::getId,ids)
                .set(Setmeal::getStatus,0);
        setmealService.update(setmealUpdateWrapper);
        return Result.success("状态更改成功");
    }

    @PostMapping("/status/1")
    public Result<String> startStatus(@RequestParam List<Long> ids){
        UpdateWrapper<Setmeal> setmealUpdateWrapper = new UpdateWrapper<>();
        setmealUpdateWrapper.lambda()
                .in(Setmeal::getId,ids)
                .set(Setmeal::getStatus,1);
        setmealService.update(setmealUpdateWrapper);
        return Result.success("状态更改成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
//    http://localhost/setmeal/list?categoryId=1413386191767674881&status=1
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return Result.success(setmealList);
    }


    /**
     * 移动端点击套餐图片查看套餐具体内容
     * 这里返回的是dto 对象，因为前端需要copies这个属性
     * 前端主要要展示的信息是:套餐中菜品的基本信息，图片，菜品描述，以及菜品的份数
     * @param SetmealId
     * @return
     */
    //这里前端是使用路径来传值的，要注意，不然你前端的请求都接收不到，就有点尴尬哈
    @GetMapping("/dish/{id}")
    public Result<List<DishDto>> dish(@PathVariable("id") Long SetmealId){
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,SetmealId);
        //获取套餐里面的所有菜品  这个就是SetmealDish表里面的数据
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        List<DishDto> dishDtos = list.stream().map((setmealDish) -> {
            DishDto dishDto = new DishDto();
            //其实这个BeanUtils的拷贝是浅拷贝，这里要注意一下
            BeanUtils.copyProperties(setmealDish, dishDto);
            //这里是为了把套餐中的菜品的基本信息填充到dto中，比如菜品描述，菜品图片等菜品的基本信息
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);

            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtos);
    }
}
