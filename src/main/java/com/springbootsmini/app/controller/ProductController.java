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
    
 // 3. 상품 등록 페이지 이동 (Create - GET)
    @GetMapping("/productWrite")
    public String productWriteForm(Model model) {
        // 상단 메뉴나 선택창에 뿌릴 카테고리 전체 목록 가져오기
        List<Category> cList = productService.getCategoryList(); 
        model.addAttribute("cList", cList);
        return "views/product/productWrite";
    }

    // 4. 상품 등록 실행 (Create - POST)
    @PostMapping("/productWrite")
    public String productWrite(Product product) {
        // insertProduct 대신 서비스에 정의된 addProduct로 변경
        productService.addProduct(product); 
        return "redirect:/productList";
    }

    // 5. 상품 수정 페이지 이동 (Update - GET)
    @GetMapping("/productUpdate")
    public String productUpdateForm(@RequestParam("product_id") int product_id, Model model) {
        Product product = productService.getProduct(product_id); // 기존 정보 가져오기
        List<Category> cList = productService.getCategoryList();
        
        model.addAttribute("product", product);
        model.addAttribute("cList", cList);
        return "views/product/productUpdate";
    }

    // 6. 상품 수정 실행 (Update - POST)
    @PostMapping("/productUpdate")
    public String productUpdate(Product product) {
        productService.updateProduct(product); // DB 정보 갱신
        // 수정한 상품의 상세 페이지로 다시 이동
        return "redirect:/productDetail?product_id=" + product.getProduct_id();
    }

    // 7. 상품 삭제 실행 (Delete - POST)
    @PostMapping("/productDelete")
    public void productDelete(@RequestParam("product_id") int product_id, HttpServletResponse response) throws Exception {
        productService.deleteProduct(product_id); // DB에서 삭제
        
        // 삭제 후 알림창 띄우고 리스트로 이동
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('상품이 삭제되었습니다.'); location.href='/productList';</script>");
        out.close();
    }
}