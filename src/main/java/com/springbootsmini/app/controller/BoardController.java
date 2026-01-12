package com.springbootsmini.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springbootsmini.app.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	public BoardService boardService;
	
	
	//게시판 요청이 들어오면 해당 카테고리에 맞는 게시판 내용을 보여줘야 함 board.html 하나로 전부 돌려쓰기 
	//ex) 요청이 th:href="@{/board(category = 4)}"이런식으로 들어옴 
	@GetMapping("/board")
	public String boardList
	(@RequestParam("category") int category,
	@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
	@RequestParam( value = "hashtag", defaultValue = "0")int hashtag,Model model){
		
		//일단 해당 카테고리에 해당하는 게시판을 들고 와야함 
		//해시태그 써야되니까 검색 기능도 추가 해야함 
		model.addAllAttributes(boardService.boardList(category, hashtag, pageNum));
		model.addAttribute("category", category);
		
		return "views/board";
	}
	
}
