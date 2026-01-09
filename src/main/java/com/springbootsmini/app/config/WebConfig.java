package com.springbootsmini.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/joinForm").setViewName("views/joinForm");
		registry.addViewController("/writeForm").setViewName("views/writeForm");
	    registry.addViewController("/writeBoard").setViewName("views/writeForm");
	    registry.addViewController("/loginForm").setViewName("views/user/loginForm");
		
	}

	
}
