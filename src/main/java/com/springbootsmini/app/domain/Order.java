package com.springbootsmini.app.domain;

import lombok.Data;

@Data
public class Order {
    private String merchant_uid; // 주문 번호 (결제 API에서 발급)
    private String id;           // 유저 ID (구매자)
    private String name;         // 주문명 (예: 사료 외 1건)
    private int amount;          // 총 결제 금액
    private String buyer_email;  // 구매자 이메일
    private String buyer_tel;    // 구매자 연락처
    private String status;       // 주문 상태 (PAID, READY 등)
}