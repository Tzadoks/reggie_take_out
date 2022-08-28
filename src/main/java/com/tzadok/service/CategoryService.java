package com.tzadok.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tzadok.pojo.Category;

/**
 * @author Tzadok
 * @date 2022/8/24 16:10:56
 * @project reggie_take_out
 * @description:
 */
public interface CategoryService extends IService<Category> {

    void remove(Long id);

}
