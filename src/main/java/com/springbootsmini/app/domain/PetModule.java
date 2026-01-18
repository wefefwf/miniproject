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
public class PetModule {
	
	
    private int id;
    private int petId;        // FK
    private Double weight;
    private String content;   // 기록 내용
    private Timestamp recordDate;
    
    
}
