package com.springbootsmini.app.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springbootsmini.app.mapper.CartMapper;
import com.springbootsmini.app.mapper.OrderMapper;
import com.springbootsmini.app.domain.Cart; // Cart 도메인 추가
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImple implements OrderService {

    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional // 하나라도 실패하면 전체 롤백(취소)
    public void completeOrder(String userId, String merchantUid, List<Integer> cartIds) {
        log.info("결제 성공 처리 시작 - 유저: {}, 주문번호: {}", userId, merchantUid);

        if (cartIds != null && !cartIds.isEmpty()) {
            // 결제한 상품 리스트를 가져옵니다 (이미 CartMapper에 getSelectedCartItems가 있음!)
            List<Cart> selectedItems = cartMapper.getSelectedCartItems(cartIds);

            for (Cart item : selectedItems) {
                // 1. 주문(orders) 테이블에 한 행씩 저장
                Map<String, Object> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("product_id", item.getProduct_id()); // Cart 객체에서 product_id 추출
                // XML에 status가 '주문완료'로 하드코딩 되어있으므로 여기까지만 넣어도 됩니다.
                
                orderMapper.addOrder(params); 

                // 2. 해당 장바구니 항목 삭제
                cartMapper.deleteCart(item.getCartNo(), userId);
            }
            log.info("주문 데이터 생성 및 장바구니 비우기 완료: {}건", cartIds.size());
        }
    }
}