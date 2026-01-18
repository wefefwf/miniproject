$(function(){
	
	//펫 상세페이지에서 펫 삭제를 누르면
	$(document).on("click","#deletePet",function(){
	
	let petId = $(this).data("pet-id");
		
	//정말 삭제할거냐고 물어보고 컨트롤러로 보내기 
	if (confirm("정말 삭제할까요?")) {
	    location.href = "/deletePet?petId=" + petId ;
		}
	//아니오 누르면 뭐 걍 암것도 없다 
	});
	
	
	//펫 추가 폼 전부 들어갔는지 확인
	$(document).on("submit","#petAdd",function(){
		
		let name  = $("#name").val();
		let gender  = $("#gender").val();
		let birthday  = $("#birthday").val();
		let age  = $("#age").val();
		let weight  = $("#weight").val();
		let content  = $("#content").val();
		//이미지는 뭐 넣을수도 안넣을 수도
		
		if(name.length == 0) {
							alert("이름을 입력하세요");
							return false;
						}
		
		if(gender.length == 0) {
							alert("성별을 입력하세요");
							return false;
						}				
		if(birthday.length == 0) {
									alert("생일을 입력하세요");
									return false;
								}
				
		if(age.length == 0) {
							alert("나이를 입력하세요");
							return false;
						}
		if(weight.length == 0) {
									alert("몸무게를 입력하세요");
									return false;
								}
				
		if(content.length == 0) {
							alert("특징을 입력하세요");
							return false;
						}						
						
		//다 넣으면 뭐 자기 알아서 submit됨			
	});
	
	
});