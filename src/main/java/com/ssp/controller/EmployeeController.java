package com.ssp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssp.common.R;
import com.ssp.entities.Employee;
import com.ssp.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String verifycode = (String) request.getSession().getAttribute("verifycode");
        if(employee.getCode() == null || !employee.getCode().equals(verifycode)){
            return R.error("验证码不正确");
        }
        return employeeService.login(request,employee);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }

    @GetMapping("/page")
    public R<Page<Employee>> list(@RequestParam("page")Integer page,
                                  @RequestParam("pageSize")Integer pageSize,
                                  @RequestParam(value = "name",required = false) String name){
        return employeeService.queryListByPage(page,pageSize,name);
    }

    @PostMapping
    public R save(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);
        employeeService.save(employee);
        return R.success("添加成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> queryById(@PathVariable("id") Long id){
        Employee one = employeeService.getOne(new QueryWrapper<Employee>().eq("id", id));
        return R.success(one);
    }

    @PutMapping
    public R update(@RequestBody Employee e){
        employeeService.update(e,new UpdateWrapper<Employee>().eq("id",e.getId()));
        return R.success("修改成功");
    }
}
