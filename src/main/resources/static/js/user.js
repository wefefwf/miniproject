	$(function() {
	
		$("#loginForm").submit(function() {
		var id = $("#userId").val();
		var pass = $("#userPass").val();
		if(id.length <= 0) {
		alert("아이디가 입력되지 않았습니다.\n아이디를 입력해주세요");
		$("#userId").focus();
		return false;
	}
		
		if(pass.length <= 0) {
	
		alert("비밀번호가 입력되지 않았습니다.\n비밀번호를 입력해주세요");
		$("#userPass").focus();
		return false;
		}
	});

	});