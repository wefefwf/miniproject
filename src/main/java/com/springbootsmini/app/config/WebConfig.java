package com.springbootsmini.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
		.addPathPatterns(
	            "/add*", "/update*", "/userUpdate*", 
	            "/cart*", "/order*","/petAddForm", "/diaryWrite*"  // 장바구니와 주문 관련 패턴 추가
	        );
	}
	
	// 이 부분을 꼭 추가해야 이미지가 나옴
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 내 컴퓨터의 복잡한 주소를 다 지우고, 프로젝트 안의 static 폴더를 바로 보게 만듭니다.
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("classpath:/static/upload/");
    }
}
