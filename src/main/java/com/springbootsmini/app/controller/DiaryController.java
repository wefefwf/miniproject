package com.springbootsmini.app.controller;

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
            model.addAttribute("petImage", "/upload/pet/" + lastPetImage);
            
            // 3-4. 결정된 강아지의 일기만 필터링
            List<DiaryVo> allList = diaryService.getDiaryList(user.getId());
            
            // DiaryVo에 pet_id 필드를 추가했다면 정상적으로 필터링됩니다.
            List<DiaryVo> filteredList = allList.stream()
                    .filter(d -> d.getPet_id() == currentPet.getPetId()) 
                    .collect(Collectors.toList());
            
            model.addAttribute("diaryList", filteredList);
        } else {
            model.addAttribute("diaryList", List.of());
        }

        return "views/diary/diaryList";
    }

    // 4. 일기 삭제 기능
    @GetMapping("/delete")
    public String deleteDiary(@RequestParam("diary_id") int diaryId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/loginForm";

        diaryService.deleteDiary(diaryId, user.getId()); 
        
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

        if (petList != null && !petList.isEmpty()) {
            // 상세 정보용 강아지 찾기 (람다식 변수 제약 해결을 위해 final 사용)
            final int diaryPetId = diary.getPet_id(); 
            
            Pet diaryPet = petList.stream()
                    .filter(p -> p.getPetId() == diaryPetId)
                    .findFirst()
                    .orElse(petList.get(0));

            String lastPetImage = petService.getLastPetImage(diaryPet.getPetId());
            model.addAttribute("petName", diaryPet.getName()); 
            model.addAttribute("petImage", "/upload/pet/" + lastPetImage);
        }
        
        return "views/diary/diaryDetail";
    }
}