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

public class Product {
    private int product_id;
    private int category_id;
    private String product_name;
    private int price;
    private String image_url;
    private String description;
    
    private String rating;
    private String recall_status;
    private String ingredient_check;
    private String ingredients;        // 에러 원인!
    private String nutrition;          // 추가한 항목들
    private String origin_manufacturer; // 추가한 항목들
}
