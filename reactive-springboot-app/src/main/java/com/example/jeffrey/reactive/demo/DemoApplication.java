package com.example.jeffrey.reactive.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate rest() {
		return new RestTemplateBuilder().build();
	}

	@Bean
	public WebMvcConfigurer initializrWebMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}

			// Spring Boot 2 moves actuator endpoints to /actuator and we want
			// to follow that default by adding a simple redirect
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addRedirectViewController("/info", "/actuator/info");
			}
		};
	}
}
