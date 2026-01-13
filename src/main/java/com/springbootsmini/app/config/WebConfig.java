package com.springbootsmini.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springbootsmini.app.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/writeForm").setViewName("views/writeForm");
	    registry.addViewController("/writeBoard").setViewName("views/writeForm");
	    registry.addViewController("/loginForm").setViewName("views/user/loginForm");
		
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor())
			.addPathPatterns("/add*", "/write*", 
					"/update*", "/userUpdate*","/order**");
	}

	
}
