package com.springbootsmini.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

@Mapper
public interface OrderMapper {
    // XML의 id="addOrder"와 연결됨
    void addOrder(Map<String, Object> params);
    
    // XML의 id="getOrderList"와 연결됨
    List<Map<String, Object>> getOrderList(String user_id);
}