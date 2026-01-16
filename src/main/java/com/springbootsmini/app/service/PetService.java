package com.springbootsmini.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.PetImage;
import com.springbootsmini.app.domain.PetModule;
import com.springbootsmini.app.mapper.PetMapper;

@Service
public class PetService {

	@Autowired 
	public PetMapper petMapper;
	
	//동물 하나 의 가장 최신 이미지 하나 가져오기
	public String getLastPetImage(int petId){
		return petMapper.getLastPetImage(petId);
	};
	
	
	//동물 하나 별 업데이트 정보들
	public List<PetModule> getPetModules(@Param("petId") int petId){
		return petMapper.getPetModules(petId);
	};
	
	//동물 하나 별 이미지 리스트
	public List<PetImage> getPetImages(@Param("petId") int petId){
		return petMapper.getPetImages(petId);
	};
	
	//유저별 동물 리스트
	public List<Pet> getPetList(@Param("userId") String userId){
		return petMapper.getPetList(userId);
	};
	
	
}
