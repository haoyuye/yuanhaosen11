package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssp.common.BaseContext;
import com.ssp.common.R;
import com.ssp.dto.OrdersDTO;
import com.ssp.entities.*;
import com.ssp.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;

    @Transactional
    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders){
        Long userId = BaseContext.getCurrentId();
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());

        AddressBook addressBook = addressBookService.getOne(new QueryWrapper<AddressBook>().eq("id", orders.getAddressBookId()));
        String provinceName = addressBook.getProvinceName();
        String cityName = addressBook.getCityName();
        String districtName = addressBook.getDistrictName();
        String detail = addressBook.getDetail();
        StringBuilder address = new StringBuilder();
        if(StringUtils.hasLength(provinceName)){
            address.append(provinceName);
        }
        if(StringUtils.hasLength(cityName)){
            address.append(cityName);
        }
        if(StringUtils.hasLength(districtName)){
            address.append(districtName);
        }
        if(StringUtils.hasLength(detail)){
            address.append(detail);
        }
        orders.setAddress(address.toString());

        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setConsignee(user.getName());

        long orderId = IdWorker.getId();
        orders.setNumber(String.valueOf(orderId));
        //计算价格
        List<ShoppingCart> list = shoppingCartService.list(new QueryWrapper<ShoppingCart>().eq("user_id", userId));
        BigDecimal decimal = new BigDecimal(0);
        for (ShoppingCart shoppingCart : list) {
            Integer number = shoppingCart.getNumber();
            BigDecimal amount = shoppingCart.getAmount();
            decimal = decimal.add(amount.multiply(new BigDecimal(number)));
        }
        orders.setAmount(decimal);
        ordersService.save(orders);
        List<OrderDetail> collect = list.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(item.getAmount());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setOrderId(orders.getId());
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            orderDetail.setSetmealId(item.getSetmealId());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(collect);

        shoppingCartService.remove(new QueryWrapper<ShoppingCart>().eq("user_id",userId));
        return R.success(orders);
    }

    @GetMapping("/userPage")
    public R userPage(Integer page, Integer pageSize){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        ordersService.page(ordersPage,new QueryWrapper<Orders>().eq("user_id",BaseContext.getCurrentId()).orderByDesc("order_time"));
        List<Orders> records = ordersPage.getRecords();
        List<OrdersDTO> order_id1 = records.stream().map(item -> {
            OrdersDTO ordersDTO = new OrdersDTO();
            BeanUtils.copyProperties(item, ordersDTO);
            List<OrderDetail> order_id = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("order_id", item.getId()));
            ordersDTO.setSumNum(order_id.size());
            return ordersDTO;
        }).collect(Collectors.toList());
        Page<OrdersDTO> ordersDTOPage = new Page<>();
        BeanUtils.copyProperties(ordersPage,ordersDTOPage);
        ordersDTOPage.setRecords(order_id1);
        return R.success(ordersDTOPage);
    }

    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,
                      @RequestParam("pageSize") Integer pageSize,
                      @RequestParam(value = "number",required = false) String number,
                      @RequestParam(value = "beginTime",required = false) String beginTime,
                      @RequestParam(value = "endTime",required = false) String endTime){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasLength(number)){
            queryWrapper.eq("number",number);
        }
        if(beginTime != null){
            LocalDateTime dateTime = LocalDateTime.parse(beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.ge("order_time",dateTime);
        }
        if(endTime != null){
            LocalDateTime dateTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.le("order_time",dateTime);
        }
        queryWrapper.eq("user_id",BaseContext.getCurrentId());
        ordersService.page(ordersPage,queryWrapper);
        return R.success(ordersPage);
    }
}
