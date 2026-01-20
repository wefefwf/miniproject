$(function(){
	
	
	
	//좋아요 싫어요 버튼 누르면
	$(document).on("click", ".good, .bad", function() {
	    let hospitalId = $(this).closest(".like-row").data("hospital-id");
		
		// 만약 여전히 안된다면 아래 줄로 대체
		    // let hospitalId = $("#modalLikeRow").data("hospital-id");
			
	    let type = $(this).hasClass("good") ? "good" : "bad";

	    $.ajax({
	        url: "/like.ajax",
	        type: "post",
	        data: { hospitalId: hospitalId, type: type },
	        dataType: "json",
	        success: function(data){
	            let msg = type === 'good' ? "좋아요가 " : "싫어요가 ";
	            alert(msg + "반영되었습니다.");
				
	            $("#goodCount").text(`(${data.good})`);
	            $("#badCount").text(`(${data.bad})`);

            //카드 속 애들도 직접 바꾸기
			// hospitalId 변수를 사용하여 해당 병원의 카드만 숫자를 바꿉니다.
			    $(`#cardGood-${hospitalId}`).text(`(${data.good})`);
			    $(`#cardBad-${hospitalId}`).text(`(${data.bad})`);
	        },
	        error: function(error){
	            console.log(error);
	        }
	    });
	});

	
	// 모달이 열린 직후
	$('#hospitalModal').on('shown.bs.modal', function () {
	    // 지도 객체가 이미 존재하면 리사이즈 & 중심 재조정
	    if (window.hospitalMapInstance) {
	        window.hospitalMapInstance.invalidateSize(); // div 크기 재계산
	        window.hospitalMapInstance.setView([window.hospitalLat, window.hospitalLng], 16);
	    }
	});
	
	
	//모달이 눌리면 해당 병원의 정보를 들고와야됨
	$(document).on("click", ".hospital-card", function () {
		
	    //data에 저장해둔 병원아이디 받기
		let hospitalId = $(this).data("hospital-id");
		
		// 모달 내 like-row에 현재 클릭한 병원 ID를 심어줌
		    $("#modalLikeRow").data("hospital-id", hospitalId);
		
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
				
				  
				  //지도----------------------------------------
				  // 위도/경도 저장
	              window.hospitalLat = data.latitude;
	              window.hospitalLng = data.longitude;
				  
				  // 지도 초기화 (모달 열릴 때 정확히 보이도록)
                  setTimeout(() => {
                      initHospitalMap(data.latitude, data.longitude, data.name);
                  }, 100); // 모달 렌더링 후 잠깐 대기
              
			},
			error : function(error,status){
				console.log(error)
			}
			
		});

	});
	
	/*지도!!!!!!!!*/
	// Leaflet 지도 초기화
	/* 지도 초기화 */
	function initHospitalMap(lat, lng, name) {
	    // div 가져오기
	    let mapContainer = document.getElementById('hospitalMap');

	    // 기존 지도 제거
	    if (window.hospitalMapInstance) {
	        window.hospitalMapInstance.remove();
	    }

	    // 지도 생성 저 14은 줌 레벨
	    let map = L.map('hospitalMap').setView([lat, lng], 14);

		//오픈 스트리트 지도 맵 위도 경도로 불러오겠다
		
	    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			//저작자 표시
	        attribution: '&copy; OpenStreetMap contributors'
			//오픈스트리트 맵을 우리 저 지도객체에 붙임
	    }).addTo(map);

	    // 마커 생성 
	    let marker = L.marker([lat, lng]).addTo(map);
		//누르면 마커 팝업 (X 버튼 제거)
	    marker.bindPopup(`<b>${name}</b>`, 
			{ closeButton: false, offset: [-48, -10]}).openPopup();

	    // 모달 안에서 div 크기 문제 해결
		//위치 재정의임 
		setTimeout(() => {
		        map.invalidateSize();
		        map.setView([lat, lng], 14);
		    }, 50);
	    // 전역 저장 집어넣기
	    window.hospitalMapInstance = map;
	}
	
});