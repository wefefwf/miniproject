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

public class Category {
    private int category_id;      // DB 컬럼명과 맞춤
    private String category_name;
    private String description;
}