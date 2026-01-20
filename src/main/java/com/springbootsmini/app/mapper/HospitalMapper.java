package com.springbootsmini.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardHashtag;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.Hashtag;
import com.springbootsmini.app.domain.Hospital;

@Mapper
public interface HospitalMapper {
	
	//병원 디테일 들고오기
	public Hospital getHospitalDetail(@Param("hospitalId")int hospitalId);
	
	//병원 테이블 안ㅇ 갯수 다 들고오기
	public int getHospitalCount(@Param("address")String address);
	
	//병원 전체 리스트 가져오기
	public List<Hospital> getHospitalList(@Param("address")String address,@Param("startRow")int startRow,@Param("pageSize")int pageSize);
}
