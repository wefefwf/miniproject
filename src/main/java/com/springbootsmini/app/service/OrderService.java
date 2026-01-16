package com.springbootsmini.app.service;

import java.util.List;
import com.springbootsmini.app.domain.Cart; // Cart 추가

public interface OrderService {
    // 결제 완료 후 주문을 생성하고 장바구니를 비우는 핵심 메서드
    void completeOrder(String userId, String merchantUid, List<Integer> cartIds);

}