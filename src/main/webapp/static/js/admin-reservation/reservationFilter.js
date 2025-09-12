/**
 *  예약페이지 필터 드롭다운 (rv 전용)
 */
let rvActiveFilters = {
    assetKind: null,
    status: null,
    startDate: null
};

$(function (){
    // 필터 드롭다운 열기/닫기
    $(".rv-filter-dropdown .rv-dropdown-toggle").on("click", function(e){
        e.stopPropagation(); // 모달 드롭다운에 영향 안 가게 차단

        const $menu = $(this).siblings(".rv-dropdown-menu");
        
        // 다른 필터 드롭다운 닫기
        $(".rv-filter-dropdown .rv-dropdown-menu").not($menu).hide();
        $(".rv-filter-dropdown .rv-dropdown-toggle").not(this).removeClass("active");
        
        // 현재 드롭다운 토글
        if($menu.is(":visible")){
            $menu.hide();
            $(this).removeClass("active");
        } else {
            $menu.show();
            $(this).addClass("active");
        }
    });
    
    // 옵션 선택 (예약 전용)
    $(".rv-filter-dropdown .rv-dropdown-menu li").on("click" , function(){
        const value = ($(this).data("value") || "").toString().trim();
        const text = $(this).text().trim();
        
        const $dropdown = $(this).closest(".rv-filter-dropdown");
        const $label = $dropdown.find(".rv-dropdown-label");

        // 라벨 업데이트
        $label.text(text).attr("data-value", value);
        $label.data("value", value);
        
        // active 표시 갱신
        $(this).siblings().removeClass("rv-active");
        $(this).addClass("rv-active");
        
        // 드롭다운 닫기
        $dropdown.find(".rv-dropdown-menu").hide();
        $dropdown.find(".rv-dropdown-toggle").removeClass("active");
        
        // 상태 업데이트 (드롭다운 이름에 따라 구분)
        const filterType = $dropdown.find(".rv-custom-dropdown-name").text().trim();
        if (filterType === "종류") rvActiveFilters.assetKind = value;
        if (filterType === "상태") rvActiveFilters.status = value;
        if (filterType ==="시작일") rvActiveFilters.startDate = value;
    
        // 적용된 필터 태그 렌더링
        renderRvActiveFilters();
        
        // 리스트 새로 로드 (필터 반영)
        loadRvList(1);
    });
    
    // 바깥 클릭 시 필터 드롭다운 닫기
    $(document).on("click", function(e){
        if(!$(e.target).closest(".rv-filter-dropdown").length) {
            $(".rv-filter-dropdown .rv-dropdown-menu").hide();
            $(".rv-filter-dropdown .rv-dropdown-toggle").removeClass("active");
        }
    });
});

function renderRvActiveFilters(){
    const $container = $(".rv-active-filters");
    $container.find(".rv-filter-tag").remove(); // 기존 태그 초기화
    
    const statusMap = {
        "PENDING": "승인대기",
        "APPROVED": "사용중",
        "REJECTED": "반려",
        "COMPLETED": "반납완료"
    };
    
    Object.entries(rvActiveFilters).forEach(([key, value]) => {
        const displayValue = statusMap[value] || value;
        if(value){
            $container.append(`
                <div class="rv-filter-tag" data-type="${key}">
                    <span class="rv-filter-name">${displayValue}</span>
                    <img alt="cancel-filter" src="/assets/asset/xbtn.svg" class="rv-filter-cancel">
                </div>
            `);
        }
    });

    // 카운트 업데이트
    const count = Object.values(rvActiveFilters).filter(v => v).length;
    $container.find(".rv-count").text(count);

    bindRvFilterCancel();
}

// 필터 삭제  
function bindRvFilterCancel(){
    $(".rv-filter-cancel").on("click", function(){
        const type = $(this).parent().data("type");
        rvActiveFilters[type] = null;
        renderRvActiveFilters();
        loadRvList(1); // 필터 적용 다시
    });
}

$(document).on("click",  ".rv-btn-reset", function(){
    rvActiveFilters = { assetKind: null, status: null, startDate: null };
    renderRvActiveFilters();
    loadRvList(1);
});
