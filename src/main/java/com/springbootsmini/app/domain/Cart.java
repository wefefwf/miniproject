package com.springbootsmini.app.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


    public class Cart {
        private int cartNo;          // 장바구니 PK
        private String id;           // User의 id (외래키)
        private int product_id;      // Product의 product_id (이름 통일)
        private int count;           // 수량

        // --- 장바구니 목록 화면에서 "함께" 보여주기 위해 필요한 필드들 ---
        // DB의 cart 테이블에는 없지만, JOIN을 통해 가져올 정보
        private String product_name; 
        private int price;
        private String image_url;
        
     // --- [추가] 결제 및 화면 제어를 위해 필요한 필드들 ---
        private int totalPrice;      // (price * count) 서버에서 계산해서 넘겨주면 편리함
        private int deliveryFee;     // 배송비 (보통 3000원, 특정 금액 이상 무료 등)
        
        // 추가로 상품의 카테고리나 재고 상태가 필요하다면 아래처럼 추가 가능
        // private String category;
        // private int stock;
    }