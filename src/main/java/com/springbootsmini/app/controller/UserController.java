package com.springbootsmini.app.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@SessionAttributes("user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//여기 추가
	@GetMapping("/userUpdateForm")
	public String updateForm(Model model, HttpSession session) {

		return "user/memberUpdateForm";
	}
	
	
	
	/*
	 * @GetMapping("/loginForm") public String loginForm() {
	 * 
	 * //model.addAttribute("loginMsg", loginMsg); //log.info("/loginForm : " +
	 * loginMsg); return "user/loginForm"; }
	 */
	//로그아웃 
	@GetMapping("/loginOut")
	public String logout(HttpSession session) {
		
		// 현재 세션을 종료하고 다시 시작한다.
		session.invalidate();
		return "redirect:mainPage";
	}
	//로그인
	@PostMapping("/login")
	public String login(Model model, HttpSession session,
			HttpServletResponse response, PrintWriter out,
			@RequestParam("userId") String id, @RequestParam("pass") String pass) {
		
		// 로그인 체크
		int result = userService.login(id, pass);
		
		// 로그인 실패 직접 응답 - 2가지 - 아이디 없음, 비번 틀림
		if(result == -1) { // 아이디 없음
			// 직접 자바스크립트로 응답 - HttpServletResponse, 스트림
			response.setContentType("text/html; charset='utf-8'");
			out.println("<script>");
			out.println("	alert('아이디 없음');");
			out.println("	history.back();");
			out.println("</script>");
			return null;
			
		} else if(result == 0) { // 비번 틀림
			response.setContentType("text/html; charset='utf-8'");
			out.println("<script>");
			out.println("	alert('비밀번호 틀림');");
			out.println("	history.back();");
			out.println("</script>");
			return null;			
		}
		
		// 로그인 성공		
		User user = userService.getUser(id);

		model.addAttribute("user", user);
		session.setAttribute("isLogin", true);
		return "redirect:mainPage";
	}
}
