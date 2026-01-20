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
	    registry.addViewController("/writeHospital").setViewName("views/hospital/writeHospital");
		
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
	//추가됨
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // 프로젝트 루트 경로 확보
	    String rootPath = System.getProperty("user.dir");

	    registry.addResourceHandler("/upload/**")
	            // file: 을 붙여야 '실제 컴퓨터 폴더'를 뒤집니다.
	            .addResourceLocations("file:" + rootPath + "/upload/")
	            // 기존에 static/upload에 들어있던 배너 같은 파일들도 같이 보게 함
	            .addResourceLocations("classpath:/static/upload/");
	}
}
