package com.ssp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssp.common.R;
import com.ssp.entities.Employee;
import com.ssp.service.EmployeeService;
import com.ssp.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {

        Employee emp = this.getOne(new QueryWrapper<Employee>().eq("username", employee.getUsername()));
        if(emp == null){
            return R.error("用户名不存在");
        }
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        if(!password.equals(emp.getPassword())){
            return R.error("密码错误");
        }
        if(emp.getStatus() != 1){
            return R.error("该用户已被禁用");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @Override
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @Override
    public R<Page<Employee>> queryListByPage(Integer page, Integer pageSize,String name) {
        Page<Employee> employee = new Page<Employee>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if(name != null){
            queryWrapper.like(Employee::getName,name);
        }
        queryWrapper.orderByDesc(Employee::getCreateTime);
        this.baseMapper.selectPage(employee, queryWrapper);
        return R.success(employee);
    }
}




