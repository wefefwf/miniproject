package com.springbootsmini.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardHashtag;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.Hashtag;
import com.springbootsmini.app.domain.Hospital;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.BoardService;
import com.springbootsmini.app.service.HospitalService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HospitalController {
	

	
	@Autowired
	public HospitalService hospitalService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	
	
	//병원 업데이트 submit되면
	@PostMapping("/updateHospitalForm")
	public String updateHospital(@RequestParam("hospitalId") int hospitalId,
											@RequestParam("name")String name,
											@RequestParam("doctorName")String doctorName,
											@RequestParam("address") String address,
											@RequestParam("mobile1") String mobile1,
											@RequestParam("mobile2") String mobile2,
											@RequestParam("mobile3") String mobile3,
											@RequestParam("latitude") double latitude,
											@RequestParam("longitude") double longitude,
											@RequestParam("content") String content,
											@RequestParam(value = "file", required = false) MultipartFile mainImage,
											HttpSession session) throws IOException{
		
		
		String phone = mobile1+"-"+mobile2+"-"+mobile3;
		
		Hospital hospital = new Hospital();
		hospital.setHospitalId(hospitalId);
		hospital.setName(name);
		hospital.setDoctorName(doctorName);
		hospital.setAddress(address);
		hospital.setPhone(phone);
		hospital.setLatitude(latitude);
		hospital.setLongitude(longitude);
		hospital.setContent(content);
		
		
		hospitalService.updateHospital(hospital,mainImage);
		return "redirect:/hospital";
	}
	
	
	
	//병원 업데이트 폼 가기 
	@GetMapping("/updateHospital")
	public String goupdateHospital(@RequestParam("redirectUrl") String redirectUrl,
											@RequestParam("hospitalId") int  hospitalId, Model model) {

	    // 🔥 여기서 DB 조회
	    Hospital hospital = hospitalService.getHospitalDetail(hospitalId);

	    // 👉 이게 "hospitalId에 해당하는 내용 들고 가는 것"
	    model.addAttribute("hospital", hospital);
	    return "views/hospital/updateHospital";
	}


	//병원 추가 폼
	@PostMapping("/addHospital")
	public String addHospital(@RequestParam("name")String name,
										@RequestParam("doctorName")String doctorName,
										@RequestParam("address") String address,
										@RequestParam("mobile1") String mobile1,
										@RequestParam("mobile2") String mobile2,
										@RequestParam("mobile3") String mobile3,
										@RequestParam("latitude") double latitude,
										@RequestParam("longitude") double longitude,
										@RequestParam("content") String content,
										@RequestParam("file") MultipartFile mainImage,
										HttpSession session
										) throws IOException{
		
		
		String phone = mobile1+"-"+mobile2+"-"+mobile3;
		
		Hospital hospital = new Hospital();
		hospital.setName(name);
		hospital.setDoctorName(doctorName);
		hospital.setAddress(address);
		hospital.setPhone(phone);
		hospital.setLatitude(latitude);
		hospital.setLongitude(longitude);
		hospital.setContent(content);
		
		hospitalService.addHospital(hospital,mainImage);
		
	
		return "redirect:/hospital";
	}
	
	//병원 가기 
	@GetMapping("/hospital")
	public String goHospital(Model model,
			@RequestParam(value = "pageNum", required = true , defaultValue ="1")int pageNum,
			@RequestParam(value = "address", required = false)String address,
			HttpSession session){
		
		//병원 메인페이지로 가달라고 하면 지역별로 카테고리 가져갈 거 생각해서 쿼리짜서 가져가야함
		User user = (User) session.getAttribute("user");
		//1이면 manager 참
		boolean isManager = false;
		
		if(user != null) {
		    Integer manager = user.getManager();
		    if(manager != null) {
		        if(manager == 1){
		        	isManager = true;
		        };
		    }
		}
		session.setAttribute("isManager", isManager);
		model.addAttribute("hospitalList",hospitalService.getHospitalList(pageNum, address));
	    model.addAttribute("address", address);
	    model.addAttribute("pageNum", pageNum);
	    session.setAttribute("user", user);
	    model.addAttribute("redirectUrl", "/hospital?address=" + address);
		return "views/hospital/hospital";
	}
	
}
