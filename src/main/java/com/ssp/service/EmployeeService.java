package com.ssp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssp.common.R;
import com.ssp.entities.Employee;
import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public interface EmployeeService extends IService<Employee> {

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<String> logout(HttpServletRequest request);

    R<Page<Employee>> queryListByPage(Integer page, Integer pageSize,String name);
}
