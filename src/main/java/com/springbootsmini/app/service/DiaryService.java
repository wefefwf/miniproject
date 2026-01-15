package com.springbootsmini.app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.DiaryVo;
import com.springbootsmini.app.mapper.DiaryMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    // 1. 일기 저장 (Summernote의 HTML 내용이 content로 들어옴)
    public void insertDiary(DiaryVo diary) {
        log.info("일기 저장 시작: {}", diary.getTitle());
        diaryMapper.insertDiary(diary);
    }

    // 2. 내 일기 목록 가져오기
    public List<DiaryVo> getDiaryList(String userId) {
        log.info("사용자 {}의 일기 목록 조회", userId);
        return diaryMapper.getDiaryList(userId);
    }

    // 3. 일기 상세 보기 (수정 시 필요)
    public DiaryVo getDiaryDetail(int diaryId) {
        return diaryMapper.getDiaryDetail(diaryId);
    }

    // 4. 일기 수정
    public void updateDiary(DiaryVo diary) {
        log.info("일기 수정: {}", diary.getDiary_id());
        diaryMapper.updateDiary(diary);
    }

    // 5. 일기 삭제
    public void deleteDiary(int diaryId, String userId) {
        log.info("일기 삭제: ID {}", diaryId);
        diaryMapper.deleteDiary(diaryId, userId);
    }
}