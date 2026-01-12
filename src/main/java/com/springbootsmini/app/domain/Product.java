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
    private double price;
    private String image_url;
    private String description;
}