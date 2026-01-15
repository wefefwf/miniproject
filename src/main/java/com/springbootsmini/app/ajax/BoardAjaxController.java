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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootsmini.app.domain.BoardReply;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.BoardService;
import com.springbootsmini.app.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class BoardAjaxController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	
	//댓글 추가
	@PostMapping("/board/reply/insert")
	public Object insertReply(
	        @RequestParam("boardId") int boardId,
	        @RequestParam("content") String content,			
	        HttpSession session){
		
			User loginUser = (User) session.getAttribute("loginUser");
		 
		 if(loginUser == null) {
			 Map<String, Object> failResult = new HashMap<>();
		        failResult.put("status", "fail");
		        failResult.put("msg", "댓글은 로그인 이후 작성이 가능합니다.");
		        return failResult;  // List가 아니라 단일 Map 반환
		 }
		 String writerId = loginUser.getId();
		 boardService.insertReply(boardId, writerId, content);
		 return boardService.getReplyList(boardId);
	};
	
	//댓글 업데이트 
	@PostMapping("/board/reply/update")
	public Object updateReply(
			@RequestParam("replyId") int replyId,
	        @RequestParam("boardId") int boardId,
	        @RequestParam("content") String content,			
	        HttpSession session){
		
			User loginUser = (User) session.getAttribute("loginUser");
		 
		 if(loginUser == null) {
			 Map<String, Object> failResult = new HashMap<>();
		        failResult.put("status", "fail");
		        failResult.put("msg", "댓글은 로그인 이후 작성이 가능합니다.");
		        return failResult;  // List가 아니라 단일 Map 반환
		 }
		 String writerId = loginUser.getId();
		 boardService.updateReply(replyId, writerId, content);
		 return boardService.getReplyList(boardId);
	};
	
	
	
	//댓글 삭제하기
	@PostMapping("/board/reply/delete")
	public Object deleteReply(
	        @RequestParam("replyId") int replyId,
	        @RequestParam("boardId") int boardId,
	        HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		 
		 if(loginUser == null) {
			 Map<String, Object> failResult = new HashMap<>();
		        failResult.put("status", "fail");
		        failResult.put("msg", "댓글은 로그인 이후 작성이 가능합니다.");
		        return failResult;  // List가 아니라 단일 Map 반환
		 }
		 String writerId = loginUser.getId();
		
	    boardService.deleteReply(replyId,writerId);
	    
	    // 삭제 후 다시 리스트 반환
	    return boardService.getReplyList(boardId);
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
