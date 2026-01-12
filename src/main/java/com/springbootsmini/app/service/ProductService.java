package com.springbootsmini.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.Product;
import com.springbootsmini.app.mapper.ProductMapper;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> getProductList(int category_id) {
        return productMapper.getProductList(category_id);
    }

    public Product getProduct(int product_id) {
        return productMapper.getProduct(product_id);
    }

    public void addProduct(Product product) {
        productMapper.addProduct(product);
    }
}





