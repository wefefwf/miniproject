$(function(){
	//----------------------
	//댓글 추가의 기본버튼은 insert
	let replyMode = "insert"; // 기본은 추가

	$(document).on("click", ".modifyReply", function(){
	    replyMode = "update";

	    let $replyRow = $(this).closest(".replyRow");
	    let replyId = $replyRow.find(".deleteReply").data("replyid"); // 삭제 버튼에서 가져오기
	    let replyContent = $replyRow.find("pre").text();

	    // 히든과 textarea 채우기
	    $("#replyWriteForm input[name='replyId']").val(replyId);
	    $("#replyContent").val(replyContent);
	    $("#replyWriteButton").val("댓글수정");

	    // 폼 위치 이동
	    if($("#replyForm").is(":visible")){
	        let $next = $replyRow.next();
	        if(!$next.is("#replyForm")){
	            $("#replyForm").slideUp(300, function(){
	                $("#replyForm").insertAfter($replyRow).slideDown(300);
	            });
	        }
	    } else {
	        $("#replyForm").insertAfter($replyRow).removeClass("d-none")
	                       .css("display", "none")
	                       .slideDown(300);
	    }
	});
	
	
	//--------------------
	
	// 댓글 추가/수정 submit
	$(document).on("submit", "#replyWriteForm", function(e){
	    e.preventDefault();

	    if($("#replyContent").val().length < 5) {
	        alert("댓글은 5자 이상 입력해 주세요");
	        return false;
	    }

	    $("#replyForm").slideUp(300);

	    let boardId  = $("#boardId").val();
	    let categoryId = $("#categoryId").val();
	    let replyId = $("#replyWriteForm input[name='replyId']").val() || null; // 이거 중요
	    let url = replyId ? "/board/reply/update" : "/board/reply/insert";

	    $.ajax({
	        url: url,
	        type: "POST",
	        data: { replyId: replyId, boardId: boardId, categoryId: categoryId, content: $("#replyContent").val() },
	        dataType: "json",
	        success: function(data){
	            if(data.status === "fail"){
	                alert(data.msg);
	                return;
	            } else {
	                location.reload();
	            }
	        }
	    });
	});


	
	//댓글 삭제 눌리면 
	  $(document).on("click", ".deleteReply", function(){

	    $("#replyForm").slideUp(300);
	    $("#replyContent").val("");

	    let replyId  = $(this).data("replyid");
	    let boardId  = $("#boardId").val();
	    let ok = confirm( " 댓글을 삭제하시겠습니까?");
	    if (!ok) return;

	    $.ajax({
	      url: "/board/reply/delete",
	      type: "POST",
	      data: {  replyId: replyId,
		        	 boardId: boardId},
	      dataType: "json",
	      success: function(data){
			//걍 서버 재시작때리기
			location.reload();
	      },
	      error: function(){
	        alert("삭제 실패");
	      }
	    });
	  });

	    // 댓글쓰기 버튼
	    $(document).on("click", "#replyWrite", function(){    
	        if(!$("#replyForm").is(":visible")) {
	            $("#replyForm").removeClass("d-none")
	                           .css("display","none")
	                           .insertBefore("#replyList")
	                           .slideDown(300);
			
				//css너비 맞추기용
			   $("#replyForm").css("width", $("#replyList").width());
			   $("#replyForm").css("margin", "0 auto");
			   
			   // 내용 초기화
	            $("#replyContent").val(""); 
	            $("#replyWriteButton").val("댓글쓰기");
	        } else {
	            var prev = $("#replyList").prev();
	            if(!prev.is("#replyForm")){
	                $("#replyForm").slideUp(300, function(){
	                    $("#replyForm").insertBefore("#replyList").slideDown(300);
	                    $("#replyContent").val("");
	                    $("#replyWriteButton").val("댓글쓰기");
	                });
	            } else {
	                $("#replyContent").val("");
	                $("#replyWriteButton").val("댓글쓰기");
	            }
	        }
	    });

	    // 댓글 수정 버튼
	    $(document).on("click", ".modifyReply", function(){
	        let $replyRow = $(this).closest(".replyRow");

	        if($("#replyForm").is(":visible")){
	            let $next = $replyRow.next();
	            if(!$next.is("#replyForm")){
	                $("#replyForm").slideUp(300, function(){
	                    $("#replyForm").insertAfter($replyRow).slideDown(300);
	                });
	            }
	        } else {
	            $("#replyForm").insertAfter($replyRow).removeClass("d-none")
	                           .css("display", "none")
	                           .slideDown(300);
	        }

	        $("#replyWriteButton").val("댓글수정");

	        // 댓글 내용 가져오기
	        let reply = $replyRow.find("pre").text();
	        $("#replyContent").val(reply);

	});


	
	//recommend버튼 눌렀을 때
	//thank버튼 눌렀을때
	$(document).on("click",".btnCommend",function(){
		//눌린 거에 span에 각각 id가있으니 가져오기 
		let type = $(this).attr("id");
		let boardId = $("#boardId").val();	
		let categoryId = $("#categoryId").val();	
			
		$.ajax({
			url:"/recommend.ajax",
			type:"post",
			data:{type :type,boardId:boardId,categoryId:categoryId},
			dataType:"json",
			success:function(data){
				var msg = type == 'commend'? "추천이 " : "좋아요가 ";
				alert(msg + "반영 되었습니다.")
				
				$("#commend > .recommend").text("("+data.recommend+")");
				$("#thank > .recommend").text("("+data.thank+")");		
				},
			error:function(xhr,status,error){
				console.log("error : " + xhr.statusText +"," + status + "," + error);
				}
			});
		
		});
	
	
	
	//업데이트 하기 눌렀을 때
	$(document).on("click","#goUpdateBoard",function(){
		
		let pass = $("#pass").val();
			    if(!pass){
			        alert("비밀번호를 입력해주세요");
			        return false;
			    }
			    $("#rPass").val(pass);
			    $("#detailCheckForm").attr("action", "/goUpdateBoard");
			    $("#detailCheckForm").attr("method", "post");
			    $("#detailCheckForm").submit();
	});
	
	
	
	//삭제 하기 버튼을 눌렀을 때
	//삭제는 fk반대 순으로
	$(document).on("click", "#detailDelete", function(){
	    let pass = $("#pass").val();
	    if(!pass){
	        alert("비밀번호를 입력해주세요");
	        return false;
	    }
	    $("#rPass").val(pass);
	    $("#detailCheckForm").attr("action", "/deleteBoard");
	    $("#detailCheckForm").attr("method", "post");
	    $("#detailCheckForm").submit();
	});
	
	
	
	
	
});