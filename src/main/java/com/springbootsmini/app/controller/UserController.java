package com.springbootsmini.app.controller;

import java.io.PrintWriter;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.UserService;
import com.springbootsmini.app.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//로그인 안 된 상태면 securityconfig가 막고 
	@GetMapping("/userUpdateForm")
	public String showUpdateForm(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    model.addAttribute("user", user);
	    return "views/userUpdateForm";
	}

	//로그인 된 상태에서 회원가입못하게하기
	@GetMapping("/joinForm")
	public String showjoinForm(HttpSession session, Model model,RedirectAttributes re)throws Exception{
	    User user = (User) session.getAttribute("user");
	    
	    if(user  != null){
	    	re.addFlashAttribute("msg", "로그 아웃 상태에서 이용해 주세요.");
			return "redirect:/";
	    }
	    return "views/joinForm";
	}

	//회원가입
	//이름 같은건 자동으로 들어감
	@PostMapping("/joinResult")
	public String joinResult(User user,
			@RequestParam("pass1") String pass1, 
			@RequestParam("emailId") String emailId, 
			@RequestParam("emailDomain") String emailDomain, 
			@RequestParam("mobile1") String mobile1, 
			@RequestParam("mobile2") String mobile2, 
			@RequestParam("mobile3") String mobile3, 
			@RequestParam(value = "manager" , required =false , defaultValue = "0") int manager) {
		
			user.setPass(pass1);
			user.setEmail(emailId+"@"+emailDomain);
			user.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
			
			userService.addUser(user);
			
			return "redirect:/loginForm";
	}
	
	
	//아이디 중복 체크요청 들어오면 중복체크 후 값을 
	// 아이디 중복체크 확인 폼으로 보냄
	@RequestMapping("/overlapIdCheck")
	public String overlapIdCheck(Model model, @RequestParam("id") String id){
		
		//중복인지 아닌지 boolean으로 받음
		boolean overlapIdCheck = userService.overlapIdCheck(id);
		
		model.addAttribute("id",id);
		model.addAttribute("overlapIdCheck",overlapIdCheck);
		return "views/user/overlapIdCheck";
	}
	

	//정보수정업데이트 메서드
	@PostMapping("/userUpdateResult")
	public String memberUpdateResult(Model model, User user,
			@RequestParam("pass1") String pass1, 
			@RequestParam("emailId") String emailId, 
			@RequestParam("emailDomain") String emailDomain, 
			@RequestParam("mobile1") String mobile1, 
			@RequestParam("mobile2") String mobile2, 
			@RequestParam("mobile3") String mobile3, 
			@RequestParam(value = "manager" , required =false , defaultValue = "0") int manager,HttpSession session) {
		
		
		user.setPass(pass1);
		user.setEmail(emailId+"@"+emailDomain);
		user.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
		
		// MemberService를 이용해 회원 정보를 DB에서 수정한다.
		userService.updateUser(user);
		session.setAttribute("user", user);
		return "redirect:/mainPage";
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
				HttpServletResponse response,
				@RequestParam("userId") String id, 
		        @RequestParam("pass") String pass,
		        @RequestParam(value = "redirectUrl", required = false) String redirectUrl) throws Exception { // 파라미터 추가
			
			// 로그인 체크
			int result = userService.login(id, pass);
			
			response.setContentType("text/html; charset= UTF-8");
			PrintWriter out = response.getWriter();
			
			// 로그인 실패 직접 응답 - 2가지 - 아이디 없음, 비번 틀림
			if(result == -1) { // 아이디 없음
				// 직접 자바스크립트로 응답 - HttpServletResponse, 스트림
				out.println("<script>");
				out.println("	alert('아이디 없음');");
				out.println("	history.back();");
				out.println("</script>");
				return null;
				
			} else if(result == 0) { // 비번 틀림
				out.println("<script>");
				out.println("	alert('비밀번호 틀림');");
				out.println("	history.back();");
				out.println("</script>");
				return null;			
			}
			
			// 로그인 성공		
			User user = userService.getUser(id);

			session.setAttribute("isLogin", true);
			session.setAttribute("user", user);
			
			// [추가된 로직] 돌아갈 주소가 있다면 그 주소로, 없으면 mainPage로
		    if (redirectUrl != null && !redirectUrl.isEmpty()) {
		        return "redirect:" + redirectUrl;
		    }
			
			return "redirect:mainPage";
		}
	}
