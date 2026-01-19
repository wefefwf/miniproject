package com.springbootsmini.app.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.Category;
import com.springbootsmini.app.domain.Product;
import com.springbootsmini.app.domain.User;
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
    //상품 페이지 사진 
    @GetMapping("/productWrite")
    public String productWriteForm(Model model) {
        // 상단 메뉴나 선택창에 뿌릴 카테고리 전체 목록 가져오기
        List<Category> cList = productService.getCategoryList(); 
        model.addAttribute("cList", cList);
        return "views/product/productWrite";
    }

    // 4. 상품 등록 실행 (Create - POST)
    @PostMapping("/productWrite")
    public String productWrite(Product product, 
                               @RequestParam("file") MultipartFile file, 
                               HttpSession session) throws Exception {
        
        // 1. [보안] 세션에서 로그인 유저 정보 확인
        User user = (User) session.getAttribute("user");
        
        // 로그인 상태가 아니거나 manager가 1(관리자)이 아니면 리스트로 튕겨내기
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        // 2. [파일 업로드] 이미지 파일 처리
        if (file != null && !file.isEmpty()) {
            // 실제 파일이 저장될 서버 내부 경로 (본인의 프로젝트 경로에 맞게 static 폴더 확인)
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload/products/";
            
            // 폴더가 없으면 생성
            File dir = new File(projectPath);
            if (!dir.exists()) dir.mkdirs();

            // 파일명 중복 방지를 위해 UUID 생성 (예: 3e2f..._dog.jpg)
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            
            // 지정된 경로에 파일 물리적 저장
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            
            // DB에 저장할 웹 접근 경로 세팅 (예: /upload/products/uuid_name.jpg)
            product.setImage_url("/upload/products/" + fileName);
        }

        // 3. [DB 저장] 서비스 호출 (기존 addProduct 유지)
        productService.addProduct(product); 
        
        return "redirect:/productList";
    }

    //여기서 관리자
 // 5. 상품 수정 페이지 이동 (Update - GET)
    @GetMapping("/productUpdate")
    public String productUpdateForm(@RequestParam("product_id") int product_id, HttpSession session, Model model) {
        // [추가] 세션에서 로그인 유저 정보 가져오기
        User user = (User) session.getAttribute("user");

        // [추가] 로그인 상태가 아니거나 manager가 1(관리자)이 아니면 리스트로 리다이렉트
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        Product product = productService.getProduct(product_id); 
        List<Category> cList = productService.getCategoryList();
        
        model.addAttribute("product", product);
        model.addAttribute("cList", cList);
        return "views/product/productUpdate";
    }

 // 6. 상품 수정 실행 (Update - POST)
    @PostMapping("/productUpdate")
    public String productUpdate(Product product, 
                               @RequestParam("file") MultipartFile file, // 파일 받기 추가
                               HttpSession session) throws Exception {
        // [보안 추가] 관리자 권한 체크
        User user = (User) session.getAttribute("user");
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        // --- 여기서부터 파일 처리 로직 
        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/upload/products/";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            product.setImage_url("/upload/products/" + fileName);
        }
        productService.updateProduct(product); // DB 정보 갱신
        // 수정한 상품의 상세 페이지로 다시 이동
        return "redirect:/productDetail?product_id=" + product.getProduct_id();
    }

    // 7. 상품 삭제 실행 (Delete - POST)
    @PostMapping("/productDelete")
    public void productDelete(@RequestParam("product_id") int product_id, HttpSession session, HttpServletResponse response) throws Exception {
        // [보안 추가] 관리자 권한 체크
        User user = (User) session.getAttribute("user");
        if (user == null || user.getManager() != 1) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('권한이 없습니다.'); location.href='/productList';</script>");
            out.close();
            return;
        }

        productService.deleteProduct(product_id); // DB에서 삭제
        
        // 삭제 후 알림창 띄우고 리스트로 이동
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('상품이 삭제되었습니다.'); location.href='/productList';</script>");
        out.close();
    }
}