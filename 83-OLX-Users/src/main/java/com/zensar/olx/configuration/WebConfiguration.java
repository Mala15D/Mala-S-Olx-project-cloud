package com.zensar.olx.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
	
			registry
							.addMapping("/**")
						//.allowedMethods("GET","DELETE","PUT","POST","OPTIONS")
							.allowedMethods("*")
						.allowedOrigins("http://localhost:4200");
		}
	}


