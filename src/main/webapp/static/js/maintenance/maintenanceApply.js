/**
 *  점검 기록 등록 모달 JS
 */
let assetId = null;

$(document).ready(function() {
  // 등록 버튼 클릭 → 모달 열기
	  $(".maintenance-apply").on("click", function() {
    $("#inspectionApplyModal").removeClass("hidden").show();
//	assetId = $(".아이디");
		assetId = 54;
	$(".apply-input.asset").val("RS 650A");
	console.log('가져옵니다 필터');
	partList(assetId);
  });

  // 모달 닫기 버튼 클릭 → 모달 닫기
  $(document).on("click", "#inspectionApplyModal .close-btn", function() {
    $("#inspectionApplyModal").addClass("hidden").hide();
  });
});


// 드롭다운 함수 
let inspectionSelect;

document.addEventListener("DOMContentLoaded", function () {
  inspectionSelect = new Choices('#inspectionItems', {
    removeItemButton: true,
    placeholder: false,
    placeholderValue: '항목 선택',
    searchEnabled: false,
    shouldSort: false,
    noResultsText: '검색 결과 없음',
    noChoicesText: '선택할 항목이 없습니다',
    itemSelectText: '클릭해서 선택'
  });
});

//드롭다운에 부품 리스트 넣어주기 
function partList(assetId){

	$.ajax({
		url : "/maintenance/part-list/"+assetId ,
		type : "GET",
		success : function(res){
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
let selectedStatus = "";

$(document).on("click", ".dropdown-menu.apply li",function() {
    selectedStatus = $(this).data("value") || "";
	
	//  $dropdown 먼저 선언해야 함
	   const $dropdown = $(this).closest(".custom-dropdown.apply");
	   
	     // 라벨 업데이트
	       $dropdown.find(".dropdown-label.apply")
	                .text($(this).text())
	                .attr("data-value", selectedStatus);

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

		//console.log('담당자 : ', resolverName, '정검 유형 : ', selectedStatus, '항목 : ', JSON.stringify(data));
	
		// 서버 전송 
	$.ajax({
		url : "/maintenance/apply",
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify({
			assetId : assetId,
			parts : data,
			adminId : 41,
			resolverName : resolverName,
			type : selectedStatus
		}),
		success : function(res){
			if(res.code === "SUCCESS"){
			alert("점검 신청이 등록되었습니다.");				
			$("#inspectionApplyModal").addClass("hidden").hide();
			location.reload();
			}
		},
		error : function(xhr, status, error){
			console.error('점검 신청 실패', error);
		}
	})
})


