package com.springbootsmini.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;
import com.springbootsmini.app.mapper.ProductMapper;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    public List<Category> getCategoryList() {
        return productMapper.getCategoryList();
    }

    public List<Product> getProductList(int category_id) {
        return productMapper.getProductList(category_id);
    }

    public Product getProduct(int product_id) {
        return productMapper.getProduct(product_id);
    }

    public void addProduct(Product product) {
        productMapper.addProduct(product);
    }
    
 // 1. 상품 등록 (컨트롤러에서 insertProduct로 호출 중이라면 이름을 맞춰줍니다)
    public void insertProduct(Product product) {
        productMapper.addProduct(product); // 실제 Mapper의 메서드명과 연결
    }

    // 2. 상품 수정
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    // 3. 상품 삭제
    public void deleteProduct(int product_id) {
        productMapper.deleteProduct(product_id);
    }
}





