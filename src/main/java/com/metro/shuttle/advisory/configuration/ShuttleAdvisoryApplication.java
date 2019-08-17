package com.metro.shuttle.advisory.configuration;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import com.metro.shuttle.advisory.security.entity.Role;
import com.metro.shuttle.advisory.security.entity.User;
import com.metro.shuttle.advisory.security.service.UserService;

@SpringBootApplication
@ComponentScan(basePackages = "com.metro.shuttle")
@EnableJpaRepositories("com.metro.shuttle.advisory.security.jpa.repository")
@EntityScan("com.metro.shuttle.advisory.security.entity")
@EnableCircuitBreaker
@EnableHystrixDashboard
public class ShuttleAdvisoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShuttleAdvisoryApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	@Bean
	public CommandLineRunner setupDefaultUser(UserService service) {
		return args -> {
			service.save(new User("user", // username
					"user", // password
					Arrays.asList(new Role("USER"), new Role("ACTUATOR")), // roles
					true// Active
			));
		};
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

}
