package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssp.entities.Category;
import com.ssp.entities.Dish;
import com.ssp.entities.Orders;
import com.ssp.service.CategoryService;
import com.ssp.service.DishService;
import com.ssp.service.OrdersService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echart")
public class EchartController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishService dishService;

    @GetMapping("/getAll")
    @ResponseBody
    public Map getAll(){
        Map<String,Object> map = new HashMap<>();
        List<Category> categories = categoryService.list();
        String[] names = new String[categories.size()];
        Integer[] amount = new Integer[categories.size()];
        int i = 0;
        for(Category c : categories){
            names[i] = c.getName();
            int count = dishService.count(new QueryWrapper<Dish>().eq("category_id",c.getId()));
            amount[i++] = count;
        }
        map.put("names",names);
        map.put("amount",amount);

        return map;
    }

    @GetMapping("/echart")
    public void echart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/backend/page/echart/echart.html").forward(request,response);
    }
}
