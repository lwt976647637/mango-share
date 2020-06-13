package com.mango.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mango"})
@MapperScan("com.mango.core.dao")
public class MangoTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangoTaskApplication.class, args);
    }

}
