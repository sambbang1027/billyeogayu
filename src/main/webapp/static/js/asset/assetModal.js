// 작성자 : 김민호
/* ========== 공통 모달 유틸: assetModal ========== */
const assetModal = (() => {
  const $root   = document.getElementById('assetModal');
  const $title  = document.getElementById('assetModalTitle');
  const $desc   = document.getElementById('assetModalDesc');
  const $ok     = document.getElementById('assetModalOkBtn');
  const $cancel = document.getElementById('assetModalCancelBtn');

  let onOk = null, onCancel = null, bgClose = true;

  function open(opts = {}) {
    const {
      title = "알림",
      message = "",
      okText = "확인",
      cancelText = "취소",
      onOk: ok,
      onCancel: cancel,
      showCancel = true,
      bgClose: bgc = true
    } = opts;

    onOk     = ok || null;
    onCancel = cancel || null;
    bgClose  = !!bgc;

    $title.textContent  = title;
    $desc.textContent   = message;
    $ok.textContent     = okText || "확인";
    $cancel.textContent = cancelText || "취소";
    $cancel.style.display = showCancel ? "inline-block" : "none";

    $root.classList.remove("hidden");
    $root.style.display = "flex";
  }

  function close() {
    $root.style.display = "none";
    $root.classList.add("hidden");
    onOk = onCancel = null;
  }

  $ok.addEventListener("click", async () => {
    try { await onOk?.(); } catch(e) {}
    close();
  });

  $cancel.addEventListener("click", () => { onCancel?.(); close(); });

  $root.addEventListener("click", e => {
    if (e.target === $root && bgClose) {
      onCancel?.();
      close();
    }
  });

  window.addEventListener("keydown", e => {
    if (e.key === "Escape" && $root.style.display !== "none") {
      onCancel?.();
      close();
    }
  });

  return {
    confirm: open, // 확인 + 취소
    alert: (opts) => open({ ...(opts || {}), showCancel: false }) // 확인만
  };
})();

// CSRF 헤더 헬퍼
function getCsrfHeaders() {
  const h = document.querySelector('meta[name="_csrf_header"]');
  const t = document.querySelector('meta[name="_csrf"]');
  return (h && t) ? { [h.getAttribute('content')]: t.getAttribute('content') } : {};
}

// 오류 모달 헬퍼
function showAssetError(msg) {
  assetModal.alert({
    title: "주의 사항",
    message: msg || "처리 중 오류가 발생했습니다."
  });
}
