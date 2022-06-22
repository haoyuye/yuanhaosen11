package com.ssp.controller;

import com.ssp.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${takeout.baseUrl}")
    String path;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        File store = new File(path);
        if(!store.exists()){
            store.mkdirs();
        }
        try {
            file.transferTo(new File(path+newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newName);
    }

    @GetMapping("/download")
    public R<String> download(HttpServletResponse response, String name){
        try {
            FileInputStream fis = new FileInputStream(path + name);
            int len = 0;
            byte[] bytes = new byte[1024];
            ServletOutputStream outputStream = response.getOutputStream();
            while ((len = fis.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success("下载成功");
    }
}
