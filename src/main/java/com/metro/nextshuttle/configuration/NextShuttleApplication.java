package com.metro.nextshuttle.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages="com.metro.nextshuttle")
public class NextShuttleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NextShuttleApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	         return new MethodValidationPostProcessor();
	}	

}
