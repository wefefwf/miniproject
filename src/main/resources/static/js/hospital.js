$(function(){
	
	// 모달에서 수정하기 버튼 눌리면
	$("#updateBtn").on("click", function () {
	    let hospitalId = $(this).data("hospital-id");

	    location.href =
	        "/updateHospital?hospitalId=" + hospitalId +
	        "&redirectUrl=" + encodeURIComponent(window.location.href);
	});
	
	//병원 추가 폼 submit되면
	$(document).on("submit","#addHospital",function(e){
		
		//지오코딩 해야되서 일반 submit막기
		e.preventDefault(); 
		
		//병원 이름
		if ($("#name").val().trim() === "") {
			alert("병원 이름을 입력하세요.");
			$("#name").focus();
			return false;
		}
		
		//원장 이름
		if ($("#doctorName").val().trim() === "") {
			alert("원장 선생님 성함을 입력하세요.");
			$("#doctorName").focus();
			return false;
		}
		//병원 주소
		if ($("#address").val().trim() === "") {
			alert("병원의 도로명 주소를 입력하세요.");
			$("#address").focus();
			return false;
		}
		//전화번호,2,3
		if ($("#mobile2").val().trim() === "") {
			alert("전화번호를 입력하세요.");
			$("#mobile2").focus();
			return false;
		}
		if ($("#mobile3").val().trim() === "") {
			alert("전화번호를 입력하세요.");
			$("#mobile3").focus();
			return false;
		}
		//전화번호 네자리인지도 봐야할 듯
		if ($("#mobile2").val().length < 2) {
			alert("전화번호 앞 자리 세 개 이상 입력해주세요.");
			$("#mobile2").focus();
			return false;
		}

		if ($("#mobile3").val().length < 4) {
			alert("전화번호 뒷 자리 네 개를 입력해주세요.");
			$("#mobile2").focus();
			return false;
		}
		
		if ($("#content").val().trim() === "") {
			alert("간단한 병원 소개를 입력하세요.");
			$("#content").focus();
			return false;
		}

		// 이미지 필수 체크
		if ($("#file").val().trim() === "") {
		    alert("이미지를 등록하세요.");
		    $("#file").focus();
		    return false;
		}

		//도로명 주소를 nominatim을이용해 위도 경도 뽑아내기 
		let address = $("#address").val();
		
		//외부 nominatim api로 요청 
		$.ajax({
		    url: "https://nominatim.openstreetmap.org/search",
		    type: "get",
		    dataType: "json",
		    data: {
				//이 형태로 주세요
		        format: "json",
				//검색어
		        q: address,
				//결과는 1개 
		        limit: 1
		    },
		    success: function (result) {
		        if (result.length === 0) {
		            alert("주소로 좌표를 찾을 수 없습니다.");
		            return;
		        }

				/*	응답이 이렇게 옴	
				result = [
				  {
				    lat: "37.558104",
				    lon: "126.928066",
				    display_name: "서울특별시 마포구 신촌로 16 ..."
				  }
				]*/
				
		        let lat = result[0].lat;
		        let lng = result[0].lon;

		        // hidden input에 위경도 심기
		        $("#latitude").val(lat);
		        $("#longitude").val(lng);

		        // ⭐ 여기서 직접 submit
		        $("#addHospital")[0].submit();
		    },
		    error: function () {
		        alert("좌표 변환 실패");
		    }
		});
		
		return false;
	});
	
	
	// 병원 삭제 
	$(document).on("click", "#deleteBtn", function() {
	    // 1. 현재 모달에 심어져 있는 hospitalId 가져오기
	    let hospitalId = $("#modalLikeRow").data("hospital-id");

	    if (confirm("정말로 이 병원 정보를 삭제하시겠습니까?")) {
	        $.ajax({
	            url: "/delete.ajax",
	            type: "post",
	            data: { hospitalId: hospitalId },
	            success: function(data) {
					if(data === "success") {
					        alert("게시물이 삭제되었습니다.");
					        location.reload(); // 여기서 리스트를 새로고침해서 삭제를 반영함
					    } else {
					        alert("삭제 권한이 없거나 오류가 발생했습니다.");
					    }
	        		},
				error : function(error){
					console.log(error)
				}
			
	   		 });
		}
	});
	
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
		//업데이트 버튼에도 넣어줌 
			$("#updateBtn").data("hospital-id", hospitalId);
			
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