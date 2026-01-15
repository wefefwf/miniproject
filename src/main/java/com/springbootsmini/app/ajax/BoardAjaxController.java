package com.springbootsmini.app.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.service.BoardService;
import com.springbootsmini.app.service.UserService;

@RestController
public class BoardAjaxController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	
	
	
	
	//댓글 삭제하기
	@PostMapping("/board/reply/delete")
	public Map<String, Integer> deleteReply(
	        @RequestParam("replyId") int replyId,
	        @RequestParam("boardId") int boardId,
	        @RequestParam("categoryId") int categoryId,
	        @RequestParam(value="hashtag", required=false) String hashtag) {

	    boardService.deleteReply(replyId);
	    // 삭제 후 다시 리스트 반환
	    return boardService.getRecommend(boardId,categoryId,hashtag);
	}
	
	//댓글 가져오기
	@PostMapping("/recommend.ajax")	
	public Map<String, Integer>  recommend (
			@RequestParam("boardId") int boardId , 
			@RequestParam("type") String type,
			@RequestParam("categoryId") int categoryId,
			@RequestParam(value ="hashtag",required = false) String hashtag
			) {
		boardService.updateRecommend(boardId, type);
		return boardService.getRecommend(boardId,categoryId,hashtag);
	}
}
