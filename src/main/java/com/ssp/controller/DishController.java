package com.ssp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssp.common.R;
import com.ssp.dto.DishDto;
import com.ssp.entities.Category;
import com.ssp.entities.Dish;
import com.ssp.entities.DishFlavor;
import com.ssp.service.CategoryService;
import com.ssp.service.DishFlavorService;
import com.ssp.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishFlavorService dishFlavorService;

    @GetMapping("/list")
    public R list(Long categoryId){
        List<Dish> dishList = dishService.list(new QueryWrapper<Dish>().eq("category_id", categoryId).eq("status",1));
        List<DishDto> dish_id1 = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            List<DishFlavor> dish_id = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", item.getId()));
            dishDto.setFlavors(dish_id);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dish_id1);
    }

    @GetMapping("/page")
    public R page(int page,int pageSize,String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasLength(name)){
            queryWrapper.like("name",name);
        }
        dishService.page(dishPage,queryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getOne(new QueryWrapper<Category>().eq("id", categoryId));
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody  DishDto dishDto){
        boolean save = dishService.save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> collect = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);
        return R.success("保存成功");
    }

    @PostMapping("/status/{status}")
    public R batchUpdate(@PathVariable("status") Integer status,@RequestParam("ids") Long[] ids){
        List<Dish> dishes = dishService.listByIds(Arrays.asList(ids));
        List<Dish> collect = dishes.stream().map(item -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(collect);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> query(@PathVariable("id") Long id){
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getOne(new QueryWrapper<Dish>().eq("id", id));
        BeanUtils.copyProperties(dish,dishDto);
        List<DishFlavor> dish_id = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", id));
        dishDto.setFlavors(dish_id);
        return R.success(dishDto);
    }

    @DeleteMapping
    public R delete(@RequestParam("ids") Long[] ids){
        List<Dish> dishes = dishService.listByIds(Arrays.asList(ids));
        List<Long> collect = dishes.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        dishService.removeByIds(Arrays.asList(ids));
        dishFlavorService.removeByIds(collect);
        return R.success("删除成功");
    }

    @PutMapping
    public R update(@RequestBody DishDto dishDto){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        List<DishFlavor> flavors = dishDto.getFlavors();
        dishService.update(dish, new UpdateWrapper<Dish>().eq("id",dish.getId()));
        dishFlavorService.remove(new QueryWrapper<DishFlavor>().eq("dish_id",dish.getId()));
        List<DishFlavor> collect = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(collect);
        return R.success("修改成功");
    }
}
