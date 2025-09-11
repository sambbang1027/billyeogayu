/**
 *  예약페이지 (
 */
$(function () {
  //console.log('페이지 로드 시작 ');
  loadRvList(1); // 처음 로드될 때 1페이지 데이터 호출
});

// 달력 함수 
$(document).ready(function() {
  // flatpickr 초기화
  const picker = flatpickr("#rv-inspectionDate", {
    dateFormat: "Y-m-d",
    locale: "ko"
  });

  // 아이콘 클릭 → 달력 열기
  $(".rv-calendar-icon").on("click", function() {
    picker.open();
  });
});

// 필터 값 수집
function getRvFilterValues(){
  return {
    assetKind   : rvActiveFilters.assetKind || "",
    status       : rvActiveFilters.status || "",
    createdAt : rvActiveFilters.createdAt || ""
  };
}


// 리스트 로드 
function loadList(page = 1){
	const filters = getFilterValues();
	console.log('서버에 필터링 보내는 중 ' , filters);
	$.ajax({
		url : "/admin-reservation/search",
		type : "GET",
		data : {...filters, page : page}, //DTO 매핑
		success : function(res){
		//	console.log(res);
			renderTable(res.list);
			renderPagination(res.currentPage, res.totalPage);
		},
		error : function(xhr, status, err){
			console.error("리스트 로드 실패 -> ", err);
		}
	});
}



// 테이블 렌더링
function renderTable(list){
	const $tbody = $(".rv-reservation-table tbody");
	$tbody.empty();
	
	if(!list || list.length === 0){
		$tbody.append(`<tr><td colspan="7"> 데이터가 없습니다.</td></tr>`);
		return;
	}
	
	list.forEach((row, i) => {
		let statusHtml = "";
		let actionHtml = "";
		
		if(row.status === "PENDING"){
			statusHtml = `
			<div class="rv-status pending">
			    <span>승인대기</span>                    	
			</div>
			`
			actionHtml = `
			<div class="rv-btn-container">
                <div class="rv-btn-box">
                    <button class="rv-btn-approve" data-id="${row.reservationId}>승인</button>        
                </div>
                <div class="rv-btn-box">
                    <button class="rv-btn-reject" data-id="${row.reservationId}>반려</button>
                </div>
            </div>
			`
		}else if(row.status === "USING"){
			statusHtml = `
			<div class="rv-status using">
				<span>사용중</span>                    	
			</div>
			`
			actionHtml = `
			<div class="rv-btn-box">
			    <button class="rv-btn-complete" data-id="${row.reservationId}>반납</button>
			</div>			
			`
		}else if(row.status === "REJECTED"){
			statusHtml = `
			<div class="rv-status rejected">
					 <span>반려</span>                    	
			</div>
			`
			actionHtml=`
			<div class="rv-btn-box">
			    <button class="rv-btn-view" data-id="${row.reservationId}>상세</button>
			</div>
			`
		}else if(row.status === "DONE"){
			statusHtml = `
			<div class="rv-status done">
					 <span>반납완료</span>                    	
			</div>
			`
		}
		
		
		$tbody.append(`
			<tr>
					<td>${i + 1}</td>
					<td>${row.assetName}</td>
					 <td>${row.startDate}</td>
					<td>${row.endDate}</td>
					<td>${row.userName}</td>
					<td>${statusHtml}</td>
					<td>${row.createdAt}</td>
					<td>${actionHtml}</td>
			</tr>
			`);
	});
}





// 페이지네이션 렌더링
function renderRvPagination(currentPage, totalPage){
  const $pagination = $(".rv-pagination");
  $pagination.empty();

  // 이전
  if (currentPage > 1) {
    $pagination.append(`
      <a href="#" class="rv-arrow rv-prev" data-page="${currentPage - 1}">
        <img src="/assets/asset/left.svg" alt="이전">
      </a>
    `);
  }

  // 번호
  for (let i = 1; i <= totalPage; i++) {
    $pagination.append(`
      <a href="#" class="rv-page ${i === currentPage ? "active" : ""}" data-page="${i}">${i}</a>
    `);
  }

  // 다음
  if (currentPage < totalPage) {
    $pagination.append(`
      <a href="#" class="rv-arrow rv-next" data-page="${currentPage + 1}">
        <img src="/assets/asset/right.svg" alt="다음">
      </a>
    `);
  }

  // 페이지 버튼 이벤트 바인딩
  $pagination.find("a").on("click", function (e) {
    e.preventDefault();
    loadRvList($(this).data("page"));
  });
}
