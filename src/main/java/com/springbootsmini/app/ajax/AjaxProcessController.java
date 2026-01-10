package com.springbootsmini.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbootsmini.app.service.UserService;

@RestController
public class AjaxProcessController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserService boardService;
	
	@GetMapping("/passCheck.ajax")	
	public Map<String, Boolean> userPassCheck(
			@RequestParam("id") String id,
			@RequestParam("pass") String pass) {
		
		boolean result = userService.userPassCheck(id, pass);
		
		Map<String, Boolean> map = new HashMap<>();
		map.put("result", result);
		
		// 결과 데이터 : {"result": "true"}		
		return map;
	}
}
