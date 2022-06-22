package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssp.common.BaseContext;
import com.ssp.common.R;
import com.ssp.entities.Category;
import com.ssp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R page(@RequestParam("page")int page,@RequestParam("pageSize") int pageSize){
        Page<Category> categoryPage = new Page<>(page,pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        categoryService.page(categoryPage,queryWrapper);
        return R.success(categoryPage);
    }

    @PostMapping
    public R<String> save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        if(save){
            return R.success("保存成功");
        }
        return R.error("保存失败");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){

        boolean update = categoryService.update(category, new QueryWrapper<Category>().eq("id",category.getId()));
        if(update){
            return R.success("更新成功");
        }
        return R.error("更新失败");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id){
        boolean id1 = categoryService.remove(new QueryWrapper<Category>().eq("id", id));
        if(id1){
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    @GetMapping("/list")
    public R list(Integer type){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if(type != null){
            queryWrapper.eq("type",type);
        }
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
