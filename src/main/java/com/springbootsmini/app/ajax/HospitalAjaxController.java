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
	
	
	//삭제 누르면
	@PostMapping("/delete.ajax")
	public String deleteHospital(HttpSession session,
			@RequestParam("hospitalId") int hospitalId){
	
	Boolean isManager = (Boolean) session.getAttribute("isManager");
	//매니저 권한 있으면 지우기 
	if (isManager != null && isManager) {
        hospitalService.deleteHospital(hospitalId);
        
        return "success";
    }
        //매니저 권한이 없을수도 잇음 자기가 쳐서들어오면 
        return "false"; 
	}
	
	//좋아요나 싫어요 누르면
	@PostMapping("/like.ajax")
	 public Hospital getGoodBad(@RequestParam("hospitalId") int hospitalId,@RequestParam("type") String type){
		
		hospitalService.updateGoodBad(hospitalId,type);
		return hospitalService.getHospitalDetail(hospitalId);
	} 

	//병원 상세 모달로 들고오기
	@GetMapping("/hospital/detail")
	 public Hospital getHospitalDetail(@RequestParam("hospitalId") int hospitalId){
	
		return hospitalService.getHospitalDetail(hospitalId);
	} 
	
	
	
	
	
	
	
	
}
