package com.springbootsmini.app.interceptor;

import java.util.List;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{

	// 우리 컨트롤러가 호출되기 전에 실행되는 메서드 
	// false를 반환하면 컨트롤러의 메서드가 호출되지 않는다. 
	@Override
	public boolean preHandle(HttpServletRequest request, 
	        HttpServletResponse response, Object handler) throws Exception {
	    log.info("########## LoginCheckInterceptor - preHandle ##########");

	    HttpSession session = request.getSession();
	    
	    if(session.getAttribute("isLogin") == null) {            
	        
	        // 💡 [추가] 현재 사용자가 요청한 주소(URI)와 파라미터(Query)를 가져옵니다.
	        String uri = request.getRequestURI();
	        String query = request.getQueryString();
	        String redirectUrl = (query == null) ? uri : uri + "?" + query;
	        
	        log.info("########## 로그인 후 돌아갈 주소: {} ##########", redirectUrl);

	        // 기존 FlashMap 로직
	        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
	        flashMap.put("loginMsg", "로그인이 필요한 서비스");
	        
	        RequestContextUtils.getFlashMapManager(request)
	                .saveOutputFlashMap(flashMap, request, response);
	        
	        // 💡 [수정] 로그인 폼으로 보낼 때 redirectUrl 파라미터를 붙여서 보냅니다.
	        response.sendRedirect("/loginForm?redirectUrl=" + redirectUrl);
	        return false;
	    }
	    
	    return true;
	}

	// 컨트롤러에서 요청을 처리한 후에 실행되는 메서드
	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("########## LoginCheckInterceptor - postHandle ##########");
		
		response.setHeader("Cache-Control", "no-cache");
	}

	// 뷰를 생성하고 클라이언트로 응답된 후에 호출되는 메서드
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("########## LoginCheckInterceptor - afterCompletion ##########");
	}

	
}
