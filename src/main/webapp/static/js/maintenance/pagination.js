/**
 * 페이지네이션 
 */
document.addEventListener("DOMContentLoaded", () => {
  const pagination = document.querySelector(".pagination");
  const pages = pagination.querySelectorAll(".page");
  const prevBtn = pagination.querySelector(".prev");
  const nextBtn = pagination.querySelector(".next");

  let currentPage = 1;
  const totalPages = pages.length;

  // 페이지 클릭
  pages.forEach(page => {
    page.addEventListener("click", e => {
      e.preventDefault();
      currentPage = parseInt(page.dataset.page);

      updatePagination();
      loadPageData(currentPage);
    });
  });

  // 이전 버튼
  prevBtn.addEventListener("click", e => {
    e.preventDefault();
    if (currentPage > 1) {
      currentPage--;
      updatePagination();
      loadPageData(currentPage);
    }
  });

  // 다음 버튼
  nextBtn.addEventListener("click", e => {
    e.preventDefault();
    if (currentPage < totalPages) {
      currentPage++;
      updatePagination();
      loadPageData(currentPage);
    }
  });

  // UI 업데이트 함수
  function updatePagination() {
    pages.forEach(p => p.classList.remove("active"));
    const activePage = pagination.querySelector(`.page[data-page="${currentPage}"]`);
    if (activePage) activePage.classList.add("active");
  }

  // 페이지 데이터 로딩 (실제로는 Ajax 연동 가능)
  function loadPageData(page) {
    console.log("현재 페이지:", page);
    // 👉 여기에 Ajax 호출 or 데이터 갱신 로직 추가
  }
});
