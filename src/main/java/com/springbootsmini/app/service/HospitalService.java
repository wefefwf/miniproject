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
	
	@Autowired
	private SqlSession sqlSession;
	
	
}
