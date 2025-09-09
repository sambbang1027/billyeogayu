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

			// 점검 유형 
			let type;
			if(res.type === "EMERGENCY"){
				type = "긴급점검";
			}else if(res.type === "REGULAR"){
				type = "정기점검"; 
			}
			$(".inspection-value.type").text(type);
			
			// 자원종류 (category)
			$(".inspection-value.assetType").text(res.assetKind);
			
			// 자원명 (모델명 )
			$(".inspection-value.assetName").text(res.assetName);
			
			// 점검자 
			$(".inspection-value.inspector").text(res.resolverName)
			
			// 부품 목록 
			let tags = "";
			if(res.parts){
				res.parts.split(",").forEach(function(part){
					tags += `<span class="tag">${part.trim()}</span>`;
				})
			}
			$(".inspection-tags").html(tags);
			
			// 조치내역 / 비고는 초기화만 (작성용)
			$(".inspection-textarea.action").val("");
			$(".inspection-textarea.note").val("");
		}
	})
}


