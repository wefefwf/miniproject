package com.springbootsmini.app.service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardHashtag;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.Hashtag;
import com.springbootsmini.app.domain.Hospital;
import com.springbootsmini.app.mapper.BoardMapper;
import com.springbootsmini.app.mapper.HospitalMapper;

@Service
public class HospitalService {

	@Autowired
	public HospitalMapper hospitalMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	//한페이지에 몇개 보여줄건지
	private static final int PAGE_SIZE = 9;
	//페이지네이션 몇개씩 보여줄건지
	private static final int PAGE_GROUP = 10;
	//파일 저장 경로
	private static String uploadPath = "src/main/resources/static/upload/board/";

	//병원 전체 리스트 가져오기
	//주소로 검색 누가하면 보여주기(쿼리에 where추가)
	public Map<String,Object> getHospitalList(int pageNum,String address){
		
		//현재 페이지
		int currentPage = pageNum;
		
		//시작하는 번호 
		//1페이지 0~9 2페이지 10~18
		int startRow = (currentPage -1 )* PAGE_SIZE;
		
		//전체 갯수 들고오기
		int listCount = hospitalMapper.getHospitalCount(address);
		
		//해당 주소카테고리와 현재 페이지네이션 번호에 해당하는 내용물 들고오기
		//9개씩
		List<Hospital> hList = hospitalMapper.getHospitalList(address,startRow,PAGE_SIZE);
		
		//지금 9개씩 들고오는 건 성공
		//이제 페이지 네이션 설정
		
		//1~10페이지는 시작 페이지가 1
		//11~20페이지는 시작 페이지가 11
		int startPage = ((currentPage - 1) / PAGE_GROUP) * PAGE_GROUP + 1;
		
		int endPage = startPage + PAGE_GROUP -1;
		
		//페이지 갯수 확실하게(큰줄)
		int pageCount = listCount / PAGE_GROUP + (listCount % PAGE_GROUP == 0? 0 : 1);
		
		//페이지 갯수( 마지막 페이지 확실하게)
		if(endPage > pageCount){
			endPage = pageCount;
		}
		
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("hList",hList);
		modelMap.put("pageCount", pageCount); //전체 갯수 반올림 
		modelMap.put("startPage", startPage); // 페이지네이션 시작 번호
		modelMap.put("endPage", endPage); // 페이지 네이션 끝 번호
		modelMap.put("listCount", listCount); // 총 갯수
		modelMap.put("pageGroup", PAGE_GROUP); // 페이지네이션 갯수 기준
		modelMap.put("currentPage", currentPage); //현재 페이지
		
		//address 있는지 없는지
		// 안전하게 Boolean 객체로 넣기
		boolean isAddress = (address != null && !address.isEmpty());
		modelMap.put("isAddress", isAddress);
		if(isAddress) {
		    modelMap.put("address", address);
		} else {
		    modelMap.put("address", "");
		}
		return modelMap;
	};
	
}
