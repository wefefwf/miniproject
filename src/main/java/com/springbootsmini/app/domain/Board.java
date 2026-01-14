package com.springbootsmini.app.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	
	// 게시글 고유 번호 (PK)
    private int boardId;
    // 게시판 카테고리 (1:꿀팁, 2:자랑, 3:실종, 4:입양)
    private int categoryId;
    // 작성자 아이디 (user 테이블 id, FK)
    private String writerId;
    // 게시글 제목
    private String title;
    //게시글 비밀번호
    private String pass;
    // 게시글 내용
    private String content;
    // 여기부터 선택사항---------
    // 나이 (입양/실종 게시판에서 사용, NULL 가능)
    private Integer age;
    // 성별
    private String gender;
    // 생일
    private Date birthday;
    // 지역
    private String region;
    //여기까지 선택사항 ---------
    // 추천 수
    private int recommend;
    // 감사 수
    private int thank;
    // 작성일
    private Timestamp createdAt;
	//테이블 건들기 x 여기서 썸네일 이미지 추가 
    private BoardImage thumbnail;
  //테이블 건들기 x 여기서 전체 이미지 추가 
    private List<BoardImage> images;
    
}
