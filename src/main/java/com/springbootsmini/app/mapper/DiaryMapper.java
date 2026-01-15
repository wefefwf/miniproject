package com.springbootsmini.app.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.springbootsmini.app.domain.DiaryVo;

@Mapper
public interface DiaryMapper {
    
    // 1. 새 일기 작성 (Summernote HTML 내용 저장)
    void insertDiary(DiaryVo diary);

    // 2. 내 일기 전체 목록 조회 (최신순)
    List<DiaryVo> getDiaryList(String userId);

    // 3. 특정 일기 상세 보기
    DiaryVo getDiaryDetail(int diaryId);

    // 4. 일기 수정 (Summernote 내용 업데이트)
    void updateDiary(DiaryVo diary);

    // 5. 일기 삭제 (본인 확인을 위해 userId 포함)
    void deleteDiary(@Param("diaryId") int diaryId, @Param("userId") String userId);

    // 6. [추가기능] 총 작성한 일기 개수 확인
    int getDiaryCount(String userId);
    
    // 7. [추가기능] 제목 또는 내용으로 일기 검색
    List<DiaryVo> searchDiary(@Param("userId") String userId, @Param("keyword") String keyword);
}