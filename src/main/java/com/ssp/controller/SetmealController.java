package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssp.common.R;
import com.ssp.dto.DishDto;
import com.ssp.dto.SetmealDishDto;
import com.ssp.dto.SetmealDto;
import com.ssp.entities.Dish;
import com.ssp.entities.DishFlavor;
import com.ssp.entities.Setmeal;
import com.ssp.entities.SetmealDish;
import com.ssp.service.DishFlavorService;
import com.ssp.service.DishService;
import com.ssp.service.SetmealDishService;
import com.ssp.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    DishService dishService;

    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "name",required = false) String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        if(StringUtils.hasLength(name)){
            setmealQueryWrapper.like("name",name);
        }
        setmealService.page(setmealPage,setmealQueryWrapper);
        return R.success(setmealPage);
    }

    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        System.out.println(setmealDto);
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(collect);
        return R.success("新增成功");
    }

    @PostMapping("/status/{status}")
    public R updateStatus(@PathVariable("status") Integer status,@RequestParam("ids") Long[] ids){
        List<Setmeal> setmeals = setmealService.listByIds(Arrays.asList(ids));
        List<Setmeal> collect = setmeals.stream().map(item -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(collect);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> update(@PathVariable("id") Long id){
        Setmeal setmeal = setmealService.getById(id);
        List<SetmealDish> setmeal_id = setmealDishService.list(new QueryWrapper<SetmealDish>().eq("setmeal_id", id));
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmeal_id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R update(@RequestBody SetmealDto setmealDto){
        setmealService.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.remove(new QueryWrapper<SetmealDish>().eq("setmeal_id",setmealDto.getId()));
        setmealDishService.saveBatch(collect);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R delete(@RequestParam("ids") Long[] ids){
        setmealService.removeByIds(Arrays.asList(ids));
        for(Long id : ids){
            setmealDishService.remove(new QueryWrapper<SetmealDish>().eq("setmeal_id",id));
        }
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R list(Long categoryId,Integer status){
        List<Setmeal> list = setmealService.list(new QueryWrapper<Setmeal>().eq("category_id", categoryId).eq("status", status));
        return R.success(list);
    }


    @GetMapping("/dish/{id}")
    public R dish(@PathVariable("id")Long id){
        List<SetmealDish> setmeal_id = setmealDishService.list(new QueryWrapper<SetmealDish>().eq("setmeal_id", id));
        List<SetmealDishDto> id2 = setmeal_id.stream().map(item -> {
            SetmealDishDto setmealDishDto = new SetmealDishDto();
            Dish id1 = dishService.getOne(new QueryWrapper<Dish>().eq("id", item.getDishId()));
            BeanUtils.copyProperties(item, setmealDishDto);
            setmealDishDto.setImage(id1.getImage());
            return setmealDishDto;
        }).collect(Collectors.toList());
        return R.success(id2);
    }
}
