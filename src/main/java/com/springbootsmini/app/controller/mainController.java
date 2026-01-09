package com.springbootsmini.app.controller;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class mainController {

	@PostMapping("/alertModal")
	public void setalertModal(
	    @RequestParam(value = "updateAlert", required = false, defaultValue = "false") boolean updateAlert,
	    @RequestParam(value = "emailAlert", required = false, defaultValue = "false") boolean emailAlert,
	    @RequestParam(value = "securityAlert", required = false, defaultValue = "false") boolean securityAlert,
	    @RequestParam(value = "eventAlert", required = false, defaultValue = "false") boolean eventAlert,
	    HttpServletResponse response,HttpSession session)throws Exception{
	    session.setAttribute("updateAlert", updateAlert);
	    session.setAttribute("emailAlert", emailAlert);
	    session.setAttribute("securityAlert", securityAlert);
	    session.setAttribute("eventAlert", eventAlert);
	    
	    response.setContentType("text/html; charset=utf-8");
	    PrintWriter out = response.getWriter();
	    out.println("<script>");
	    out.println("alert('알림 정보가 업데이트 되었습니다.');");
	    out.println("location.href = '/';");
	    out.println("</script>");
	}

	
	//메인 페이지 이동 설정
	@GetMapping({"/mainPage","/","reeflo"})
	public String goMain(Model model){
	    model.addAttribute("mainPage", true); // main-page 클래스 붙임
	    return "views/main/mainPage";
	}
	
	//테스트 페이지 후 삭제 
	@GetMapping("test")
	public String goMain2(Model model){
	    model.addAttribute("mainPage", true); // main-page 클래스 붙임
	    return "views/test";
	}
}
