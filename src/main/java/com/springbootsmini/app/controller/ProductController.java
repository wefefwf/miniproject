package com.springbootsmini.app.controller;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;
import com.springbootsmini.app.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. 상품 목록 페이지 (카테고리별로 보여줌)
    @GetMapping("/productList")
    public String productList(@RequestParam(value="category_id", defaultValue="1") int category_id, Model model) {
        // 1. 현재 카테고리에 맞는 상품들 가져오기
        List<Product> pList = productService.getProductList(category_id);
        
        // 2. 상단 메뉴에 뿌릴 카테고리 전체 목록 가져오기 (추가)
        List<Category> cList = productService.getCategoryList(); 
        
        model.addAttribute("pList", pList);
        model.addAttribute("cList", cList); // HTML에서 메뉴를 만들 때 사용
        
        return "views/product/productList";
    }

    // 2. 상품 상세 페이지
    @GetMapping("/productDetail")
    public String productDetail(@RequestParam("product_id") int product_id, Model model) {
        Product product = productService.getProduct(product_id);
        model.addAttribute("product", product);
        return "views/product/productDetail";
    }
}