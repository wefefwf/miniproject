package com.springbootsmini.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.mapper.UserMapper;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	//회원 가입 폼 
	public void addUser(User user){
		//비밀번호 비식별화 
		user.setPass(passwordEncoder.encode(user.getPass()));
		userMapper.addUser(user);
	}
	
	
	//회원 가입 폼에서 아이디 중복 체크하는 메서드
	public boolean overlapIdCheck(String id){
		
		//해당 아이디의 유저가 있는지 검색후 
		// 없으면 false 있으면 true(중복 보내기)
		User user = userMapper.getUser(id);
		if(user == null ){
			return false;			
		}
		return true;
	}
	
	
	// 회원 정보를 UserMapper를 이용해 회원 테이블에서 수정하는 메서드
	public void updateUser(User user) {
		
	// BCryptPasswordEncoder 객체를 이용해 비밀번호를 암호화한 후 저장
	//user.setPass(passwordEncoder.encode(user.getPass()));
	log.info(user.getPass());
	userMapper.updateUser(user);
	}
	
	//회원 정보 수정시 기존 번호가 맞는지 체크
	public boolean userPassCheck(String id, String pass) {
		String dbPass = userMapper.userPassCheck(id);
		boolean result = false;
		if(passwordEncoder.matches(pass, dbPass)) {
		result = true;
		}
		return result;
		}
	
	

	// 회원 로그인을 처리하는 메서드 - 
	public int login(String id, String pass) {
		
		//  -1 : 아이디 없음, 0 : 비밀번호 틀림, 1 : 로그인 성공
		int result = -1;
		
		User u = userMapper.getUser(id);
		if(u == null) {
			return result;
		}
		
		if(passwordEncoder.matches(pass, u.getPass())) {
			result  = 1;
		} else {
			result = 0;
		}
		
		return result;
	}

	
	public User getUser(String id) {
		return userMapper.getUser(id);
	}
	
	
	
}





