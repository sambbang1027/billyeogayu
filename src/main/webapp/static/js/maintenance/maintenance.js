/**
 *  필터링을 적용하여 비동기방식으로 데이터를 가져온다 
 */

$(function () {
	//console.log('페이지 로드 시작 ');
  loadList(1); // 처음 로드될 때 1페이지 데이터 호출
});

let sortOrder = "desc";
let sortField = "maintDate";  // 기본값

$(document).on("click", "th.sortable", function () {
  const $th = $(this);
  sortField = $th.data("sort"); 
  sortOrder = $th.attr("data-order"); // DOM 속성 읽기
  sortOrder = (sortOrder === "desc") ? "asc" : "desc";
  $th.attr("data-order", sortOrder);  // DOM 속성 갱신
  loadList(1);
});

// 필터 + 검색 값 수집
function getFilterValues(){
	return {
		searchType : $(".search-dropdown-label").data("value") || "", 
		keyword : $(".search-input").val().trim(),
		assetKind  : activeFilters.assetKind || "",
		company    : activeFilters.company || "",
		maintStatus: activeFilters.maintStatus || "",
		orderBy : sortField + (sortOrder === "asc" ? "Asc" : "Desc")
	};
}


// 리스트 로드 
function loadList(page = 1){
	const filters = getFilterValues();
	console.log('서버에 필터링 보내는 중 ' , filters);
	$.ajax({
		url : "/admin/maintenance/search",
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
						<img class = "complete-img" src="/assets/asset/canuse.svg">
						<span>점검완료</span>
				</div>
			`
		}else if(row.maintStatus === "IN_PROGRESS"){
			statusHtml = `
				<div class ="status-progress">
						<img class = "progress-img" src="/assets/asset/using.svg">
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
					<td>${row.assetKind}</td>
					<td>${row.assetName}</td>
					 <td>${row.maintDate && row.maintDate !== "null" ? row.maintDate : ""}</td>
					<td>${typeHtml}</td>
					<td>${statusHtml}</td>
					<td>${row.adminName}</td>
					<td>
							<button class="edit-btn"  data-id="${row.requestId}">
									<img src="/assets/maintenance/edit-btn.svg" alt="수정">
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

  // 이전 화살표
  if (currentPage > 1) {
    $pagination.append(`
      <a href="#" class="arrow prev" data-page="${currentPage - 1}">
        <img src="/assets/asset/left.svg" alt="이전">
      </a>
    `);
  }

  // === 페이지 번호 범위 계산 (5개 단위) ===
  const pageGroupSize = 5; // 한 그룹에 보여줄 페이지 개수
  const groupStart = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
  let groupEnd = groupStart + pageGroupSize - 1;
  if (groupEnd > totalPage) groupEnd = totalPage;

  // 번호 버튼 출력
  for (let i = groupStart; i <= groupEnd; i++) {
    $pagination.append(`
      <a href="#" class="page ${i === currentPage ? "active" : ""}" data-page="${i}">${i}</a>
    `);
  }

  // 다음 화살표
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

