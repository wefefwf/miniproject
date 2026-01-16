
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
	    }

	    model.addAttribute("petList", petList);
	    model.addAttribute("userId", userId);
	    return "views/pet/petProfile";
	}
	
	
	
}
