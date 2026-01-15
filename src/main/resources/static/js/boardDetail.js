$(function(){
	
	//recommend버튼 눌렀을 때
	//thank버튼 눌렀을때
	$(document).on("click",".btnCommend",function(){
		//눌린 거에 span에 각각 id가있으니 가져오기 
		let type = $(this).attr("id");
		let boardId = $("#boardId").val();	
		let categoryId = $("#categoryId").val();	
			
		$.ajax({
			url:"recommend.ajax",
			type:"post",
			data:{type :type,boardId:boardId,categoryId:categoryId},
			dataType:"json",
			success:function(data){
				var msg = type == 'commend'? "추천이" : "좋아요가";
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