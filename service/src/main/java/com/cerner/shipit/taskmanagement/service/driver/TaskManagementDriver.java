package com.cerner.shipit.taskmanagement.service.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.cerner.shipit.taskmanagement" })
public class TaskManagementDriver extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementDriver.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TaskManagementDriver.class);
	}
}
