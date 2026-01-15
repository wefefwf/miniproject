package com.springbootsmini.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbootsmini.app.service.BoardService;
import com.springbootsmini.app.service.UserService;

@RestController
public class BoardAjaxController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/recommend.ajax")	
	public Map<String, Integer>  recommend (
			@RequestParam("boardId") int boardId , 
			@RequestParam("type") String type,
			@RequestParam("categoryId") int categoryId,
			@RequestParam(value ="hashtag",required = false) String hashtag
			) {
		
		return boardService.getRecommend(boardId,type,categoryId,hashtag);
	}
}
