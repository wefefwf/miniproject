package com.springbootsmini.app.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Cart;
import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;

@Mapper
public interface CartMapper {
    
	// 1. 장바구니에 상품이 이미 있는지 확인
    Cart checkCart(Cart cart);

    // 2. 장바구니에 상품 처음 추가
    void addCart(Cart cart);

    // 3. 이미 있는 상품일 경우 수량만 증가
    void updateCount(Cart cart);

    // 4. 헤더 배지에 표시할 총 수량 조회
    int getCartCount(String id);

    // --- 아래 두 메서드가 추가되어야 Service의 에러가 사라집니다 ---

    // 5. [추가] 장바구니 전체 목록 조회 (SELECT)
    List<Cart> getCartList(String id);

    // 6. [추가] 선택된 장바구니 항목들만 조회 (SELECT)
    List<Cart> getSelectedCartItems(List<Integer> cartIds);
    
	 // 7. [필수 추가] 장바구니 항목 삭제 (취소 기능)
	  void deleteCart(@Param("cartNo") int cartNo, @Param("id") String id);
	  
	// 8. [추가] 장바구니 화면에서 수량 직접 변경 (UPDATE)
	  void updateQty(@Param("cartNo") int cartNo, @Param("count") int count);
	  
}