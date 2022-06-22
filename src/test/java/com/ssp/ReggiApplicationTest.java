package com.ssp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssp.entities.Employee;
import com.ssp.entities.User;
import com.ssp.service.EmployeeService;
import com.ssp.service.UserService;
import com.ssp.utils.HttpUtils;
import com.ssp.utils.SendMessage;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ReggiApplicationTest {

//    @Autowired
//    UserService userService;
//
//    @Autowired
//    EmployeeService employeeService;

    @Test
    public void testInsert(){
//        User user = new User();
//        user.setName("sss");
//        user.setPhone("1231");
//        user.setSex("0");
//        user.setStatus(0);
//        userService.save(user);
//        Assert.assertTrue(user.getId() > 0);
//        Employee byId = employeeService.getById(1);
//        System.out.println(byId);
//        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
////        employeeService.list(employeeQueryWrapper);
//        employeeService.getOne(employeeQueryWrapper.eq("id",1));
    }

    @Test
    public void testSendMsg(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.sendMessage("17366169547","5873");
    }

    @Test
    public void testSMS(){
        String host = "https://cxkjsms.market.alicloudapi.com";
        String path = "/chuangxinsms/dxjk";
        String method = "POST";
        String appcode = "523be21a44b044db8633b64b07f38ba8";//开通服务后 买家中心-查看AppCode
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", "【创信】你的验证码是：5873，3分钟内有效！");
        querys.put("mobile", "17366169547");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
