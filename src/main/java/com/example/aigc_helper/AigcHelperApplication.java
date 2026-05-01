package com.example.aigc_helper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.aigc_helper.mapper")
@SpringBootApplication
public class AigcHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AigcHelperApplication.class, args);
    }

}
