
package com.springbootsmini.app.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

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

	//pet 업데이트 폼 가기
	@GetMapping("/petUpdateForm")
	public String goPetDetail(@RequestParam("petId")  int petId, Model model) {
		//펫 아이디로 펫 가져오기
	    Pet pet = petService.getPetByPetId(petId);
	    // 내부 썸네일 값...가져오기..
	    pet.setPetImage(petService.getLastPetImage(petId));

	    model.addAttribute("pet", pet);
	    return "views/pet/petUpdateForm";
	}
	
	
	//삭제버튼 누르면
	@GetMapping("/deletePet")
	public String deletePet(@RequestParam("petId")int petId){
		
		//삭제
		petService.deletePet(petId);
		//완료후 펫 전체 프로필로 가기 ~
		return "redirect:/petProfile";
	};
	
	//펫 디테일가기
	@GetMapping("/petDetail")
	public String goPetDetail(@RequestParam("petId") int petId,
										HttpSession session,
										Model model){
		
		//일단 petId를 받앗으니 petId에 해당하는 pet정보 들고오기 !!
		User user = (User) session.getAttribute("user");
	    String userId = user.getId();
	    
	    //아이디 값도 있어야 나중에 업데이트 가능
	    //펫 정보가져오기
	   Pet pet = petService.getPetByPetId(petId);
	   //맨위에 나올 가장 마지막에 추가된 이미지 설정
	   pet.setPetImage(petService.getLastPetImage(petId)); 
	   
	   //해당하는 이미지 다들고오기
	   pet.setImages(petService.getPetImages(petId));
	   
	   //해당하는 모듈 다들고오기
	   pet.setModules(petService.getPetModules(petId));
	   
	   	model.addAttribute("pet",pet);
		return "views/pet/petDetail";
	}
	
	
	//펫 추가버튼 눌리면
	@PostMapping("/petAdd")
	public String petAdd(
			Model model,
			HttpSession session,
			@RequestParam("name")String name,
			@RequestParam("age") int age,
			@RequestParam("weight") double weight,
			@RequestParam("gender")String gender,
			 @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
			@RequestParam(value = "petImageFile", required = false) MultipartFile petImageFile,
			@RequestParam("content")String content
			) throws IOException{
		
		User user = (User) session.getAttribute("user");
	    String userId = user.getId(); // 세션에서 바로 가져오기
		
		//내용물 다 받아서 pet으로 넣고
		Pet pet  = new Pet();
		pet.setUserId(userId);
		pet.setName(name);
		pet.setAge(age);
		pet.setWeight(weight);
		pet.setGender(gender);
		pet.setBirthday(birthday);
		pet.setContent(content);
		
		
		//그럼 위에서 펫이 들어갓을테니까
		//이미지는 petImage에 넣고 
		
		//몸무게도 저장해야되니까 petModule에도 저장해야됨 
		//service가서 각자 넣기
		petService.addPet(pet,petImageFile);
		
		 return "redirect:/petProfile";
	}
	
	
	
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
	    session.setAttribute("userId", userId);
	    return "views/pet/petProfile";
	}
	
	
	
}
