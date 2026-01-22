package com.springbootsmini.app.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springbootsmini.app.domain.Cart;
import com.springbootsmini.app.domain.Product;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.CartService;
import com.springbootsmini.app.service.ProductService; // [추가됨] 이 줄이 없어서 에러

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService; // 이제 에러 없이 정상적으로 주입됩니다.

    @PostMapping("/cart/add")
    @ResponseBody
    public String addCart(Cart cart, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "login_required";
        
        cart.setId(user.getId());
        cartService.addCart(cart);
        
        List<Cart> cartList = cartService.getCartList(user.getId());
         // 수정 전
			/* session.setAttribute("cartCount", cartList.size()); */
        // 수정 후
        session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
        
        return "success";
    }
    
    @PostMapping("/cart/delete")
    @ResponseBody
    public String deleteCart(@RequestParam("cartNo") int cartNo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "login_required";
        
        cartService.deleteCart(cartNo, user.getId());
        
        List<Cart> cartList = cartService.getCartList(user.getId());
		/*
		 * // 수정 전 session.setAttribute("cartCount", cartList.size());
		 */
        // 수정 후
        session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
        
        return "success";
    }

    @PostMapping("/cart/updateQty")
    @ResponseBody
    public String updateQty(@RequestParam("cartNo") int cartNo, 
                           @RequestParam("count") int count, 
                           HttpSession session) { // HttpSession 추가
        
        cartService.updateQty(cartNo, count);
        
        // [추가] 수량이 변경되었으니 세션의 총 개수도 다시 계산해서 업데이트!
        User user = (User) session.getAttribute("user");
        if (user != null) {
            session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
        }
        
        return "success";
    }
    
    @GetMapping("/cart/list")
    public String cartList(HttpSession session, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        User user = (User) session.getAttribute("user");
        
        // 이 부분만 우리가 원하는 대로 작동하도록 수정!
        if (user == null) {
            if (redirectUrl == null || redirectUrl.isEmpty()) {
                redirectUrl = "/cart/list"; // 로그인 후 돌아올 목적지 설정
            }
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }

        List<Cart> cartList = cartService.getCartList(user.getId());
        model.addAttribute("cartList", cartList);
		/*
		 * // 수정 전 session.setAttribute("cartCount", cartList.size());
		 */
        // 수정 후
        session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
        
        return "views/cart/cartList";
    }

    @PostMapping("/order/form")
    public String showOrderForm(@RequestParam("cartIds") List<Integer> cartIds, 
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        List<Cart> selectedItems = cartService.getSelectedCartItems(cartIds);
        int totalPrice = selectedItems.stream()
                                      .mapToInt(item -> item.getPrice() * item.getCount())
                                      .sum();

        model.addAttribute("orderItems", selectedItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", user);

        return "views/order/orderForm";
    }
    
    @PostMapping("/order/direct")
    public String directOrder(@RequestParam("product_id") int product_id, 
                             @RequestParam("count") int count, 
                             HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // [수정] 로그인이 없으면 상품 상세 페이지 주소를 redirectUrl로 담아서 보냄
        if (user == null) {
            // 다시 돌아올 주소: 상품 상세 페이지 (ID 포함)
            String redirectUrl = "/productDetail?product_id=" + product_id;
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }

        // 1. 상품 정보 가져오기 (기존 로직 동일)
        Product product = productService.getProduct(product_id);
        
        // 2. 바로구매용 임시 Cart 객체 생성 (기존 로직 동일)
        Cart directItem = new Cart();
        directItem.setCartNo(0); 
        directItem.setProduct_id(product_id);
        directItem.setProduct_name(product.getProduct_name()); 
        directItem.setPrice(product.getPrice());
        directItem.setCount(count);
        directItem.setImage_url(product.getImage_url());

        List<Cart> items = new ArrayList<>();
        items.add(directItem);

        model.addAttribute("orderItems", items);
        model.addAttribute("totalPrice", product.getPrice() * count);
        model.addAttribute("user", user);

        return "views/order/orderForm"; 
    }
}