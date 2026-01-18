package com.springbootsmini.app.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.PetImage;
import com.springbootsmini.app.domain.PetModule;

@Mapper
public interface PetMapper {
	
	//펫 아이디로 펫 하나 지움
	public void deletePet(@Param("petId") int petId);
	
	//펫 아이디에 해당하는 이미지 최신으로 한개만 가져오기
	public String getLastPetImage (@Param("petId") int petId);
	
	//--------------------------------------------------------------------------
	//펫 아이디에 해당하는 펫내용 가져오기
	public Pet getPetByPetId(@Param("petId") int petId);
	
	//--------------------------------------------------------------------------
	//펫 추가
	public void addPet(Pet pet);
	
	//펫 이미지 추가
	public void addPetImage(PetImage petImage);
	
	//펫 모듈 추가
	public void addPetModule(@Param("petId") int petId,@Param("weight") double weight,@Param("content") String content);
	
	//--------------------------------------------------------------------------
	//동물 하나 별 업데이트 정보들
	public List<PetModule> getPetModules(@Param("petId") int petId);
	
	//동물 하나 별 이미지 리스트
	public List<PetImage> getPetImages(@Param("petId") int petId);
	
	//유저별 동물 리스트
	public List<Pet> getPetList(@Param("userId") String userId);
}
