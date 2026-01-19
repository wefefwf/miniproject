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
        // 브라우저가 /upload/products/** 주소로 사진을 요청하면
        // 실제 내 컴퓨터의 static/upload/products/ 폴더를 뒤져서 파일을 보여줍니다.
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///D:/SpringBootStudy 10/springbootsmini/miniproject/src/main/resources/static/upload/");

    }
}
