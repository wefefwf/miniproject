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
    }