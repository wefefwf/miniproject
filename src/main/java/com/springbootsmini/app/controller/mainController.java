package com.springbootsmini.app.controller;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class mainController {

	//알림 설정 업데이트 메서드
	@PostMapping("/alertModal")
	public String setalertModal(
	    @RequestParam(value = "updateAlert", required = false, defaultValue = "false") boolean updateAlert,
	    @RequestParam(value = "emailAlert", required = false, defaultValue = "false") boolean emailAlert,
	    @RequestParam(value = "securityAlert", required = false, defaultValue = "false") boolean securityAlert,
	    @RequestParam(value = "eventAlert", required = false, defaultValue = "false") boolean eventAlert,
	    RedirectAttributes ra,HttpSession session)throws Exception{
	    session.setAttribute("updateAlert", updateAlert);
	    session.setAttribute("emailAlert", emailAlert);
	    session.setAttribute("securityAlert", securityAlert);
	    session.setAttribute("eventAlert", eventAlert);
	    
	    ra.addFlashAttribute("alertMsg", "알림 정보가 업데이트 되었습니다.");
	    return "redirect:/mainPage";
	}

	
	//메인 페이지 이동 설정
	@GetMapping({"/mainPage","/","reeflo"})
	public String goMain(Model model){
	    model.addAttribute("mainPage", true); // main-page 클래스 붙임
	    return "views/main/mainPage";
	}
}
