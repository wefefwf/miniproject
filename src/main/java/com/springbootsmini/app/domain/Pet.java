package com.springbootsmini.app.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
	
	private int petId;
    private String userId;      // user id FK
    private String name;
    private Integer age;
    private Double weight;
    private String gender;
    private Date birthday;
    private String petImage;      // 기본 이미지 나중에 가장 마지막 업데이트된 사진 들고와서 넣기 
    private String content;
    private Timestamp createdAt;

    //여긴 DB에 x 만들기만 
    private List<PetImage> images;       // pet_image 리스트
    private List<PetModule> modules; // pet_weight 리스트

    
}
