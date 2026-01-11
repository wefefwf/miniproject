	$(function() {
		
		//회원 가입 폼 가입을 누르면 !!!!
		//마지막 점검 함수 실행
		$("#joinForm").on("submit", function() {
			//여기 true는 이거 수정 폼이랑 같이 써서 true면 회원 가입 폼인거임
				return joinFormCheck(true);
			});
			
			
			
		//비밀 번호 확인 칸 입력체크 
		//keyup하면 한글자 누를때마다 알림 뜨면 focus빠지는 값이 기준인 blur를 넣음
		$("#pass2").on("blur",function(){
			
			let pass1 = $("#pass1").val();
			let pass2 = $("#pass2").val();
			
			//비밀번호 입력안됐으면 위부터 입력하라고 말하기
			if(pass1 == ""){
				alert("비밀 번호를 먼저 입력해주세요.");
				$("#pass1").focus();
			}else{
			//입력 됐으면 둘 비교해서 같지 않으면 안같다고 알려주기
			if(pass1 != pass2){
				alert("입력된 비밀 번호가 다릅니다.\n 다시 한 번 확인해주세요.")
				}
			}
		});	
			
			
		//이메일 주소 체크에 따라 각자 다른 값 넣어주기 
		//전화번호는 그대로 들어가서안해도됨!
		$("#selectDomain").on("change",function(){
			let domain = $(this).val();
			
			if(domain == '직접입력'){
				$("#emailDomain").val("");
				$("#emailDomain").prop("readonly", false);		
				$("#emailDomain").focus();
				}
			if(domain == '네이버'){
				$("#emailDomain").val("naver.com");
				$("#emailDomain").prop("readOnly",true);
			}
			if(domain == '다음'){
				$("#emailDomain").val("daum.net");
				$("#emailDomain").prop("readOnly",true);
			}
			if(domain == '한메일'){
				$("#emailDomain").val("hanmail.net");
				$("#emailDomain").prop("readOnly",true);
			}
			if(domain == '구글'){
				$("#emailDomain").val("gmail.com");
				$("#emailDomain").prop("readOnly",true);
			}
		});
		
		//우편 주소 찾기 버튼 누르면 daum api이용
		$("#btnZipcode").click(findZipcode);
		
		//중복확인 폼에서 아이디 사용하기 버튼 눌렀을 때
		$("#btnIdCheckClose").on("click",function(){
			//아이디 값받아서 원래 폼 아이디에 넣어주고 isIdCheck true시키기 
			let id =$(this).data("id-value");
			opener.document.joinForm.id.value = id;
			opener.document.joinForm.isIdCheck.value = true;
			opener.$("#id").prop("readonly",true);
			//중복확인 창 닫기 
			window.close();
		});
		
		
		//회원 가입 폼에서 아이디 중복확인 눌럿을때 
		$("#btnOverlap").on("click",function(){
			//아이디 값을 가져오고		
			let id = $("#id").val();
				// 입력이 안되었다면 경고 창 띄우고 종료
				if(id.length == 0) {
					alert("아이디를 입력하세요");
					return false;
				}
				// 5자 미만 - 경고창
				if(id.length <5) {
					alert("아이디는 5자 이상 입력하세요");
					return false;
				}
				
				//다 입력됐을 시 url에 해당 입력 아이디를 넣고 
				let url = "overlapIdCheck?id=" + id;
				//아이디 중복 확인 폼 열기		
				window.open(url, "idCheck", "toolbar=no, scrollbars=no, resizeable=no, " 
					+ "status=no, menubar=no, width=500, height=300"); 
			});
		
		
		//아이디 특수문자 입력되면 막기 함수
		$("#id").on("keyup", function(e) {
				let regExp = /[^A-Za-z0-9]/gi;
				if(regExp.test($(this).val())) {
					alert("영문 대소문자, 숫자만 입력할 수 있음");
					$(this).val($(this).val().replace(regExp, ""));
					return false;
				}
			});
	
		//비번1, 비번2,이메일1,이메일2 특수문자 확인용 함수걸기
		$("#pass1").on("keyup",inputCharReplace);
		$("#pass2").on("keyup",inputCharReplace);
		$("#emailId").on("keyup",inputCharReplace);
		$("#emailDomain").on("keyup",inputEmailReplace);
		$("#mobile2").on("keyup",inputNumReplace);
		$("#mobile3").on("keyup",inputNumReplace);
		
		
		
		//비밀 번호 체크 
		$(document).on("click", "#btnPassCheck", function() {
			console.log("일단 함수는 실행됨 ")
		var oldId = $("#id").val();
		var oldPass = $("#oldPass").val();
		if($.trim(oldPass).length == 0) {
		alert("기존 비밀번호가 입력되지 않았습니다.\n기존 비밀번호를 입력해주세요");
		return false;
		}
		var data = "id=" + oldId + "&pass="+oldPass;
		console.log("data : " + data);
		$.ajax({
		"url": "/passCheck.ajax",
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
		
		
		
		
		//로그인버튼 눌렀을 때 확인하는 작업
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

	//폰번호 칸 숫자만 들어올수 있게 막기
			function inputNumReplace(){
				let regExp = /[^0-9]/gi;
				if(regExp.test($(this).val())){
					alert("전화번호에는 숫자만 입력할 수 있습니다.");
					$(this).val($(this).val().replace(regExp,""));
					return false;
				}
			};
	
		//비번1,비번2, 이메일 1 특수문자 막기
		function inputCharReplace(){
			let regExp = /[^A-Za-z0-9]/gi;
			if(regExp.test($(this).val())){
				alert("영문 대소문자, 숫자만 입력할 수 있습니다.");
				$(this).val($(this).val().replace(regExp,""));
				return false;
			}
		};
		
		//이메일은 .포함해서 막아야되서 따로 만듬
		function inputEmailReplace(){
			let regExp =/[^a-z\.]/gi;
			if(regExp.test($(this).val())) {
				alert("이메일 도메인은 소문자, 점(.)만 입력할 수 있습니다.");
				$(this).val($(this).val().replace(regExp,""));
				return false;
				}
		};
		
		function findZipcode(){
			
			//daum api쓸거임
			new daum.Postcode({
				//사용자가 주소를 클릭시 실행
				    oncomplete: function(data) {
				  		
						//최종으로 넣을 주소 문자열
				        var addr = ''; // 주소 변수
						//동명 , 건물 명 넣을거임
				        var extraAddr = ''; // 참고항목 변수
						
						//사용자가 선택한 도로명 주소			
						addr = data.roadAddress;

			            //이름 안비고 동 로 가 로 끝나는지 검사
			            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
			                extraAddr += data.bname;
			            }
			            // 건물명이 있고, 공동주택일 경우 추가한다.
			            if(data.buildingName !== '' && data.apartment === 'Y'){
							//이미 동명이 있으면 ,하고 건물명 없으면 그냥 건물명
			                extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
			            }
			            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
			            if(extraAddr !== ''){
			                extraAddr = ' (' + extraAddr + ')';
			            }
						
						//도로명 주소 참고 항목 합쳐주기 //최종
						addr += extraAddr;			
					
						//우편번호에 번호넣기
						$("#zipcode").val(data.zonecode);			
						// 상세주소에 총 문자열 넣기
						$("#address1").val(addr);	      
						//이제 집동호수 쓰라고 focus
						$("#address2").focus();
				    }
					//설정 다 했으니까 주소 검색창 열기 ! 
				}).open();	
		};
		
		//조인 폼이냐고 물어보는거임
		function joinFormCheck(isJoinForm){
			let name = $("#name").val();
				let id = $("#id").val();
				let pass1 = $("#pass1").val();
				let pass2 = $("#pass2").val();
				let zipcode = $("#zipcode").val();
				let address1 = $("#address1").val();
				let address2 = $("#address2").val();
				let emailId = $("#emailId").val();
				let emailDomain = $("#emailDomain").val();
				let mobile2 = $("#mobile2").val();
				let mobile3 = $("#mobile3").val();
				let isIdCheck = $("#isIdCheck").val();
				
				if(name.length == 0) {
					alert("이름을 입력하세요");
					return false;
				}
				if(id.length == 0) {
					alert("아이디를 입력하세요");
					return false;
				}
				//isJoinForm==회원가입 폼 
				//아이디 중복체크 후 isIdCheck = true 
				// 둘다true일때 실행 
				//joinForm이 아니면 중복체크를 안해도되니까 
				if(isJoinForm &&  isIdCheck == 'false') {
					alert("아이디 중복체크를 해주세요");
					return false;
				}
				if(pass1.length == 0) {
					alert("비밀번호를 입력하세요");
					return false;		
				}
				if(pass2.length == 0) {
					alert("비밀번호 확인을 입력하세요");
					return false;		
				}
				if(zipcode.length == 0) {
					alert("우편번호를 입력하세요");
					return false;
				}
				if(address1.length == 0) {
					alert("주소를 입력하세요");
					return false;
				}	
				if(emailId.length == 0) {
					alert("이메일 아이디를 입력하세요");
					return false;
				}	
				if(emailDomain.length == 0) {
					alert("이메일 도메인을 입력하세요");
					return false;
				}	
				if(mobile2.length ==0 || mobile3.length == 0) {
					alert("휴대폰 번호를 입력하세요");
					return false;
				}
	};