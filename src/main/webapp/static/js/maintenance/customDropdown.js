/**
 *  필터 안에 있는 드롭다운 
 */

let activeFilters = {
    assetKind: null,
    company: null,
    maintStatus: null
};


$(function (){
	//드롭다운 열기/ 닫기
	$(".custom-dropdown .dropdown-toggle").on("click", function(){
		const $menu = $(this).siblings(".dropdown-menu");
		
		// 다른 드롭다운 닫기
		$(".dropdown-menu").not($menu).hide();
		$(".dropdown-toggle").not(this).removeClass("active");
		
		// 현재 드롭다운 토글
		if($menu.is(":visible")){
			$menu.hide();
			$(this).removeClass("active");
		}else{
			$menu.show();
			$(this).addClass("active");
		}
	});
	
	
	// 옵션 선택 
	$(".custom-dropdown .dropdown-menu li").on("click" , function(){
		const value = ($(this).data("value") || "").toString().trim();
		const text = $(this).text().trim();
		
		const $dropdown = $(this).closest(".custom-dropdown");
		const $label = $dropdown.find(".dropdown-label");

		// 라벨 업데이트 (텍스트 + data-value)
		$label.text(text).attr("data-value", value);
		$label.data("value", value);  // ← 이거 해줘야 getFilterValues에서 읽힘
		
		// active 표시 갱신
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		
		//드롭다운 닫기
		$dropdown.find(".dropdown-menu").hide();
		$dropdown.find(".dropdown-toggle").removeClass("active");
		
		// 상태 업데이트 (드롭다운 이름에 따라)
	     const filterType = $dropdown.find(".custom-dropdown-name").text().trim();
	     if (filterType === "종류") activeFilters.assetKind = value;
	     if (filterType === "제조사") activeFilters.company = value;
	     if (filterType === "상태") activeFilters.maintStatus = value;
	
		// 적용된 필터 태그 렌더링
		renderActiveFilters();
		
		//리스트 새로 로드 (필터 반영)
		loadList(1);
	});
	
	// 바깥 클릭 시 닫기
	$(document).on("click", function(e){
		if(!$(e.target).closest(".custom-dropdown").length) {
			$(".dropdown-menu").hide();
			$(".dropdown-toggle").removeClass("active");
		}
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


function bindFilterCancel(){
    $(".filter-cancel").on("click", function(){
        const type = $(this).parent().data("type");
        activeFilters[type] = null;
        renderActiveFilters();
        loadList(1); // 필터 적용 다시
    });
}

$(".btn-reset").on("click", function(){
    activeFilters = { assetKind: null, company: null, maintStatus: null };
    renderActiveFilters();
    loadList(1);
});


