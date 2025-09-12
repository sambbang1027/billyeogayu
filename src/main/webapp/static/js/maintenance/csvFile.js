$(function(){
  $(".btn-download").on("click", function(){
    let sp = new URLSearchParams();

    // 1) 검색어
    const keyword = $("#keyword").val();
    if (keyword) {
      sp.set("keyword", keyword);
    }

    // 2) 종류 (assetKind)
    const kindVal = $(".filter-dropdown .custom-dropdown-name:contains('종류')")
                      .closest(".filter-dropdown")
                      .find(".dropdown-label")
                      .data("value");
    if (kindVal) {
      sp.set("assetKind", kindVal);
    }

    // 3) 제조사 (company)
    const companyVal = $(".filter-dropdown .custom-dropdown-name:contains('제조사')")
                         .closest(".filter-dropdown")
                         .find(".dropdown-label")
                         .data("value");
    if (companyVal) {
      sp.set("company", companyVal);
    }

    // 4) 상태 (maintStatus)
    const statusVal = $(".filter-dropdown .custom-dropdown-name:contains('상태')")
                        .closest(".filter-dropdown")
                        .find(".dropdown-label")
                        .data("value");
    if (statusVal) {
      sp.set("maintStatus", statusVal);
    }

    // 최종 URL
    let url = contextPath + "/admin/maintenance/export";
    if (sp.toString()) {
      url += "?" + sp.toString();
    }

    console.log("CSV export URL:", url);
    window.location.href = url;
  });
});
