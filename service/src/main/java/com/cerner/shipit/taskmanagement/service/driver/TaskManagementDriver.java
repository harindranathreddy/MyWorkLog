package com.cerner.shipit.taskmanagement.service.driver;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = { "com.cerner.shipit.taskmanagement" })
@EnableJpaRepositories(basePackages = { "com.cerner.shipit.taskmanagement.dao" })
@EntityScan(basePackages = { "com.cerner.shipit.taskmanagement.utility.entity" })
public class TaskManagementDriver extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementDriver.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TaskManagementDriver.class);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
