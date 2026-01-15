package com.springbootsmini.app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.OrderService; // 서비스 임포트 확인
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService; 

    @GetMapping("/order/success")
    public String orderSuccess(@RequestParam("merchant_uid") String merchantUid,
                               @RequestParam(value = "cartIds", required = false) List<Integer> cartIds,
                               HttpSession session, Model model) {
        
        // 1. 세션에서 유저 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        try {
            // 2. 핵심 로직 실행: DB 주문 저장 및 장바구니 비우기
            // (cartIds가 필요하므로 JS의 location.href 부분에 쿼리스트링으로 추가되어야 합니다)
            orderService.completeOrder(user.getId(), merchantUid, cartIds);
            
            // 3. 화면에 보여줄 데이터 담기
            model.addAttribute("orderNo", merchantUid);
            model.addAttribute("message", "결제가 성공적으로 완료되었습니다!");
            
        } catch (Exception e) {
            // 에러 발생 시 처리
            model.addAttribute("message", "주문 처리 중 오류가 발생했습니다.");
            return "views/order/orderError"; 
        }

        return "views/order/orderSuccess"; 
    }
}