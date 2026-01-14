$(function(){
	
	
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