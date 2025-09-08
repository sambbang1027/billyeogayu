/**
 *  검색창 드롭다운 
 */
document.addEventListener("DOMContentLoaded", () => {
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
});
