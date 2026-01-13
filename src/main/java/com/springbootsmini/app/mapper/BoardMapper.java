package com.springbootsmini.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardImage;

@Mapper
public interface BoardMapper {
	
	
	//해당 게시글의 이미지 한 개  가져오기 (썸네일용)
	public BoardImage getThumbnailByBoardId(@Param("category") int category,@Param("hashtag")String hashtag,@Param("boardId") int boardId);
	
	//해당 게시글의 이미지 전부 가져오기 (상세보기용)
	public List<BoardImage> getAllImagesByBoardId(@Param("category") int category,@Param("hashtag")String hashtag,@Param("boardId") int boardId);
	
	//해당 카테고리에 맞는 게시글 리스트 반환 메서드
	//카테고리랑 있으면 (hash 태그가 map형태로 들어옴  
	public List<Board> boardList
	(@Param("startRow") int startRow, @Param("num") int num,@Param("category") int category,@Param("hashtag")String hashtag);
	
	//해당 카테고리 글 전체 갯수 가져오는 메서드
	public int getBoardCount(@Param("category") int category,@Param("hashtag") String hashtag);
}
