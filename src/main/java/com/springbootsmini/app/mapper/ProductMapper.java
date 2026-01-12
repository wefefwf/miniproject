package com.springbootsmini.app.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;


@Mapper
public interface ProductMapper {
    
    // 상품 등록 (XML의 id="addProduct"와 매칭)
    void addProduct(Product product);
    
    // 상품 상세 정보 조회 (XML의 id="getProduct"와 매칭)
    Product getProduct(int product_id);
    
    // 카테고리별 상품 목록 조회 (XML의 id="getProductList"와 매칭)
    List<Product> getProductList(int category_id);
    
 // XML의 id="getCategoryList"와 매칭됩니다.
    List<Category> getCategoryList();
}