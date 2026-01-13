package com.springbootsmini.app.controller;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
        
        return "success";
    }
}