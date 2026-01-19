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

	public int hospitalId;
	public String name;
	public String doctorName;
	public String address;
	public String phone;
	public double latitude;
	public double longitude;
	public String content;
	public String mainImage;
	public Timestamp createdAt;
	public int good;
	public int bad;
}