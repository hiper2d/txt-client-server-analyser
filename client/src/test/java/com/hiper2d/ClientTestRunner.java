package com.hiper2d;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "com.hiper2d", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ClientRunner.class)
})
public class ClientTestRunner {
	public static void main(String[] args) {
		SpringApplication.run(ClientTestRunner.class, args);
	}
}
