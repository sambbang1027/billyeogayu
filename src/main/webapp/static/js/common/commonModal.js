/**
 *  공통 모달 함수 
 */

// 공통 모달 함수
function showModal({title, message, buttons}) {
  $("#modalTitle").text(title);
  $("#modalMessage").text(message);

  $("#modalFooter").empty();

  buttons.forEach(btn => {
    const $btn = $("<button>")
      .addClass(btn.className || "common-btn-outline")
      .text(btn.text)
      .on("click", function() {
        $("#commonModal").fadeOut();
        if(btn.onClick) btn.onClick();
      });
    $("#modalFooter").append($btn);
  });

  $("#commonModal").fadeIn();
}

// 알림용 헬퍼
function showAlert(message, onOk) {
  showModal({
    title: "알림",
    message: message,
    buttons: [
      { text: "확인", className: "common-btn-primary", onClick: onOk }
    ]
  });
}

// 확인용 헬퍼
function showConfirm(message, onOk, onCancel) {
  showModal({
    title: "확인",
    message: message,
    buttons: [
      { text: "확인", className: "common-btn-primary", onClick: onOk },
      { text: "취소", className: "common-btn-outline", onClick: onCancel }
    ]
  });
}
