package com.springbootsmini.app.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.User;


@Mapper
public interface UserMapper {

	
	public User getUser(@Param("id") String id);
}
