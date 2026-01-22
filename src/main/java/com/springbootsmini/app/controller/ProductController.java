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

 // 1. 상품 목록 페이지 (카테고리별 + 검색 기능 추가)
    @GetMapping("/productList")
    public String productList(@RequestParam(value="category_id", defaultValue="1") int category_id, 
                             @RequestParam(value="keyword", required=false) String keyword, // [추가] 검색어 파라미터
                             Model model) {
        // 1. 현재 카테고리와 검색어에 맞는 상품들 가져오기 (keyword 인자 추가)
        List<Product> pList = productService.getProductList(category_id, keyword);
        
        // 2. 상단 메뉴에 뿌릴 카테고리 전체 목록 가져오기 (추가)
        List<Category> cList = productService.getCategoryList(); 
        
        model.addAttribute("pList", pList);
        model.addAttribute("cList", cList); // HTML에서 메뉴를 만들 때 사용
        model.addAttribute("keyword", keyword); // [추가] 검색창에 입력한 검색어 유지를 위해 전달
        
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
 // 6. 상품 수정 실행 (Update - POST) - 이 부분 전체를 덮어쓰기 하세요!
    @PostMapping("/productUpdate")
    public String productUpdate(Product product, 
                               @RequestParam(value="file1", required=false) MultipartFile file1, 
                               @RequestParam(value="file2", required=false) MultipartFile file2,
                               @RequestParam(value="file3", required=false) MultipartFile file3,
                               HttpSession session) throws Exception {
        
        // 1. 관리자 권한 체크
        User user = (User) session.getAttribute("user");
        if (user == null || user.getManager() != 1) {
            return "redirect:/productList";
        }

        // 2. 파일 저장 경로 설정 (외부 폴더)
        String projectPath = System.getProperty("user.dir") + "/upload/products/";
        File dir = new File(projectPath);
        if (!dir.exists()) dir.mkdirs();

        // 3. 기존 데이터 미리 가져오기 (사진을 안 바꾼 항목은 기존 경로를 유지하기 위함)
        Product existingProduct = productService.getProduct(product.getProduct_id());

        // --- [파일 1: 메인 이미지 처리] ---
        if (file1 != null && !file1.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file1.getOriginalFilename();
            file1.transferTo(new File(projectPath, fileName));
            product.setImage_url("/upload/products/" + fileName);
        } else {
            // 새 파일을 안 올렸으면 DB에 있던 기존 경로 그대로 세팅
            product.setImage_url(existingProduct.getImage_url()); 
        }

        // --- [파일 2: 상세 이미지 1 처리] ---
        if (file2 != null && !file2.isEmpty()) {
            UUID uuid2 = UUID.randomUUID();
            String fileName2 = uuid2 + "_" + file2.getOriginalFilename();
            file2.transferTo(new File(projectPath, fileName2));
            product.setImage_url2("/upload/products/" + fileName2);
        } else {
            product.setImage_url2(existingProduct.getImage_url2());
        }

        // --- [파일 3: 상세 이미지 2 처리] ---
        if (file3 != null && !file3.isEmpty()) {
            UUID uuid3 = UUID.randomUUID();
            String fileName3 = uuid3 + "_" + file3.getOriginalFilename();
            file3.transferTo(new File(projectPath, fileName3));
            product.setImage_url3("/upload/products/" + fileName3);
        } else {
            product.setImage_url3(existingProduct.getImage_url3());
        }

        // 4. DB 업데이트 실행
        productService.updateProduct(product);
        
        // 5. 수정한 상품의 상세 페이지로 이동
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