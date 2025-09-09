/**
 *  필터링을 적용하여 비동기방식으로 데이터를 가져온다 
 */

$(function () {
	//console.log('페이지 로드 시작 ');
	getFilterList();
  loadList(1); // 처음 로드될 때 1페이지 데이터 호출
});

function getFilterList(){
	$.ajax({
		url: "/maintenance/filter",
		type : "GET",
		success : function(res){
			console.log(res);
		}
	})
}


// 필터 + 검색 값 수집
function getFilterValues(){
	return {
		searchType : $(".search-dropdown-label").data("value") || "", 
		keyword : $(".search-input").val().trim(),
		assetKind  : activeFilters.assetKind || "",
		company    : activeFilters.company || "",
		maintStatus: activeFilters.maintStatus || ""
	};
}


// 리스트 로드 
function loadList(page = 1){
	const filters = getFilterValues();
	console.log('서버에 필터링 보내는 중 ' , filters);
	$.ajax({
		url : "/maintenance/search",
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
	const $tbody = $(".maintenance-table tbody");
	$tbody.empty();
	
	if(!list || list.length === 0){
		$tbody.append(`<tr><td colspan="7"> 데이터가 없습니다.</td></tr>`);
		return;
	}
	
	list.forEach((row, i) => {
		let statusHtml = "";
		
		if(row.maintStatus === "COMPLETED"){
			statusHtml = `
				<div class ="status-complete">
						<img class = "complete-img" src="/billyeogayu/assets/asset/canuse.svg">
						<span>점검완료</span>
				</div>
			`
		}else if(row.maintStatus === "IN_PROGRESS"){
			statusHtml = `
				<div class ="status-progress">
						<img class = "progress-img" src="/billyeogayu/assets/asset/using.svg">
						<span>점검중</span>
				</div>
			`
		}
		
		let typeHtml = "";
		if(row.maintType === "EMERGENCY"){
			typeHtml = `<text>긴급점검</text>`
		}else if(row.maintType === "REGULAR"){
			typeHtml= `<text>정기점검</text>`

		}
		
		
		$tbody.append(`
			<tr>
					<td>${i + 1}</td>
					<td>${row.assetName}</td>
					<td>${row.maintDate}</td>
					<td>${typeHtml}</td>
					<td>${statusHtml}</td>
					<td>${row.adminName}</td>
					<td>
							<button class="edit-btn"  data-id="${row.requestId}">
									<img src="/billyeogayu/assets/maintenance/edit-btn.svg" alt="수정">
							</button>
					</td>
			</tr>
			`);
	});
}


// 페이지네이션 렌더링
function renderPagination(currentPage, totalPage){
	const $pagination = $(".pagination");
	$pagination.empty();
	// 이전
	  if (currentPage > 1) {
	    $pagination.append(`
	      <a href="#" class="arrow prev" data-page="${currentPage - 1}">
	        <img src="/billyeogayu/assets/asset/left.svg" alt="이전">
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
	        <img src="/billyeogayu/assets/asset/right.svg" alt="다음">
	      </a>
	    `);
	  }

	  // 페이지 버튼 이벤트 바인딩
	  $pagination.find("a").on("click", function (e) {
	    e.preventDefault();
	    loadList($(this).data("page"));
	  });
	}

