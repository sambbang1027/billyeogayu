/**
 * 처리 열에 해당하는 로직들  
 */

// 승인 처리 
$(document).on("click", ".rv-btn-approve", function(){
  const reservationId = $(this).data("id");
  console.log("승인 처리할 예약:", reservationId);
  
  showConfirm("예약을 승인하시겠습니까?",
    () => {
				console.log("승인 실행");
				// → AJAX 요청 보내기
				$.ajax({
				url : "/" + reservationId, 
				type : "GET", 
				success : function(res){
					console.log(res);
					location.reload();
				}, 
				error : function(xhr, status, error){
					console.error("예약 승인 실패", error);
				}
		})
	},
    () => console.log("취소됨")
  );
});

// 반려 처리 
let currentReservationId = null;

// 1) 반려 버튼 클릭 → 모달 열기
$(document).on("click", ".rv-btn-reject", function(){
  currentReservationId = $(this).data("id");
  $("#rv-rejectOverlay, #rv-rejectModal").fadeIn();
});

// 2) 모달 안 "반려" 버튼 클릭 → 서버 전송
$(document).on("click", ".btn-reject-confirm", function(){
  const rejectReason = $("#rejectReason").val().trim();

  if(!rejectReason){
    alert("반려 사유를 입력해주세요.");
    return;
  }

  $.ajax({
    url : "/reservation/reject",   // 실제 API 경로로 교체
    type : "POST",
    contentType : "application/json; charset=UTF-8", 
    data : JSON.stringify({
      reservationId : currentReservationId,
      rejectReason  : rejectReason
    }),
    success : function(res){
      alert("예약이 반려되었습니다.");
      $("#rv-rejectOverlay, #rv-rejectModal").fadeOut();
      $("#rejectReason").val(""); 
      location.reload();
    }, 
    error : function(xhr, status, error){
      console.error("예약 반려 실패", error);
    }
  });
});

// 3) 모달 안 "취소" 버튼 클릭 → 닫기
$(document).on("click", ".btn-reject-cancel", function(){
  $("#rv-rejectOverlay, #rv-rejectModal").fadeOut();
  $("#rejectReason").val(""); // 입력값 초기화
  currentReservationId = null;
});

// 4) 오버레이 클릭 시 닫기
$(document).on("click", "#rv-rejectOverlay", function(){
  $("#rv-rejectOverlay, #rv-rejectModal").fadeOut();
  $("#rejectReason").val("");
  currentReservationId = null;
});


// 반납 처리 
$(document).on("click", ".rv-btn-complete", function(){
  const reservationId = $(this).data("id");
  console.log("반납 처리할 예약:", reservationId);

  showConfirm("반납처리 하시겠습니까?",
    () => {
      $.ajax({
        url : "/" + reservationId, 
        type : "GET", 
        success : function(res){
          console.log(res);
          location.reload();
        }, 
        error : function(xhr, status, error){
          console.error("반납 실패", error);
        }
      });
    },
    () => console.log("취소됨")   // ← 취소 콜백도 줄 수 있음
  );
});

 // 상세 버튼 클릭 → 반려 사유 모달 열기
 $(document).on("click", ".rv-btn-view", function(){
   const reservationId = $(this).data("id");
   console.log("상세 조회할 예약:", reservationId);

   // AJAX로 반려 사유 조회
   $.ajax({
     url : "/reservation/rejectReason/" + reservationId,  // 실제 API에 맞게 수정
     type : "GET",
     success : function(res){
       // 예: res.rejectReason 에 값 있다고 가정
       $("#rv-rejectDetailReason").val(res.rejectReason || "등록된 반려 사유가 없습니다.");
       $("#rv-rejectDetailOverlay, #rv-rejectDetailModal").fadeIn();
     },
     error : function(xhr, status, error){
       console.error("반려 상세 조회 실패", error);
       showAlert("반려 사유를 불러오지 못했습니다.");
     }
   });
 });

 // 확인 버튼 / 오버레이 → 닫기
 $(document).on("click", ".btn-reject-detail-close, #rv-rejectDetailOverlay", function(){
   $("#rv-rejectDetailOverlay, #rv-rejectDetailModal").fadeOut();
   $("#rv-rejectDetailReason").val(""); // 초기화
 });
