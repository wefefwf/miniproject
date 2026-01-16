
package com.springbootsmini.app.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.PetImage;
import com.springbootsmini.app.domain.PetModule;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.PetService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PetController {

	@Autowired
	public PetService petService;

	//펫추가 폼 가기
	//Webconfig에 설정해놔서 비번체크 안해도됨
	@GetMapping("/petAddForm")
	public String goPetAddForm(HttpSession session, Model model){
		
		//세션에서 유저뽑아서 만들어서 유저 아이디만 쏙 들고가기 
		//세션에도 있긴한데 userId쓰게 편하게 하려고 들고감 
		User user = (User) session.getAttribute("user");
	    model.addAttribute("userId", user.getId());

	    return "views/pet/petAddForm";
	};
	
	//펫프로필로 가기
	@GetMapping("/petProfile")
	public String goPetProfile(Model model, HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {
	    
	    User user = (User) session.getAttribute("user"); // 직접 꺼냄
	    String userId = user != null ? user.getId() : null;

	    if(userId == null) {
	        String redirectUrl = request.getRequestURL().toString();
	        String queryString = request.getQueryString();
	        if(queryString != null) redirectUrl += "?" + queryString;
	        return "redirect:/loginForm?redirectUrl=" + URLEncoder.encode(redirectUrl, "UTF-8");
	    }

	    List<Pet> petList = petService.getPetList(userId);

	    for(Pet pet : petList){
	        List<PetImage> images = petService.getPetImages(pet.getPetId());
	        pet.setImages(images);

	        List<PetModule> modules = petService.getPetModules(pet.getPetId());
	        pet.setModules(modules);
	        
	        //해당 펫 아이디의 가장 마지막 사진 이름 들고오기
	        String petImage = petService.getLastPetImage(pet.getPetId());
	        //썸네일 이미지 용으로 생각하고 저장
	        pet.setPetImage(petImage);
	    }

	    model.addAttribute("petList", petList);
	    model.addAttribute("userId", userId);
	    return "views/pet/petProfile";
	}
	
	
	
}
