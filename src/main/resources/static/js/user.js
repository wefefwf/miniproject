	$(function() {
		
		$("#btnPassCheck").click(function() {
		var oldId = $("#id").val();
		var oldPass = $("#oldPass").val();
		if($.trim(oldPass).length == 0) {
		alert("기존 비밀번호가 입력되지 않았습니다.\n기존 비밀번호를 입력해주세요");
		return false;
		}
		var data = "id=" + oldId + "&pass="+oldPass;
		console.log("data : " + data);
		$.ajax({
		"url": "passCheck.ajax",
		"type": "get",
		"data": data,
		"dataType": "json",
		"success": function(resData) {
		if(resData.result) {
		alert("비밀번호가 확인되었습니다.\n비밀번호를 수정해주세요");
		$("#btnPassCheck").prop("disabled", true);
		$("#oldPass").prop("readonly", true);
		$("#pass1").focus();
		} else {
		alert("비밀번호가 틀립니다.\n비밀번호를 다시 확인해주세요");
		$("#oldPass").val("").focus();
		}
		},
		"error": function(xhr, status) {
		console.log("error : " + status);
		}
		});
		});
		// 회원정보 수정 폼에서 수정하기 버튼이 클릭되면 유효성 검사를 하는 함수
		$("#userUpdateForm").on("submit", function() {
		/* 회원정보 수정 폼에서 "비밀번호 확인" 버튼이 disabled 상태가 아니면
		* 기존 비밀번호를 확인하지 않았기 때문에 확인하라는 메시지를 띄운다. **/
		if(! $("#btnPassCheck").prop("disabled")) {
		alert("기존 비밀번호를 확인해야 비밀번호를 수정할 수 있습니다.\n" + "기존 비밀번호를 입력하고 비밀번호 확인 버튼을 클릭해 주세요");
		return false;
		}
		/* 회원정보 수정 폼에서 서브밋 이벤트가 발생하면 false를 인수로 지정한다. * joinFormChcek() 함수에서 폼 유효성 검사를 통과하지 못하면
		* false가 반환되기 때문에 그대로 반환하면 폼이 서브밋 되지 않는다. **/
		return joinFormCheck(false);
		});
		});
		
		
		
		
	
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
