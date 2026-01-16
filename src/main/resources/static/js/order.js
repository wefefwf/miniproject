/* src/main/resources/static/js/order.js */

// 중복 선언 에러 방지를 위해 var 사용
var IMP = window.IMP; 

if (IMP) {
    // 공용 테스트 식별코드 (실결제가 일어나지 않는 안전한 코드입니다)
    IMP.init("imp78335007"); //나중엔 이 코드 지우기
}

function removeRow(cartNo) {
    if(confirm("해당 상품을 주문 목록에서 삭제하시겠습니까?")) {
        fetch('/cart/delete?cartNo=' + cartNo, { method: 'POST' })
        .then(res => {
            if(res.ok) {
                alert("삭제되었습니다.");
                location.reload(); 
            }
        });
    }
}

function handleRequestChange() {
    const select = document.getElementById('request_select');
    const directDiv = document.getElementById('direct_input_div');
    const memoInput = document.getElementById('order_memo');
    if (select.value === 'direct') {
        directDiv.style.display = 'flex';
        memoInput.value = "";
        memoInput.focus();
    } else {
        directDiv.style.display = 'none';
        memoInput.value = select.value;
    }
}

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

function requestPay() {
    const btn = document.getElementById('orderBtn');
    const amount = btn.getAttribute('data-amount');
    
    // 필수값 체크 (인프런 사례: 데이터가 정확해야 테스트창이 뜹니다)
    const buyerName = document.getElementById('buyer_name').value;
    const postcode = document.getElementById('postcode').value;
    const address = document.getElementById('address').value;
    const detail = document.getElementById('detailAddress').value.trim();

    if(!buyerName || !postcode || !detail) {
        alert("배송지 정보를 모두 입력해주세요.");
        return;
    }

    // 결제 요청 (테스트 모드)
    IMP.request_pay({
        pg: "kakaopay.TC0ONETIME", // ★ 카카오페이 가짜 결제(테스트) 전용 코드
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(),
        name: "ReeFlo 상품 주문",
        amount: Number(amount), // 숫자로 변환
        buyer_name: buyerName, // .value가 포함된 정확한 값
        buyer_tel: "010-0000-0000", // ★ 일부 PG사 테스트 시 필수값 (인프런 사례 참고)
        buyer_postcode: postcode,
        buyer_addr: address + " " + detail
    }, function (rsp) {
        if (rsp.success) {
            // 결제 성공 시 (테스트 성공)
            let url = "/order/success?merchant_uid=" + rsp.merchant_uid + "&imp_uid=" + rsp.imp_uid;
            location.href = url;
        } else {
            // 실패 원인 로그 출력 (F12 콘솔에서 확인 가능)
            console.log(rsp); 
            alert("결제창 열기 실패: " + rsp.error_msg);
        }
    });
}