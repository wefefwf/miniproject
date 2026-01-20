package com.springbootsmini.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardHashtag;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.Hashtag;

@Mapper
public interface BoardMapper {
	//게시물 댓글 추가
	public void updateReply(@Param("replyId")int replyId,@Param("writerId")String writerId,@Param("content")String content);

	//게시물 댓글 추가
	public void insertReply(@Param("boardId")int boardId,@Param("writerId")String writerId,@Param("content")String content);
	
	//게시물 댓글 삭제
	public void deleteReply(@Param("replyId")int replyId,@Param("writerId")String writerId);
	
	//게시물의 댓글 리스트 가져오기
	public List<BoardReply>getReply(@Param("boardId")int boardId);
	
	//추천 좋아요 업데이트
	public void updateRecommend(@Param("boardId")int boardId,@Param("type") String type);
	
	//보드 업데이트
	public void updateBoard(@Param("board")Board board);
	
	//해시태그-보드 연결 삭제(업데이트 시 사용)
	public void deleteBoardHashtag(@Param("boardId")int boardId);
	
	//보드 이미지 삭제(업데이트 시 사용)
	public void deleteBoardImages(@Param("boardId")int boardId);
	//게시글 삭제
	public void deleteBoard(@Param("boardId")int boardId);
	
	//게시글에 해당하는 해시태그 가져오기
	public List<Hashtag> getHashtag(@Param("boardId")int boardId);
	
	//해당 boardId에 해당하는 게시물 가져오기
	public Board getBoardDetail(@Param("boardId")int boardId,@Param("categoryId") int categoryId,@Param("hashtag") String hashtag);
	
	//해시태그보드 연결 추가
	public void addBoardHashtag(@Param("boardId")int boardId,@Param("hashtagId")int hashtagId);
	
	//해시태그 없으면 추가하기
	public void addHashtag(String hashtag);
	
	//해시태그 있는지 가져오기 
	public Integer getHashtagId(String hashtag);
	
	//boardImage에 추가
	public void addBoardImage(BoardImage boardImage);
	
	//board에 추가 
	public void addBoard(Board board);
	
	//해당 게시글의 이미지 한 개  가져오기 (썸네일용)
	public BoardImage getThumbnailByBoardId(Map<String,Object> param);
	
	//해당 게시글의 이미지 전부 가져오기 (상세보기용)
	public List<BoardImage> getAllImagesByBoardId(@Param("boardId")int boardId);
	
	//해당 카테고리에 맞는 게시글 리스트 반환 메서드
	//카테고리랑 있으면 (hash 태그가 map형태로 들어옴  
	public List<Board> boardList
	(@Param("startRow") int startRow, @Param("num") int num,@Param("category") int category,@Param("hashtag")String hashtag);
	
	//해당 카테고리 글 전체 갯수 가져오는 메서드
	public int getBoardCount(@Param("category") int category,@Param("hashtag") String hashtag);
}
