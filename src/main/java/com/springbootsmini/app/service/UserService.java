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





