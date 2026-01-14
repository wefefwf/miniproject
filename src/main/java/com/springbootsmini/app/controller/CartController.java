package com.springbootsmini.app.controller;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springbootsmini.app.domain.Cart;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.CartService;

import jakarta.servlet.http.HttpSession;


@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/add")
    @ResponseBody
    public String addCart(Cart cart, HttpSession session) {
        // 1. 세션에서 로그인한 유저 정보 가져오기
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "login_required"; // 로그인 안 됐으면 알림
        }
        
        // 2. 장바구니 객체에 유저 아이디 세팅
        cart.setId(user.getId());
        
        // 3. 서비스 호출 (DB에 저장하거나 수량 업데이트)
        cartService.addCart(cart);
        
     // --- 여기만 추가됨: 세션에 최신 장바구니 개수 저장 ---
        List<Cart> cartList = cartService.getCartList(user.getId());
        session.setAttribute("cartCount", cartList.size());
        
        return "success";
    }
    
    @PostMapping("/cart/delete")
    @ResponseBody
    public String deleteCart(@RequestParam("cartNo") int cartNo, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "login_required";
        
        // 본인의 장바구니인지 확인하며 삭제
        cartService.deleteCart(cartNo, user.getId());
        
     // --- 여기만 추가됨: 삭제 후 세션 개수 갱신 ---
        List<Cart> cartList = cartService.getCartList(user.getId());
        session.setAttribute("cartCount", cartList.size());
        
        return "success";
    }
    //장바구니 수량 업데이트
    @PostMapping("/cart/updateQty")
    @ResponseBody
    public String updateQty(@RequestParam("cartNo") int cartNo, @RequestParam("count") int count) {
        cartService.updateQty(cartNo, count);
        return "success";
    }
    
 // 장바구니 목록 조회 (추가)
    @GetMapping("/cart/list")
    public String cartList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // DB에서 해당 유저의 장바구니 리스트를 가져옵니다.
        List<Cart> cartList = cartService.getCartList(user.getId());
        model.addAttribute("cartList", cartList);
        
     // --- 여기만 추가됨: 리스트 조회 시 세션 개수 동기화 ---
        session.setAttribute("cartCount", cartList.size());
        
        return "views/cart/cartList"; // templates/views/cart/cartList.html
    }

    // 선택한 상품들만 결제창으로 넘기기 (추가)
    @PostMapping("/order/form")
    public String showOrderForm(@RequestParam("cartIds") List<Integer> cartIds, 
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // 선택된 cart_id 목록에 해당하는 상품 정보만 가져오기
        List<Cart> selectedItems = cartService.getSelectedCartItems(cartIds);
        
        // 총 결제 금액 계산
        int totalPrice = selectedItems.stream()
                                      .mapToInt(item -> item.getPrice() * item.getCount())
                                      .sum();

        model.addAttribute("orderItems", selectedItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", user);

        return "views/order/orderForm"; // templates/views/order/orderForm.html
    }
}
    
