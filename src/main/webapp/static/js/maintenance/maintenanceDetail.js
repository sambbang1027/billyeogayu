/**
 * 기록 상세/수정 모달 띄우기
 */

$(document).ready(function() {
  // 수정 버튼 클릭 → 상태 확인 후 모달 띄우기
  $(document).on("click", ".edit-btn", function() {
    const $tr = $(this).closest("tr");
    const statusText = $tr.find(".status-complete span, .status-progress span").text().trim();
	const requestId = $(this).data("id");
	console.log("request ID -----", requestId);

    if (statusText === "점검중") {
		
	console.log('상태는 점검중 -> 모달 열림 ')
	openModal("#inspectionModal", requestId);
	
     // $("#inspectionModal").removeClass("hidden").show();
    } else if (statusText === "점검완료") {
		console.log('상태는 점검완료 -> 모달 열림 ')
		openModal("#inspectionEditModal", requestId);
      //$("#inspectionEditModal").removeClass("hidden").show();
    }
  });

  // 닫기 버튼 누르면 해당 모달만 닫기
  $(document).on("click", ".close-btn", function() {
    console.log('닫힙니다 모달이');
    const $modal = $(this).closest(".inspection-modal");
    if ($modal.length) {
      $modal.removeAttr("style").addClass("hidden").hide();
    }
  });
});



function openModal(modal, requestId){
	// 모달 열기 
	$(modal).removeClass("hidden").show();
	
	//Ajax
	$.ajax({
		url : "/maintenance/detail/"+ requestId,
		type : "GET",
		success : function(res) {
			console.log(res);
		}
	})
	
}