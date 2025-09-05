/**
 *  커스텀 드롭다운 
 */

document.addEventListener("DOMContentLoaded", () => {
  const toggle = document.querySelector(".dropdown-toggle");
  const menu = document.querySelector(".dropdown-menu");

  toggle.addEventListener("click", () => {
    menu.style.display = menu.style.display === "block" ? "none" : "block";
  });

  menu.querySelectorAll("li").forEach(item => {
    item.addEventListener("click", () => {
      toggle.textContent = item.textContent;
      menu.style.display = "none";
      menu.querySelectorAll("li").forEach(li => li.classList.remove("active"));
      item.classList.add("active");
    });
  });
});
