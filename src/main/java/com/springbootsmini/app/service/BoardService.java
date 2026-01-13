package com.springbootsmini.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.mapper.BoardMapper;

@Service
public class BoardService {

	@Autowired
	public BoardMapper boardMapper;
	
	//한페이지에 몇개 보여줄건지
	private static final int PAGE_SIZE = 9;
	//페이지네이션 몇개씩 보여줄건지
	private static final int PAGE_GROUP = 10;
	
	 // 한 게시글의 썸네일 가져오기
    public BoardImage getThumbnail(int boardId, int category, String hashtag) {
    	BoardImage boardImage = boardMapper.getThumbnailByBoardId(category,hashtag,boardId);
        return boardImage;
    }
	 
    //한 게시글의 사진 전부 가져오기
    public List<BoardImage> getAllImages(int boardId, int category, String hashtag) {
        List<BoardImage> getAllImages = boardMapper.getAllImagesByBoardId(category,hashtag,boardId);
        return getAllImages;
    }
    
	
	//카테고리에 따라 해당 게시글 가져오는 메서드
	public Map<String,Object>boardList(int category, String hashtag, int pageNum){
		
		//현재 페이지(페이지 네이션)
		int currentPage = pageNum;
		
		//시작 번호
		//1페이지 시작번호 0
		//2페이지 시작번호 9
		int startRow = (currentPage -1 )*PAGE_SIZE;
		
		//해당 게시글 전체 갯수 
		int listCount = boardMapper.getBoardCount(category, hashtag);
		//일단 리스트 받음 9개씩 
		List<Board> bList =boardMapper.boardList(startRow, PAGE_SIZE,category, hashtag);
		
		//1~10 페이지는 시작 페이지가 1 
		//11~20페이지는 시작 페이지가 11
		int startPage =currentPage / PAGE_GROUP * PAGE_GROUP +1 
				-(currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
		int endPage = startPage + PAGE_GROUP -1 ;
		
		//실제 페이지수 
		//63페이진데 10개씩 보여주면 3페이지가 못나오니까
		 int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
		 
		 //근데 또 63페이진데 70까지 나오면 안되니까 페이지 수 제한
		 if(endPage > pageCount) {
			 endPage = pageCount;
		 }
		
		 Map<String, Object> modelMap = new HashMap<>();
		 	modelMap.put("bList", bList);
			modelMap.put("pageCount", pageCount);
			modelMap.put("startPage", startPage);
			modelMap.put("endPage", endPage);
			modelMap.put("listCount", listCount);
			modelMap.put("pageGroup", PAGE_GROUP);
			modelMap.put("currentPage", currentPage);
		 
			//해시태그가 있는지 true면 있는거 false면 없는거 
			boolean isHashtagBoard = (hashtag != null && !hashtag.isEmpty());
			
			if(isHashtagBoard){
				modelMap.put("hashtag",hashtag);
			} else {
			    modelMap.put("hashtag", ""); // 안전하게 빈 문자열로
			}
			
			modelMap.put("isHashtagBoard", isHashtagBoard);
			
		return modelMap;
	}
}
