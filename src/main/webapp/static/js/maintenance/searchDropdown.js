/**
 *  검색창 드롭다운 
 */
$(document).ready(function() {
    // 드롭다운 열고닫기
    $(".search-dropdown-toggle").on("click", function(){
        $(this).siblings(".search-dropdown-menu").toggle(); 
    });

    // 항목 선택
    $(".search-dropdown-menu li").on("click", function(){
        const value = $(this).data("value");
        const text = $(this).text();
		const $label = $(this).closest(".search-dropdown").find(".search-dropdown-label");

        $(this).closest(".search-dropdown").find(".search-dropdown-label")
            .text(text)
            .attr("data-value", value);
		$label.data("value", value); 

        $(this).siblings().removeClass("active");
        $(this).addClass("active");

        $(this).parent().hide();
    });

    // 검색 버튼
    $("#btnSearch").on("click", function(e) {
        e.preventDefault();
        loadList(1);
    });

    // Enter 검색
    $("#keyword").on("keypress", function(e){
        if (e.which === 13){
            e.preventDefault();
            loadList(1);
        } 
    });
});


$(".search-dropdown-menu li").on("click", function(){
    const value = $(this).data("value");
    const text = $(this).text();

    const $label = $(this).closest(".search-dropdown").find(".search-dropdown-label");
    $label.text(text);
    $label.data("value", value);   // ← 중요: data-value 갱신
});



/*document.addEventListener("DOMContentLoaded", () => {
  const toggle = document.querySelector(".search-dropdown-toggle");
  const label = toggle.querySelector(".search-dropdown-label"); // 텍스트만 잡음
  const menu = document.querySelector(".search-dropdown-menu");

  toggle.addEventListener("click", () => {
    menu.style.display = menu.style.display === "block" ? "none" : "block";
  });

  menu.querySelectorAll("li").forEach(item => {
    item.addEventListener("click", () => {
      label.textContent = item.textContent;   // 이제 span만 바뀜
      menu.style.display = "none";
      menu.querySelectorAll("li").forEach(li => li.classList.remove("active"));
      item.classList.add("active");
    });
  });
});*/
