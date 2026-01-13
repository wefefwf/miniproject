package com.springbootsmini.app.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Cart;
import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;

@Mapper
public interface CartMapper {
    
    // 1. 장바구니에 상품이 이미 있는지 확인 (결과가 있으면 수량 업데이트용)
    Cart checkCart(Cart cart);

    // 2. 장바구니에 상품 처음 추가 (INSERT)
    void addCart(Cart cart);

    // 3. 이미 있는 상품일 경우 수량만 증가 (UPDATE)
    void updateCount(Cart cart);

    // 4. 헤더 배지에 표시할 총 수량 조회 (SELECT)
    int getCartCount(String id);
}