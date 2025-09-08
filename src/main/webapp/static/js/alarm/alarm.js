document.addEventListener("DOMContentLoaded", () => {
    console.log("Alarm script loaded");
    
    // 이벤트 위임 방식으로 동적 요소 처리
    document.addEventListener("click", (e) => {
        // 알람 버튼 클릭 시 모달 열기
        if (e.target.closest(".alarm-container")) {
            const modal = document.getElementById("alarmModal");
            if (modal) {
                modal.style.display = "block";
                console.log("Alarm modal opened");
            } else {
                console.error("Alarm modal not found");
            }
        }
        
        // 닫기 버튼 클릭 시 모달 닫기
        if (e.target.classList.contains("modal-close")) {
            const modal = document.getElementById("alarmModal");
            if (modal) {
                modal.style.display = "none";
                console.log("Alarm modal closed");
            }
        }
        
        // 모달 배경 클릭 시 닫기
        if (e.target.id === "alarmModal") {
            e.target.style.display = "none";
            console.log("Alarm modal closed by background click");
        }
    });
    
    // ESC 키로 모달 닫기
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") {
            const modal = document.getElementById("alarmModal");
            if (modal && modal.style.display === "block") {
                modal.style.display = "none";
                console.log("Alarm modal closed by ESC key");
            }
        }
    });
});