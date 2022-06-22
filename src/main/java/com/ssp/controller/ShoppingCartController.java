package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssp.common.BaseContext;
import com.ssp.common.R;
import com.ssp.entities.Orders;
import com.ssp.entities.ShoppingCart;
import com.ssp.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R list(){
        List<ShoppingCart> list = shoppingCartService.list();
        return R.success(list);
    }

    @PostMapping("/add")
    public R add(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<ShoppingCart>().eq("user_id", userId);
        if(dishId != null){
            queryWrapper.eq("dish_id",dishId);
        }else{
            queryWrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if(one == null){
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }else{
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }

    @DeleteMapping("/clean")
    public R clean(){
        shoppingCartService.remove(new QueryWrapper<ShoppingCart>().eq("user_id",BaseContext.getCurrentId()));
        return R.success("删除成功");
    }
}
