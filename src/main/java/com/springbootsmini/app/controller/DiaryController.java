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
import com.springbootsmini.app.domain.Pet;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.DiaryService;
import com.springbootsmini.app.service.PetService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private PetService petService;

    // 1. 일기 쓰기 페이지
    @GetMapping("/write")
    public String diaryWriteForm(HttpSession session,
                                @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        if (session.getAttribute("user") == null) {
            if (redirectUrl == null || redirectUrl.isEmpty()) redirectUrl = "/diary/write";
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }
        return "views/diary/diaryWrite";
    }

    // 2. 일기 저장 로직
    @PostMapping("/save")
    public String saveDiary(DiaryVo diary, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";
        
        diary.setUser_id(user.getId());
        diaryService.insertDiary(diary);
        
        return "redirect:/diary/list";
    }

    // 3. 일기 목록 페이지 (중복 메서드 에러 해결을 위해 하나만 남김)
    @GetMapping("/list")
    public String diaryList(HttpSession session, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            if (redirectUrl == null || redirectUrl.isEmpty()) {
                redirectUrl = "/diary/list"; 
            }
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }

        // 3-1. 다이어리 목록 가져오기
        List<DiaryVo> list = diaryService.getDiaryList(user.getId());
        model.addAttribute("diaryList", list);

        // 3-2. 반려동물 사진 가져오기 (에러 수정: getName() 사용)
        List<Pet> petList = petService.getPetList(user.getId());
        if (petList != null && !petList.isEmpty()) {
            Pet firstPet = petList.get(0);
            String lastPetImage = petService.getLastPetImage(firstPet.getPetId());
            
            // 이미지 에러 수정: 도메인에 정의된 getName() 또는 getPetName() 확인 필요
            // 보내주신 이미지의 퀵픽스를 보면 getName()이 실제 메서드 이름일 확률이 높습니다.
            model.addAttribute("petName", firstPet.getName()); 
            model.addAttribute("petImage", "/upload/pet/" + lastPetImage);
        }

        return "views/diary/diaryList";
    }

    // 4. 일기 삭제 기능 (인자 2개 불일치 에러 해결)
    @GetMapping("/delete")
    public String deleteDiary(@RequestParam("diary_id") int diaryId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // 서비스 정의에 맞춰 ID와 유저 아이디를 함께 보냄
        diaryService.deleteDiary(diaryId, user.getId()); 
        
        return "redirect:/diary/list";
    }

    // 5. 일기 상세 보기 (메서드명 불일치 에러 해결)
    @GetMapping("/detail")
    public String diaryDetail(@RequestParam("diary_id") int diaryId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // getDiaryById 대신 실제 존재하는 getDiaryDetail 사용
        DiaryVo diary = diaryService.getDiaryDetail(diaryId); 
        model.addAttribute("diary", diary);
        
        return "views/diary/diaryDetail";
    }
}