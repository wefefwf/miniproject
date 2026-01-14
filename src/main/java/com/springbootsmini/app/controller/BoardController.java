package com.springbootsmini.app.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springbootsmini.app.domain.Board;
import com.springbootsmini.app.domain.BoardImage;
import com.springbootsmini.app.domain.User;
import com.springbootsmini.app.service.BoardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	

	
	@Autowired
	public BoardService boardService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	//게시글 상세 페이지
	@GetMapping("/board/boardDetail")
	public String boardDetail(	@RequestParam("category") int categoryId,
										@RequestParam("boardId") int boardId, 
										@RequestParam(value ="hashtag",required = false) String hashtag,
										@RequestParam(value = "pageNum")int pageNum, Model model){
		
		Board board = boardService.getBoardDetail(categoryId,boardId,hashtag);
		model.addAttribute("board", board);
	    model.addAttribute("pageNum", pageNum);
	    
	    return "views/board/boardDetail";
	}
	
	//게시글 추가
	@PostMapping("/addBoard")
	public String addBoard(
			@RequestParam("categoryId") int categoryId,
            @RequestParam("writerId") String writerId,
            @RequestParam("pass") String pass,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "birthday", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
            @RequestParam(value = "fileName", required = false) MultipartFile[] fileName,
            @RequestParam(value = "hashtag", required = false) String hashtag
	        ) throws Exception {

		//보드만들기
	    Board board = new Board();
	    board.setCategoryId(categoryId);
	    board.setWriterId(writerId);
	    board.setPass(passwordEncoder.encode(pass));
	    board.setTitle(title);
	    board.setContent(content);
	    board.setAge(age);
	    board.setGender(gender);
	    board.setBirthday(birthday);
	    board.setRegion(region);

	    // 여기서 addBoard, addHashtag 등은 나중 서비스에서 처리
	    // 서비스 호출
	    boardService.addBoard(board, fileName, hashtag);
	    
	    return "redirect:/board?category=" + categoryId;
	}

	
	//write 갈때 유저 값 들고감 
	@GetMapping("/writeBoard")
	public String goWriteBoard(HttpSession session, Model model,
			@RequestParam("category") int categoryId,
			@RequestParam( value = "hashtag", required = false )String hashtag) {
		
		model.addAttribute("loginUser", session.getAttribute("user"));
		model.addAttribute("categoryId",categoryId);
		model.addAttribute("hashtag",hashtag);		
	    return "views/board/writeBoard";
	    }
	
	//게시판 요청이 들어오면 해당 카테고리에 맞는 게시판 내용을 보여줘야 함 board.html 하나로 전부 돌려쓰기 
	//ex) 요청이 th:href="@{/board(category = 4)}"이런식으로 들어옴 
	//여기에 썸네일 이미지도 들고올거임 
	@GetMapping("/board")
	public String boardList
	(@RequestParam("category") int category,
	@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
	@RequestParam( value = "hashtag", required = false )String hashtag,Model model){
		
		//일단 해당 카테고리에 해당하는 게시판을 들고 와야함 
		//해시태그 써야되니까 검색 기능도 추가 해야함 
		
		//기존 boardList호출 - 썸네일 이미지 추가해줘야되서 이렇게 따로부름
		Map<String, Object> boardList = boardService.boardList(category, hashtag, pageNum);
		
		//리스트꺼내기..사진 하나하나 넣어야되서
		List<Board> bList = (List<Board>) boardList.get("bList");
		for (Board board : bList) {
	        BoardImage thumbnail = boardService.getThumbnail(board.getBoardId(), board.getCategoryId(), hashtag);
	        board.setThumbnail(thumbnail);
	    }
		
		//썸네일 넣었으니까 다시 bList boardList에 넣어주기..
		boardList.put("bList",bList);
		
		//이제 model에 값넣기
		model.addAllAttributes(boardList);
		model.addAttribute("category", category);
		
		return "views/board/board";
	}
	
}
