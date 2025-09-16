// 작성자 : 서샘이
/**
 * 필터 전용 
 */
let activeFilters = { assetKind: null, company: null, maintStatus: null };

$(function (){
    // 공통 드롭다운 유틸 초기화 (필터 전용)
    initDropdown(".filter-dropdown", ".dropdown-toggle", ".dropdown-menu");

    // 옵션 선택 (필터 전용)
	$(".filter-dropdown .dropdown-menu li").on("click", function(e){
	    e.stopPropagation();
	    const value = ($(this).data("value") || "").toString().trim();
	    const text = $(this).text().trim();

	    const $dropdown = $(this).closest(".filter-dropdown");
	    const $label = $dropdown.find(".dropdown-label");

	    $label.text(text).attr("data-value", value).data("value", value);

	    $(this).siblings().removeClass("active");
	    $(this).addClass("active");

	    // ✅ 선택 후 닫기
	    $dropdown.find(".dropdown-menu").hide();
	    $dropdown.find(".dropdown-toggle").removeClass("active");

	    // 상태 업데이트
	    const filterType = $dropdown.find(".custom-dropdown-name").text().trim();
	    if (filterType === "종류") activeFilters.assetKind = value;
	    if (filterType === "제조사") activeFilters.company = value;
	    if (filterType === "상태") activeFilters.maintStatus = value;

	    renderActiveFilters();
	    loadList(1);
	});
});

function renderActiveFilters(){
    const $container = $(".active-filters");
    $container.find(".filter-tag").remove(); // 기존 태그 초기화
    
    const statusMap = {
        "COMPLETED": "점검완료",
        "IN_PROGRESS": "점검중"
    };
    
    Object.entries(activeFilters).forEach(([key, value]) => {
        const displayValue = statusMap[value] || value;
        if(value){
            $container.append(`
                <div class="filter-tag" data-type="${key}">
                    <span class="filter-name">${displayValue}</span>
                    <img alt="cancel-filter" src="/assets/asset/xbtn.svg" class="filter-cancel">
                </div>
            `);
        }
    });

    // 카운트 업데이트
    const count = Object.values(activeFilters).filter(v => v).length;
    $container.find(".count").text(count);

    bindFilterCancel();
}

// 필터 삭제  
function bindFilterCancel(){
    $(".filter-cancel").on("click", function(){
        const type = $(this).parent().data("type");
        activeFilters[type] = null;
        renderActiveFilters();
        loadList(1); // 필터 적용 다시
    });
}

$(document).on("click",  ".btn-reset", function(){
    activeFilters = { assetKind: null, company: null, maintStatus: null };
    renderActiveFilters();
    loadList(1);
});
