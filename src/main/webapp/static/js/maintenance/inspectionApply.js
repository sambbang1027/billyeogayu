// 작성자 : 서샘이
/**
 * 점검 등록 전용 
 */
let selectedStatus = "";

$(function (){
    // 공통 드롭다운 유틸 초기화 (모달 전용)
    initDropdown(".custom-dropdown.apply", ".dropdown-toggle.apply", ".dropdown-menu.apply");

	$(document).on("click", ".custom-dropdown.apply .dropdown-menu li", function(e){
	    e.stopPropagation();
	    selectedStatus = $(this).data("value") || "";

	    const $dropdown = $(this).closest(".custom-dropdown.apply");
	    $dropdown.find(".dropdown-label.apply")
	             .text($(this).text())
	             .attr("data-value", selectedStatus);

	    // ✅ 선택 후 닫기
	    $dropdown.find(".dropdown-menu").hide();
	    $dropdown.find(".dropdown-toggle").removeClass("active");
	});
});
