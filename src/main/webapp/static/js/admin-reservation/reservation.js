/**
 *  예약페이지
 */
$(function () {
  loadRvList(1); // 처음 로드될 때 1페이지 데이터 호출
});

// 달력 함수 
$(document).ready(function() {
  const picker = flatpickr("#rv-inspectionDate", {
    dateFormat: "Y-m-d",
    locale: "ko",
    onChange: function(selectedDates, dateStr) {
      rvActiveFilters.startDate = dateStr;
      renderRvActiveFilters();
      loadRvList(1);
    }
  });

  $(".rv-calendar-icon").on("click", function() {
    picker.open();
  });
});

// 페이지당 개수 (고정값)
const rvPageSize = 10;

// 필터 값 수집
function getRvFilterValues(){
  return {
    assetKind : rvActiveFilters.assetKind || "",
    status    : rvActiveFilters.status || "",
    startDate : rvActiveFilters.startDate || ""
  };
}

// 리스트 로드 
function loadRvList(page = 1){
  const filters = getRvFilterValues();
  console.log('서버에 필터링 보내는 중 ' , filters);
  $.ajax({
    url : "/admin/reservations/list",
    type : "POST",
    data : {
      category  : filters.assetKind,
      status    : filters.status,
      startDate : filters.startDate,
      page      : page
    },
    success : function(res){
      renderTable(res.items, res.page, res.pageSize || rvPageSize);
      renderRvPagination(res.page, res.totalPages);
	  $('.rv-total-count').text(`총 ${res.totalCount} 건`);

    },
    error : function(xhr, status, err){
      console.error("리스트 로드 실패 -> ", err);
    }
  });
}

// 테이블 렌더링
function renderTable(list, currentPage, pageSize){
  const $tbody = $(".rv-reservation-table tbody");
  $tbody.empty();
  
  if(!list || list.length === 0){
    $tbody.append(`<tr><td colspan="8"> 데이터가 없습니다.</td></tr>`);
    return;
  }
  
  list.forEach((row, i) => {
    // 페이지별 이어지는 번호
    const rowNumber = (currentPage - 1) * pageSize + (i + 1);

    let statusHtml = "";
    let actionHtml = "";
    
    if(row.status === "PENDING"){
      statusHtml = `<div class="rv-status pending"><span>승인대기</span></div>`;
      actionHtml = `
        <div class="rv-btn-container">
          <div class="rv-btn-box">
            <button class="rv-btn-approve" data-id="${row.reservationId}">승인</button>        
          </div>
          <div class="rv-btn-box">
            <button class="rv-btn-reject" data-id="${row.reservationId}">반려</button>
          </div>
        </div>`;
    }else if(row.status === "APPROVED"){
      statusHtml = `<div class="rv-status using"><span>사용중</span></div>`;
      actionHtml = `
        <div class="rv-btn-box">
          <button class="rv-btn-complete" data-id="${row.reservationId}">반납</button>
        </div>`;
    }else if(row.status === "REJECTED"){
      statusHtml = `<div class="rv-status rejected"><span>반려</span></div>`;
      actionHtml = `
        <div class="rv-btn-box">
          <button class="rv-btn-view" data-id="${row.reservationId}">사유</button>
        </div>`;
    }else if(row.status === "COMPLETED"){
      statusHtml = `<div class="rv-status done"><span>반납완료</span></div>`;
    }

    let startTime = formatDateTime(row.startTime);
    let endTime = formatDateTime(row.endTime);
    let createdAt = row.createdAt;
    
    $tbody.append(`
      <tr>
        <td>${rowNumber}</td>
        <td>${row.assetName}</td>
        <td>${startTime}</td>
        <td>${endTime}</td>
        <td>${row.userName}</td>
        <td>${statusHtml}</td>
        <td>${createdAt[0]}.${String(createdAt[1]).padStart(2,"0")}.${String(createdAt[2]).padStart(2,"0")}</td>
        <td>${actionHtml}</td>
      </tr>
    `);
  });
}

function formatDateTime(arr) {
  if (!arr || arr.length < 5) return "";
  const [year, month, day, hour, minute] = arr;
  return (
    `${year}.${String(month).padStart(2, "0")}.${String(day).padStart(2, "0")} ` +
    `${String(hour).padStart(2, "0")}:${String(minute).padStart(2, "0")}`
  );
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

  // === 페이지 번호 범위 계산 (5개 단위) ===
  const pageGroupSize = 5;
  const groupStart = Math.floor((currentPage - 1) / pageGroupSize) * pageGroupSize + 1;
  let groupEnd = groupStart + pageGroupSize - 1;
  if (groupEnd > totalPage) groupEnd = totalPage;

  // 번호 버튼 출력
  for (let i = groupStart; i <= groupEnd; i++) {
    $pagination.append(`
      <a href="#" class="page ${i === currentPage ? "active" : ""}" data-page="${i}">${i}</a>
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
