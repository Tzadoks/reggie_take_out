package com.tzadok.dto;


import com.tzadok.pojo.Setmeal;
import com.tzadok.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
