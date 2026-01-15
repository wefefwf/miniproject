package com.springbootsmini.app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.springbootsmini.app.domain.DiaryVo;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.DiaryService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/diary") // 모든 주소를 /diary로 시작하게 하여 중복 방지
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 1. 일기 쓰기 페이지 이동
    @GetMapping("/write")
    public String diaryWriteForm(HttpSession session) {
        // 로그인 체크 (선택사항이지만 권장)
        if (session.getAttribute("user") == null) return "redirect:/loginForm";
        return "views/diary/diaryWrite";
    }

    // 2. 일기 저장 로직
    @PostMapping("/save")
    public String saveDiary(DiaryVo diary, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // 작성자 아이디 세팅
        diary.setUser_id(user.getId());
        
        // DB 저장
        diaryService.insertDiary(diary);

        // 저장 후 목록 페이지로 이동
        return "redirect:/diary/list";
    }

    // 3. 일기 목록 페이지 이동
    @GetMapping("/list")
    public String diaryList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        List<DiaryVo> list = diaryService.getDiaryList(user.getId());
        model.addAttribute("diaryList", list);

        return "views/diary/diaryList";
    }
}