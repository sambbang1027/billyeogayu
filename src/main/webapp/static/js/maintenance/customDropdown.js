/**
 *  필터 안에 있는 드롭다운 
 */

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".custom-dropdown").forEach(dropdown => {
    const toggle = dropdown.querySelector(".dropdown-toggle");
    const label = dropdown.querySelector(".dropdown-label");
    const menu = dropdown.querySelector(".dropdown-menu");

    // 버튼 클릭 시 열기/닫기
    toggle.addEventListener("click", () => {
      const isOpen = menu.style.display === "block";
      document.querySelectorAll(".dropdown-menu").forEach(m => m.style.display = "none"); // 다른 드롭다운 닫기
      document.querySelectorAll(".dropdown-toggle").forEach(t => t.classList.remove("active"));

      if (!isOpen) {
        menu.style.display = "block";
        toggle.classList.add("active");
      }
    });

    // 옵션 선택
    menu.querySelectorAll("li").forEach(item => {
      item.addEventListener("click", () => {
        label.textContent = item.textContent;
        menu.style.display = "none";
        toggle.classList.remove("active");
        menu.querySelectorAll("li").forEach(li => li.classList.remove("active"));
        item.classList.add("active");
      });
    });
  });

  // 바깥 클릭 시 닫기
  document.addEventListener("click", e => {
    if (!e.target.closest(".custom-dropdown")) {
      document.querySelectorAll(".dropdown-menu").forEach(m => m.style.display = "none");
      document.querySelectorAll(".dropdown-toggle").forEach(t => t.classList.remove("active"));
    }
  });
});
