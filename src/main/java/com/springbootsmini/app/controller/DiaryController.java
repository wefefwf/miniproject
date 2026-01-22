package com.springbootsmini.app.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    public String saveDiary(DiaryVo diary, HttpSession session, 
                           @RequestParam("pet_id") int petId) { // ✅ 어떤 강아지인지 ID를 파라미터로 받음
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";
        
        diary.setUser_id(user.getId());
        diary.setPet_id(petId); // ✅ DiaryVo에 pet_id를 세팅 (이게 있어야 저장이 됨)
        
        diaryService.insertDiary(diary);
        
        // 저장이 완료되면 방금 쓴 강아지의 리스트로 이동하는 것이 편리합니다.
        return "redirect:/diary/list?pet_id=" + petId;
    }

    // 3. 일기 목록 페이지
    @GetMapping("/list")
    public String diaryList(HttpSession session, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                           @RequestParam(value = "pet_id", required = false) Integer petId) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (redirectUrl == null || redirectUrl.isEmpty()) redirectUrl = "/diary/list"; 
            return "redirect:/loginForm?redirectUrl=" + redirectUrl;
        }

        // 3-1. 반려동물 전체 목록 가져오기
        List<Pet> petList = petService.getPetList(user.getId());
        model.addAttribute("petList", petList);

        // 3-2. 현재 보여줄 강아지 결정
        Pet selectedPet = null;
        if (petList != null && !petList.isEmpty()) {
            if (petId != null) {
                selectedPet = petList.stream()
                        .filter(p -> p.getPetId() == petId)
                        .findFirst()
                        .orElse(petList.get(0));
            } else {
                selectedPet = petList.get(0);
            }
        }

        // ✅ 람다식(filter) 내에서 사용하기 위해 final 변수로 선언 (에러 해결 포인트)
        final Pet currentPet = selectedPet;

        // 3-3. 결정된 강아지의 정보 모델에 담기
        if (currentPet != null) {
            String lastPetImage = petService.getLastPetImage(currentPet.getPetId());
            model.addAttribute("petName", currentPet.getName()); 
            
            if (lastPetImage != null && !lastPetImage.isEmpty()) {
                model.addAttribute("petImage", "/upload/pet/" + lastPetImage);
            } else {
                model.addAttribute("petImage", null); // 사진 데이터 없으면 null
            }
            
            // 3-4. 결정된 강아지의 일기 필터링 + 인덱스 순 정렬 (수정: d1, d2 순서로 정렬)
            List<DiaryVo> allList = diaryService.getDiaryList(user.getId());
            
            List<DiaryVo> filteredList = allList.stream()
                    .filter(d -> d.getPet_id() == currentPet.getPetId()) 
                    .sorted(Comparator.comparingInt(DiaryVo::getDiary_id)) // 인덱스(ID) 오름차순 정렬
                    .collect(Collectors.toList());
            
            model.addAttribute("diaryList", filteredList);
        } else {
            // 펫이 아예 없을 때
            model.addAttribute("petName", null);
            model.addAttribute("petImage", null);
            model.addAttribute("diaryList", List.of());
        }

        return "views/diary/diaryList";
    }

 // 4. 일기 삭제 기능 (수정됨)
    @GetMapping("/delete")
    public String deleteDiary(@RequestParam("diary_id") int diaryId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        // 1. 삭제하기 전에 해당 일기의 pet_id를 미리 알아둡니다.
        DiaryVo diary = diaryService.getDiaryDetail(diaryId);
        int petId = 0;
        if (diary != null) {
            petId = diary.getPet_id();
        }

        // 2. 일기 삭제 실행
        diaryService.deleteDiary(diaryId, user.getId()); 
        
        // 3. 삭제 후 원래 보던 강아지(pet_id)의 리스트로 리다이렉트
        if (petId != 0) {
            return "redirect:/diary/list?pet_id=" + petId;
        }
        
        return "redirect:/diary/list";
    }

 // 5. 일기 상세 보기
    @GetMapping("/detail")
    public String diaryDetail(@RequestParam("diary_id") int diaryId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        DiaryVo diary = diaryService.getDiaryDetail(diaryId); 
        model.addAttribute("diary", diary);

        List<Pet> petList = petService.getPetList(user.getId());
        model.addAttribute("petList", petList); 

        // 펫 리스트가 있고, 현재 일기에 등록된 pet_id가 있을 때만 펫 정보를 담음
        if (petList != null && !petList.isEmpty() && diary.getPet_id() != 0) {
            final int diaryPetId = diary.getPet_id(); 
            
            Pet diaryPet = petList.stream()
                    .filter(p -> p.getPetId() == diaryPetId)
                    .findFirst()
                    .orElse(null); // 👈 중요: 찾지 못하면 null을 반환하게 변경

            if (diaryPet != null) {
                String lastPetImage = petService.getLastPetImage(diaryPet.getPetId());
                model.addAttribute("petName", diaryPet.getName()); 
                
                // 이미지 파일명이 실제로 존재할 때만 경로를 생성
                if (lastPetImage != null && !lastPetImage.isEmpty()) {
                    model.addAttribute("petImage", "/upload/pet/" + lastPetImage);
                } else {
                    model.addAttribute("petImage", null); // 이미지가 없으면 null 전송
                }
            }
        } else {
            // 펫이 아예 없는 경우 모델 속성을 명시적으로 제거하거나 null 세팅
            model.addAttribute("petName", null);
            model.addAttribute("petImage", null);
        }
        
        return "views/diary/diaryDetail";
    }
}