package com.springbootsmini.app.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

	private int hospitalId;
    private String name;
    private String doctorName; 
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private String content;
    private String mainImage;
    private Timestamp createdAt;
    private int good;
    private int bad;
}