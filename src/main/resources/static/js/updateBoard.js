$(function(){
	
	//내용물 다 채워졌는지 확인 
	$(document).on("click","#boardUpdateBtn",function(){
		
				if($("#pass").val().length <= 0) {
					alert("비밀번호를 입력해 주세요");
					$("#pass").focus();
					return false;
				}
				/*비번 만들기*/
				let pass = $("#pass").val();
				
				if($("#title").val().length <= 0) {
									alert("제목을 입력해 주세요");
									$("#title").focus();
									return false;
								}
				
				if($("#content").val().length <= 0) {
								alert("내용을 입력해 주세요");
								$("#content").focus();
								return false;
							}
				
				//카테고리 값에 따라 let해야되는게 달라서 받아오기
				let category = $("#categoryId").val();
				
				//실종 게시판이면
				if(category ==="3"){
					
					if($("#age").val().length <= 0) {
										alert("나이를 입력해 주세요");
										$("#age").focus();
										return false;
									}
				
					if($("#gender").val().length <= 0) {
						alert("성별을 입력해 주세요");
						$("#gender").focus();
						return false;
					}		
					
					if($("#region").val().length <= 0) {
						alert("실종 지역을 입력해 주세요");
						$("#region").focus();
						return false;
					}

				}
				//입양게시판 이면
				if(category ==="4"){
									
					if($("#age").val().length <= 0) {
										alert("나이를 입력해 주세요");
										$("#age").focus();
										return false;
									}
			
					if($("#gender").val().length <= 0) {
						alert("성별을 입력해 주세요");
						$("#gender").focus();
						return false;
					}		
					//해시태그는 null일수 있음
					
					if($("#birthday").val().length <= 0) {
						alert("생년 월일을 입력해 주세요");
						$("#birthday").focus();
						return false;
					}
				}
				
				//이미지는 있을 수도 없을 수도 
			
			//다 들어왔다면 강제 설정 후 강제 커밋 시킬듯
			$("#rPass").val(pass);
			$("#updateBoardForm").attr("action", "/updateBoard");
			$("#updateBoardForm").attr("method", "post");
			$("#updateBoardForm").submit();
		
		
	});
	
	
	
	
	
	
	
	
	
	
});