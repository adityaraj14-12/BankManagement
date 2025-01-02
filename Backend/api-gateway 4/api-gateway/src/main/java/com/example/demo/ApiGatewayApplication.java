package com.example.demo;
 
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//----------------


import java.util.List;


import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.CorsWebFilter;

import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@SpringBootApplication

@EnableDiscoveryClient

public class ApiGatewayApplication {

	@Bean

    public CorsWebFilter corsWebFilter() {

        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(List.of("http://localhost:3000")); // Specific react origin

        corsConfig.setMaxAge(3600L);

        corsConfig.addAllowedMethod("*");

        corsConfig.addAllowedHeader("*");

        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);

    }

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayApplication.class, args);

	}

}
 