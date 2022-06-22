package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssp.common.BaseContext;
import com.ssp.common.R;
import com.ssp.dto.UserDto;
import com.ssp.entities.User;
import com.ssp.service.UserService;
import com.ssp.utils.SendMessage;
import com.ssp.utils.ValidateCodeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user, HttpServletRequest request){
//        System.out.println(user.getPhone());
//        SendMessage sendMessage = new SendMessage();
//        String code = ValidateCodeUtils.generateValidateCode4String(4);
        String code = "010318";
        request.getSession().setAttribute("code",code);
        User one = userService.getOne(new QueryWrapper<User>().eq("phone", user.getPhone()));
        BaseContext.setCurrentId(one.getId());

//        sendMessage.sendMessage(user.getPhone(),code);
        return R.success("验证码发送成功");
    }

    @PostMapping("/login")
    public R login(@RequestBody UserDto userDto, HttpServletRequest request){
        String code = userDto.getCode();
        if(code == null || request.getSession().getAttribute("code") == null || !code.equals(request.getSession().getAttribute("code").toString())){
            return R.error("验证码错误");
        }
        request.getSession().removeAttribute("code");
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", userDto.getPhone()));
        if(user == null){
            userService.save(userDto);
            request.getSession().setAttribute("user",userDto.getId());
        }else {
            request.getSession().setAttribute("user", user.getId());
        }
        return R.success("登录成功");
    }

    @PostMapping("/loginout")
    public R loginOut(){
        ThreadLocal<Long> longThreadLocal = new ThreadLocal<>();
        longThreadLocal.remove();
        return R.success("退出成功");
    }
}
