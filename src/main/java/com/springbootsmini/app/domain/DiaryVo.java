package com.springbootsmini.app.domain;

import java.util.Date; // ← 이 부분이 빠져서 에러가 났던 거예요!

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryVo {
    private int diary_id;      // 일기 고유 번호
    private String user_id;    // 작성자 ID
    private int pet_id;       //내가 여길 추가 안해서 diary에 문제
    private String title;      // 일기 제목
    private String content;    // 일기 내용 (Summernote HTML 내용)
    private Date reg_date;     // 작성일
}