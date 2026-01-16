package com.springbootsmini.app.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.PetImage;
import com.springbootsmini.app.domain.PetModule;

@Mapper
public interface PetMapper {
	
	
	//--------------------------------------------------------------------------
	//동물 하나 별 업데이트 정보들
	public List<PetModule> getPetModules(@Param("petId") int petId);
	
	//동물 하나 별 이미지 리스트
	public List<PetImage> getPetImages(@Param("petId") int petId);
	
	//유저별 동물 리스트
	public List<Pet> getPetList(@Param("userId") String userId);
}
