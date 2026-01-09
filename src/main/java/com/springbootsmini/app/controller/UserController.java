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
	
	/*
	 * @GetMapping("/loginForm") public String loginForm() {
	 * 
	 * //model.addAttribute("loginMsg", loginMsg); //log.info("/loginForm : " +
	 * loginMsg); return "user/loginForm"; }
	 */
	
	@GetMapping("/userUpdateForm")
	public String updateForm(Model model, HttpSession session) {
		
		return "user/userUpdateForm";
	}
}
