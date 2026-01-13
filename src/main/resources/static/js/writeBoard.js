$(function(){
	
	//아이디 카테고리 자동입력됨 
	//각 항목별로 올바른 타입이 적혔는지 정규식 검사 
	
//writeform 등록하기 눌렀을때 전부 있는지 확인
$("#addBoard ").on("submit", function () {

		const categoryId = Number($("#categoryId").val());

		// 제목
		if ($("#title").val().trim() === "") {
			alert("제목을 입력하세요.");
			$("#title").focus();
			return false;
		}
		// 내용
		if ($("#content").val().trim() === "") {
			alert("내용을 입력하세요.");
			$("#content").focus();
			return false;
		}
		// 실종 게시판
		if (categoryId === 3) {

			if ($("#age").val().trim() === "") {
				alert("나이를 입력하세요.");
				$("#age").focus();
				return false;
			}
			if ($("#region").val().trim() === "") {
				alert("실종 지역을 입력하세요.");
				$("#region").focus();
				return false;
			}
		}
		// 입양 게시판
		if (categoryId === 4) {

			if ($("#age").val().trim() === "") {
				alert("나이를 입력하세요.");
				$("#age").focus();
				return false;
			}
			if ($("#birthday").val().trim() === "") {
				alert("생년월일을 입력하세요.");
				$("#birthday").focus();
				return false;
			}
		}
	});
	
	
	
});