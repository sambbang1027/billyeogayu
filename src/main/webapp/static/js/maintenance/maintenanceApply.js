/**
 *  점검 기록 등록 모달 JS
 */
  let adminId;
  // 모달 닫기 버튼 클릭 → 모달 닫기
  $(document).on("click", "#inspectionApplyModal .close-btn", function() {
    $("#inspectionApplyModal").addClass("hidden").hide();
  });


// 드롭다운 함수 
let inspectionSelect;

document.addEventListener("DOMContentLoaded", function () {
  inspectionSelect = new Choices('#inspectionItems', {
    removeItemButton: true,
    placeholder: true,
    placeholderValue: '항목 선택',
    searchEnabled: false,
    shouldSort: false,
    noResultsText: '검색 결과 없음',
    noChoicesText: '선택할 항목이 없습니다',
    itemSelectText: '클릭해서 선택'
  });
  getAdminUser();
});

//드롭다운에 부품 리스트 넣어주기 
function partList(assetId){

	$.ajax({
		url : "/admin/maintenance/part-list/"+assetId ,
		type : "GET",
		success : function(res){
			console.log("서버 응답:", res); // ✅ 확인 필수
			 inspectionSelect.clearStore();
			  inspectionSelect.setChoices(
			    res.map(item => ({
			      value: item.PART_ID,
			      label: item.NAME,
			      selected: false
			    })),
			    'value',
			    'label',
			    false
			  );
		}
	})
}

// 점검 유형 값 가져오기 
let inspectionStatus = "";

$(document).on("click", ".dropdown-menu.apply li",function() {
    inspectionStatus = $(this).data("value") || "";
	
	//  $dropdown 먼저 선언해야 함
	   const $dropdown = $(this).closest(".custom-dropdown.apply");
	   
	     // 라벨 업데이트
	       $dropdown.find(".dropdown-label.apply")
	                .text($(this).text())
	                .attr("data-value", inspectionStatus);

	       // 메뉴 닫기
	       $dropdown.find(".dropdown-menu.apply").hide();
	       $dropdown.find(".dropdown-toggle.apply").removeClass("active");

	      // console.log("선택된 상태:", selectedStatus);
	   });


// 신청 버튼 클릭 시 발동 
$(document).on("click", ".apply-btn-submit", function() {

	// 점검자
	const resolverName = $(".apply-input").val().trim();
	// 부품 목록 
		const selectedItems = inspectionSelect.getValue();
		const data = selectedItems.map(item => ({
		  partId: item.value,
		  partName: item.label
		}));
		// 모달의 data 속성에서 assetId 가져오기
		const assetId = $("#inspectionApplyModal").attr("data-asset-id");

		console.log('담당자: ',adminId, ' 자원 ID: ', assetId, ' 점검자 : ', resolverName, 
							'정검 유형 : ', inspectionStatus, '항목 : ', JSON.stringify(data));
		
		
		// 서버 전송 
	$.ajax({
		url : "/admin/maintenance/apply",
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify({
			assetId : assetId,
			parts : data,
			adminId : adminId,
			resolverName : resolverName,
			type : inspectionStatus
		}),
		success : function(res){
			if(res.code === "SUCCESS"){
			showAlert("점검이 신청되었습니다.", () => location.reload());		
			$("#inspectionApplyModal").addClass("hidden").hide();
			}
		},
		error : function(xhr, status, error){
			showAlert("점검 신청이 실패되었습니다", () => 	console.error('점검 신청 실패', error));
			$("#inspectionApplyModal").addClass("hidden").hide();
		}
	})
})

function getAdminUser () {
	$.ajax({
	  url: "/api/profile",
	  type: "GET",
	  success: function(res) {
	//console.log('apply ', res);
	  adminId = res.data.userId;

   	  },
	  error: function(xhr) {
	    if(xhr.status === 401){
	      alert("로그인이 필요합니다.");
	      location.href = "/login";
	    } else {
	      showAlert("회원정보를 가져올 수 없습니다", () =>{
			 	console.error('점검 신청 실패', error); location.reload();})
	  	}
	  }
	});
}
