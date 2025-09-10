/**
 *  점검 기록 등록 모달 JS
 */

$(document).ready(function() {
  // 등록 버튼 클릭 → 모달 열기
  $(".maintenance-apply").on("click", function() {
    $("#inspectionApplyModal").removeClass("hidden").show();
  });

  // 모달 닫기 버튼 클릭 → 모달 닫기
  $(document).on("click", "#inspectionApplyModal .close-btn", function() {
    $("#inspectionApplyModal").addClass("hidden").hide();
  });
});

// 달력 함수 
$(document).ready(function() {
  // flatpickr 초기화
  const picker = flatpickr("#inspectionDate", {
    dateFormat: "Y-m-d",
    locale: "ko"
  });

  // 아이콘 클릭 → 달력 열기
  $(".calendar-icon").on("click", function() {
    picker.open();
  });
});

// 드롭다운 함수 
document.addEventListener("DOMContentLoaded", function () {
  const element = document.getElementById("inspectionItems");
  new Choices(element, {
    removeItemButton: true, // X 버튼으로 삭제 가능
    placeholder: false,
    placeholderValue: '항목 선택',
    searchEnabled: false,   // 검색창 안 쓰고 싶으면 false
    shouldSort: false       // 기존 순서 유지
  });
});

