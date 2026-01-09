package com.springbootsmini.app.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.springbootsmini.app.domain.User;


@Mapper
public interface UserMapper {

	
	public User getUser(String id);
}
