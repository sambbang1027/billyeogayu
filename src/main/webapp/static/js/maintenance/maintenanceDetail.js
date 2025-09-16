// 작성자 : 서샘이
/**
 * 기록 상세/수정 모달 띄우기
 */

$(document).ready(function() {
  // 수정 버튼 클릭 → 상태 확인 후 모달 띄우기
  $(document).on("click", ".edit-btn", function() {
    const $tr = $(this).closest("tr");
    const statusText = $tr.find(".status-complete span, .status-progress span").text().trim();
	const requestId = $(this).data("id");
	//console.log("request ID -----", requestId);

    if (statusText === "점검중") {
		
	//console.log('상태는 점검중 -> 모달 열림 ')
	openModal("#inspectionModal", requestId, "create");
	
    } else if (statusText === "점검완료") {
	//	console.log('상태는 점검완료 -> 모달 열림 ')
		openModal("#inspectionEditModal", requestId, "edit");
    }
  });

  // 닫기 버튼 누르면 해당 모달만 닫기
  $(document).on("click", ".close-btn", function() {
    // console.log('닫힙니다 모달이');
    const $modal = $(this).closest(".inspection-modal");
    if ($modal.length) {
      $modal.removeAttr("style").addClass("hidden").hide();
    }
  });
});



function openModal(modal, requestId, mode){
	// 모달 열기  -> requestId 를 모달에 숨겨두기 
	$(modal).data("request-id", requestId).removeClass("hidden").show();

	
	//Ajax
	$.ajax({
		url : "/admin/maintenance/detail/"+ requestId,
		type : "GET",
		success : function(res) {
			console.log(res);
			if(mode === "create"){
				// 조치내역 / 비고는 초기화만 (작성용)
				$(".inspection-textarea.action").val("");
				$(".inspection-textarea.note").val("");

			}else if(mode === "edit"){
				$(".inspection-record-textarea").text(res.description ? res.description : "");
				$(".inspection-textarea.note").val(res.note ? res.note : "");
				// 점검일 
				$(".inspection-value.date").text(res.resolvedAt);
			}
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
				$(".inspection-value.inspector").text(res.resolverName);
				
				// 소유주
				$(".inspection-value.owner").text(res.owner? res.owner : "농촌진흥청");
				// 부품 목록 
				let tags = "";
				if(res.parts){
					res.parts.split(",").forEach(function(part){
						tags += `<span class="tag">${part.trim()}</span>`;
					})
				}
				$(".inspection-tags").html(tags);
		}
	});
}


$(document).on("click", ".inspection-btn-complete.create", function() {
	const requestId = $("#inspectionModal").data("request-id")
	const description = $(".inspection-textarea.action").val().trim();
	const note = $(".inspection-textarea.note").val().trim();
	
	$.ajax({
		url : "/admin/maintenance/complete",
		type : "POST",
		contentType: "application/json; charset=UTF-8",
       data: JSON.stringify({
           requestId: requestId,
           description: description,
           note: note
       }),
	   success : function(res){
			if(res.code === "SUCCESS"){
				$("#inspectionModal").hide();
				showAlert("등록이 완료되었습니다.", () =>{
					location.reload(); // 새로 고침 
				}); 
			}
	   },
	   error : function(xhr, status, error){
		console.error(error);
		showAlert("등록 중 오류가 발생하였습니다", ()=> $("#inspectionModal").hide());
	   }
	});
})


$(document).on("click", ".inspection-btn-complete.edit", function(){
	const requestId = $("#inspectionEditModal").data("request-id");
	const note = $("#inspectionEditModal .inspection-textarea.note").val().trim();
	
	//console.log("수정된 비고 -->" + note);
	
	$.ajax({
		url : "/admin/maintenance/edit",
		type : "POST",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify({
			note : note,
			requestId : requestId
		}),
		success : function(res){
			 if(res.code === "SUCCESS"){
				$("#inspectionEditModal").hide();
				showAlert("수정이 완료되었습니다.", () => {
				location.reload();
				});
			 }
		},
		error : function(xhr, status, error){
			console.error(error);
			showAlert("기록 수정에 실패하였습니다 ", ()=> $("#inspectionEditModal").hide());
		}
		
	});
})