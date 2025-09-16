/**
 * 공통 Dropdown 유틸
 * - 열기/닫기
 * - 바깥 클릭 시 닫기
 */
// 작성자 : 서샘이

function initDropdown(scopeSelector, toggleSelector, menuSelector, activeClass = "active") {
    // 열기/닫기
    $(document).on("click", `${scopeSelector} ${toggleSelector}`, function(e){
        e.stopPropagation();
        const $menu = $(this).siblings(menuSelector);

        $(`${scopeSelector} ${menuSelector}`).not($menu).hide();
        $(`${scopeSelector} ${toggleSelector}`).not(this).removeClass(activeClass);

        $menu.toggle();
        $(this).toggleClass(activeClass, $menu.is(":visible"));
    });

    // 바깥 클릭 시 닫기
    $(document).on("click", function(e){
        if(!$(e.target).closest(scopeSelector).length) {
            $(`${scopeSelector} ${menuSelector}`).hide();
            $(`${scopeSelector} ${toggleSelector}`).removeClass(activeClass);
        }
    });
}
