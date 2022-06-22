package com.ssp.dto;


import com.ssp.entities.Setmeal;
import com.ssp.entities.SetmealDish;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
