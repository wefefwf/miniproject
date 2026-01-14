package com.springbootsmini.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springbootsmini.app.domain.Cart;
import com.springbootsmini.app.mapper.CartMapper; // CartMapper가 필요합니다.

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    // 1. 장바구니 담기 핵심 로직
    public void addCart(Cart cart) {
        // 이미 담긴 상품인지 확인
        Cart check = cartMapper.checkCart(cart);
        
        if (check == null) {
            // 없으면 새로 추가
            cartMapper.addCart(cart);
        } else {
            // 있으면 수량만 플러스
            cartMapper.updateCount(cart);
        }
    }

    // 2. 전체 장바구니 목록 가져오기 (추가)
    public List<Cart> getCartList(String id) {
        return cartMapper.getCartList(id);
    }

    // 3. 결제창을 위한 선택된 항목들만 가져오기 (추가)
    public List<Cart> getSelectedCartItems(List<Integer> cartIds) {
        return cartMapper.getSelectedCartItems(cartIds);
    }
    
	 // 4. 장바구니 항목 삭제 (추가 필수!)
	    public void deleteCart(int cartNo, String id) {
	        cartMapper.deleteCart(cartNo, id);
	    }

}

