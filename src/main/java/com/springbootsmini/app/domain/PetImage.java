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
public class PetImage {
	
    private int imageId;
    private int petId;        // FK
    private String fileName;
    private Timestamp uploadedAt;
    
}
