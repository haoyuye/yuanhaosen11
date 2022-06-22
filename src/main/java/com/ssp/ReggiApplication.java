package com.ssp;

import com.ssp.filter.LoginCheckFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class ReggiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggiApplication.class);
        log.info("项目启动完成");
    }
}
