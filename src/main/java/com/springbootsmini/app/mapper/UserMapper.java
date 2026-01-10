package com.springbootsmini.app.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.User;


@Mapper
public interface UserMapper {
	
	//회원 정보 회원 테이블에서 수정하는 메서드
	public void updateUser(User user);
	
	//userPassCheck
	public String userPassCheck(String id);

	
	public User getUser(@Param("id") String id);
}
