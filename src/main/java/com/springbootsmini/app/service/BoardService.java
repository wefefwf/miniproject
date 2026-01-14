package com.springbootsmini.app.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
	//파일 저장 경로
	private static String uploadPath = "src/main/resources/static/upload/board/";
	
	@Autowired
	private SqlSession sqlSession;
	
	
	//게시글 상세 페이지 
	public Board getBoardDetail(int categoryId,int boardId,String hashtag){
		
		//게시글 하나만 가져오는 메서드
		Board board = boardMapper.getBoardDetail(boardId, categoryId, hashtag);
		board.setImages(boardMapper.getAllImagesByBoardId(boardId));
		//가져와서 이미지 넣어줘야 함
		return board;
	};
	
	//게시글 추가
	public void addBoard(Board board, MultipartFile[] files, String hashtag) throws Exception {
	    // 1. 게시글 insert
	    boardMapper.addBoard(board); // boardId 자동 채워짐

	    // 2. 해시태그 처리
	    if(hashtag != null && !hashtag.isEmpty()) {
	    	//일단 해시태그 있는지 가져옴 
	        int hashtagId = boardMapper.getHashtagId(hashtag);
	        //해시태그 없으면 해시태그 추가
	        if(hashtagId == 0) {
	            boardMapper.addHashtag(hashtag);
	            //다시 해시태그 가져오기
	            hashtagId = boardMapper.getHashtagId(hashtag);
	        }
	        //보드 아이디 useGeneratekey써서 불러올 수 있음
	        boardMapper.addBoardHashtag(board.getBoardId(), hashtagId);
	    }

	    // 3. 파일 처리
	    if(files != null && files.length > 0) {
	        for(MultipartFile multipartFile : files) {
	            if(multipartFile != null && !multipartFile.isEmpty()) {
	                File parent = new File(uploadPath);
	                if(!parent.exists()) parent.mkdirs();

	                UUID uid = UUID.randomUUID();
	                String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
	                String saveName = uid.toString() + "." + extension;

	                File file = new File(parent.getAbsolutePath(), saveName);
	                //파일 저장
	                multipartFile.transferTo(file);

	                //xml에서 받는 값이 boardImage
	                BoardImage boardImage = new BoardImage();
	                boardImage.setBoardId(board.getBoardId());
	                boardImage.setFileName(saveName);
	                boardMapper.addBoardImage(boardImage);
	            }
	        }
	    }
	    sqlSession.flushStatements();
	}

	// 한 게시글의 썸네일 가져오기
	public BoardImage getThumbnail(int boardId, int category, String hashtag) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("boardId", boardId);       // 반드시 boardId
	    param.put("category", category);     // 반드시 category
	    param.put("hashtag", hashtag);       // hashtag
	    return boardMapper.getThumbnailByBoardId(param);
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
