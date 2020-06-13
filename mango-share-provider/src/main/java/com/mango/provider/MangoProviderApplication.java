package com.mango.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mango"})
@MapperScan("com.mango.core.dao")
public class MangoProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MangoProviderApplication.class, args);
	}

}
