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
                               @RequestParam("file1") MultipartFile file1, // HTML의 name="file1"
                               @RequestParam(value="file2", required=false) MultipartFile file2, // name="file2"
                               @RequestParam(value="file3", required=false) MultipartFile file3, // name="file3"
                               HttpSession session) throws Exception {
        
        // 1. [보안] 세션에서 로그인 유저 정보 확인 (기존 코드 유지)
        User user = (User) session.getAttribute("user");
        
        // 로그인 상태가 아니거나 manager가 1(관리자)이 아니면 리스트로 튕겨내기
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        // 2. [파일 업로드 경로 설정] (기존 코드 유지)
        String projectPath = System.getProperty("user.dir") + "/upload/products/";
        
        // 폴더가 없으면 생성
        File dir = new File(projectPath);
        if (!dir.exists()) dir.mkdirs();

        // --- [파일 1: 메인 이미지 처리 (image_url)] ---
        if (file1 != null && !file1.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file1.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file1.transferTo(saveFile);
            product.setImage_url("/upload/products/" + fileName);
        }

        // --- [파일 2: 상세 이미지 1 처리 (image_url2)] ---
        if (file2 != null && !file2.isEmpty()) {
            UUID uuid2 = UUID.randomUUID();
            String fileName2 = uuid2 + "_" + file2.getOriginalFilename();
            File saveFile2 = new File(projectPath, fileName2);
            file2.transferTo(saveFile2);
            product.setImage_url2("/upload/products/" + fileName2);
        }

        // --- [파일 3: 상세 이미지 2 처리 (image_url3)] ---
        if (file3 != null && !file3.isEmpty()) {
            UUID uuid3 = UUID.randomUUID();
            String fileName3 = uuid3 + "_" + file3.getOriginalFilename();
            File saveFile3 = new File(projectPath, fileName3);
            file3.transferTo(saveFile3);
            product.setImage_url3("/upload/products/" + fileName3);
        }

        // 3. [DB 저장] 서비스 호출 (기존 코드 유지)
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
                               @RequestParam("file") MultipartFile file, 
                               HttpSession session) throws Exception {
        
        User user = (User) session.getAttribute("user");
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        if (file != null && !file.isEmpty()) {
            // [수정 포인트!] 등록(Write) 때와 똑같이 외부 폴더 경로로 변경하세요.
            // 기존: ... + "/src/main/resources/static/upload/products/"; (X)
            String projectPath = System.getProperty("user.dir") + "/upload/products/"; // (O)
            
            File dir = new File(projectPath);
            if (!dir.exists()) dir.mkdirs();

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            
            product.setImage_url("/upload/products/" + fileName);
        } else {
            // [추가 포인트!] 사진을 새로 올리지 않았을 경우 기존 이미지 경로 유지
            Product existingProduct = productService.getProduct(product.getProduct_id());
            product.setImage_url(existingProduct.getImage_url());
        }
        
        productService.updateProduct(product);
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