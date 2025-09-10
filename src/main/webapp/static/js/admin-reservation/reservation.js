/**
 *  예약페이지 
 */


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





// 페이지네이션 렌더링
function renderPagination(currentPage, totalPage){
	const $pagination = $(".pagination");
	$pagination.empty();
	// 이전
	  if (currentPage > 1) {
	    $pagination.append(`
	      <a href="#" class="arrow prev" data-page="${currentPage - 1}">
	        <img src="/assets/asset/left.svg" alt="이전">
	      </a>
	    `);
	  }

	  // 번호
	  for (let i = 1; i <= totalPage; i++) {
	    $pagination.append(`
	      <a href="#" class="page ${i === currentPage ? "active" : ""}" data-page="${i}">${i}</a>
	    `);
	  }

	  // 다음
	  if (currentPage < totalPage) {
	    $pagination.append(`
	      <a href="#" class="arrow next" data-page="${currentPage + 1}">
	        <img src="/assets/asset/right.svg" alt="다음">
	      </a>
	    `);
	  }

	  // 페이지 버튼 이벤트 바인딩
	  $pagination.find("a").on("click", function (e) {
	    e.preventDefault();
	    loadList($(this).data("page"));
	  });
	}