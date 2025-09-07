/**
 * 기록 상세/수정 모달 띄우기 (jQuery)
 */
/**$(document).ready(function() {
  // 수정 버튼 클릭 → 상태값 확인 후 모달 분기
  $(document).on("click", ".edit-btn", function() {
    const $tr = $(this).closest("tr"); // 해당 행
    const statusText = $tr.find(".status-complete span, .status-progress span").text().trim();
    const id = $tr.find(".status-complete, .status-progress").data("maintenance-id");

    // 상태값에 따라 JSP 분기
    if (statusText === "점검중") {
      openModal(id, "/maintenance/detail/", "inspectionModal");
    } else if (statusText === "점검완료") {
      openModal(id, "/maintenance/edit/", "inspectionEditModal");
    }
  });

  // 공통 모달 열기 함수
  function openModal(id, urlPrefix, modalId) {
    $.ajax({
      url: urlPrefix + id,
      type: "GET",
      success: function(html) {
        // 기존 모달 제거 (중복 방지)
        $("#" + modalId).remove();

        // 새 모달 삽입
        $("#modal-container").html(html);

        // 닫기 버튼 이벤트 바인딩
        $(document).on("click", "#" + modalId + " .close-btn", function() {
          $("#" + modalId).remove();
        });
      },
      error: function(xhr, status, error) {
        console.error("모달 로딩 실패:", error);
      }
    });
  }
});
*/

$(document).ready(function() {
  // 수정 버튼 클릭 → 상태 확인 후 모달 띄우기
  $(document).on("click", ".edit-btn", function() {
    const $tr = $(this).closest("tr");
    const statusText = $tr.find(".status-complete span, .status-progress span").text().trim();

    if (statusText === "점검중") {
	console.log('상태는 점검중 -> 모달 열림 ')
      $("#inspectionModal").show();
    } else if (statusText === "점검완료") {
      $("#inspectionEditModal").show();
    }
  });

  // 닫기 버튼 누르면 해당 모달만 닫기
  $(document).on("click", ".close-btn", function() {
    $(this).closest(".inspection-modal").hide();
  });
});

