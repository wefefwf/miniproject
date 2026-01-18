package com.springbootsmini.app.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.PetImage;
import com.springbootsmini.app.domain.PetModule;
import com.springbootsmini.app.mapper.PetMapper;

@Service
public class PetService {

	@Autowired 
	public PetMapper petMapper;
	
	//파일 저장 경로
	private static String uploadPath = "src/main/resources/static/upload/pet/";
	
	
	//펫 업데이트
	public void petUpdate(int petId,double weight,MultipartFile petImageFile,String content) throws IOException{
		
		//펫 업데이트(펫 아이디로)
		petMapper.petUpdate(petId,weight,content);
		
		 if(petImageFile != null ) {
		    	//파일이 빈게 아니면 경로로 파일 만듬 
	            File parent = new File(uploadPath);
	            //파일 저장소 만들기
	            if(!parent.exists()) parent.mkdirs();
	            //이름바꾸기
	            UUID uid = UUID.randomUUID();
	            
	            String extension = StringUtils.getFilenameExtension(petImageFile.getOriginalFilename());
	            String saveName = uid.toString() + "." + extension;
	            
	            //파일 새로만들기 저장소안에 
	            File file = new File(parent.getAbsolutePath(), saveName);
	            //파일 저장 
	            petImageFile.transferTo(file);

	            //xml에서 받는 값이 petImage
	            PetImage petImage = new PetImage();
	            petImage.setPetId(petId);
	            petImage.setFileName(saveName);
	            //펫 이미지 추가
	            petMapper.addPetImage(petImage);
		    }
			
		//펫 모듈 추가
		 petMapper.addPetModule(petId, weight, content);
	};
	
	
	
	//펫Id로 하나 지우면 ON DELETE CASCADE해놔서 다지워짐
	public void deletePet(int petId){
		petMapper.deletePet(petId);
	}
	
	
	//펫 하나에 해당하는 정보 다가져오기 
	public Pet getPetByPetId(int PetId){
		return petMapper.getPetByPetId(PetId);
	}
	
	
	
	
	//펫 추가
	public void addPet(Pet pet,MultipartFile petImageFile) throws IOException{
		
		petMapper.addPet(pet);
		
	    // 3. 파일 처리
	    if(petImageFile != null ) {
	    	//파일이 빈게 아니면 경로로 파일 만듬 
            File parent = new File(uploadPath);
            //파일 저장소 만들기
            if(!parent.exists()) parent.mkdirs();
            //이름바꾸기
            UUID uid = UUID.randomUUID();
            
            String extension = StringUtils.getFilenameExtension(petImageFile.getOriginalFilename());
            String saveName = uid.toString() + "." + extension;
            
            //파일 새로만들기 저장소안에 
            File file = new File(parent.getAbsolutePath(), saveName);
            //파일 저장 
            petImageFile.transferTo(file);

            //xml에서 받는 값이 petImage
            PetImage petImage = new PetImage();
            petImage.setPetId(pet.getPetId());
            petImage.setFileName(saveName);
            petMapper.addPetImage(petImage);
	    }
		
		petMapper.addPetModule(pet.getPetId(),pet.getWeight(),pet.getContent());
	}
	
	
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
