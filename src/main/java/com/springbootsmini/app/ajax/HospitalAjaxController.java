package com.springbootsmini.app.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.Hospital;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.BoardService;
import com.springbootsmini.app.service.HospitalService;
import com.springbootsmini.app.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class HospitalAjaxController {

	@Autowired
	private HospitalService hospitalService;
	

	@GetMapping("/hospital/detail")
	 public Hospital getHospitalDetail(@RequestParam("hospitalId") int hospitalId){
	
		return hospitalService.getHospitalDetail(hospitalId);
	} 
	
	
	
	
	
	
	
	
}
