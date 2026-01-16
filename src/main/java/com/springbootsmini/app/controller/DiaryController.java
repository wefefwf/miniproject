package com.springbootsmini.app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springbootsmini.app.domain.DiaryVo;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.DiaryService;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 1. 일기 쓰기 페이지 (여기에도 적용 가능)
    @GetMapping("/write")
    public String diaryWriteForm(HttpSession session,
                                @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        if (session.getAttribute("user") == null) {
            // 주소가 비어있으면 글쓰기 폼으로 돌아오도록 설정
            if (redirectUrl == null || redirectUrl.isEmpty()) redirectUrl = "/diary/write";
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }
        return "views/diary/diaryWrite";
    }

    // 2. 일기 저장 로직 (저장은 그대로 둡니다)
    @PostMapping("/save")
    public String saveDiary(DiaryVo diary, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";
        diary.setUser_id(user.getId());
        diaryService.insertDiary(diary);
        return "redirect:/diary/list";
    }

    // 3. 일기 목록 페이지 (질문하신 핵심 부분!)
    @GetMapping("/list")
    public String diaryList(HttpSession session, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        
        User user = (User) session.getAttribute("user");
        
        // [이전 코드] if (user == null) return "redirect:/loginForm"; 
        // [수정 코드] 목적지를 들고 로그인 페이지로 이동!
        if (user == null) {
            if (redirectUrl == null || redirectUrl.isEmpty()) {
                redirectUrl = "/diary/list"; // 돌아올 주소 확정
            }
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }

        List<DiaryVo> list = diaryService.getDiaryList(user.getId());
        model.addAttribute("diaryList", list);
        return "views/diary/diaryList";
    }
}