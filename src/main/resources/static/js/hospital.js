$(function(){
	
	//모달이 눌리면 해당 병원의 정보를 들고와야됨
	$(document).on("click", ".hospital-card", function () {
		
	    //data에 저장해둔 병원아이디 받기
		let hospitalId = $(this).data("hospital-id");
		let dataUrl = 'hospitalId='+hospitalId;
		
				
		$.ajax({
			url : "/hospital/detail",
			type : "get",
			dataType : "json",
			data : dataUrl,
			success : function(data){
				$("#modalHospitalName").text(data.name);
				// 테이블
				  $("#modalDoctorName").text(data.doctorName);
				  $("#modalAddress").text(data.address);
				  $("#modalPhone").text(data.phone);
				  $("#modalContent").text(data.content);
				  //날짜
				  let createdAt = new Date(data.createdAt);  // 문자열 → Date 객체
				  let year = createdAt.getFullYear();
				  let month = String(createdAt.getMonth() + 1).padStart(2, '0'); // 월은 0~11이라 +1
				  let day = String(createdAt.getDate()).padStart(2, '0');

				 $("#modalCreatedAt").text(`${year}-${month}-${day}`);
				  
				  // 좋아요 / 싫어요
				  let good = data.good;
				  let bad = data.bad;
				  $("#goodCount").text(`(${good})`);
				  $("#badCount").text(`(${bad})`);
				
			},
			error : function(error,status){
				console.log(error)
			}
			
		});

	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});