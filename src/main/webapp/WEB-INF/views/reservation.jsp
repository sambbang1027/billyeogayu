<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>예약신청</title>

  <!-- 공통 헤더 CSS -->
  <link rel="stylesheet" href="<c:url value='/static/css/layout/user/header/style.css'/>"/>
  
  <!-- 예약 신청 전용 CSS -->
  <link rel="stylesheet" href="<c:url value='/static/css/layout/user/reservation/apply.css'/>"/>

  <!-- flatpickr CSS -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/themes/airbnb.css">
</head>
<body>

  <!-- 공통 헤더 -->
  <jsp:include page="/WEB-INF/views/layout/user/header.jsp"/>

  <!-- 예약신청 폼 -->
  <form id="reservationForm" action="<c:url value='/reservation/apply'/>" method="post" novalidate>
    <c:if test="${not empty _csrf}">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </c:if>

    <!-- hidden 필드 -->
    <input type="hidden" name="assetId" value="${assetId}">
    <input type="hidden" name="reserveStartDate" id="reserveStartDate">
    <input type="hidden" name="reserveEndDate"   id="reserveEndDate">
    <input type="hidden" name="reserveStartTime" id="reserveStartTime">
    <input type="hidden" name="reserveEndTime"   id="reserveEndTime">

    <main class="reservation-apply">
      <div class="container">

        <header class="page-head">
          <h1>농기계 예약</h1>
        </header>

        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
          <div class="error-message">
            <div class="error-icon">⚠️</div>
            <div class="error-content">
              <strong>오류</strong>
              <p>${error}</p>
            </div>
          </div>
        </c:if>

        <!-- 사용조건 -->
        <section class="card">
          <h2 class="card-title">사용조건 및 준수사항</h2>
          <div class="card-body">
            <ul class="bullets">
              <li>임차인은 농기계를 내 것처럼 아껴서 사용한다.</li>
              <li>사용 후 보관시는 깨끗이 세척한 후 안전한 창고 내에 보관한다.(1일 이상 사용할 때)</li>
              <li>임차 후 발생한 고장수리비는 임차인이 부담한다.</li>
              <li>운전미숙, 부주의 등 임차인 과실로 발생한 고장은 임차인이 책임 수리한다.</li>
              <li>임대기간 중 유지·보수·연료비 등은 임차인이 부담한다.</li>
              <li>운반·사용 중 발생한 사고는 임차인이 책임진다.</li>
              <li>분실·파손 시 임차인이 보상한다.</li>
              <li>임대기간 만료 시 깨끗이 세척 후 반납한다.</li>
            </ul>
          </div>
        </section>

        <!-- 신청인 -->
        <section class="card">
          <h2 class="card-title">신청인</h2>
          <div class="card-body">
            <p class="note"><b class="req">*</b> 주소 입력칸에는 <b>농기계를 이용할 주소</b>를 작성해주세요. (사용자 주소와 다름)</p>

            <div class="form-grid">
              <div class="row">
                <label class="col-l" for="applName">*성명</label>
                <div class="col-r"><input id="applName" type="text" value="${applicant.name}" readonly></div>
              </div>
              <div class="row">
                <span class="col-l">*연락처</span>
                <div class="col-r phone">
                  <input type="text" value="${phone1}" readonly><span>-</span>
                  <input type="text" value="${phone2}" readonly><span>-</span>
                  <input type="text" value="${phone3}" readonly>
                </div>
              </div>
              <div class="row">
                <label class="col-l" for="applBirth">*생년월일</label>
                <div class="col-r"><input id="applBirth" type="text" value="${applicant.birth}" readonly></div>
              </div>

              <!-- 사용 장소 주소 -->
              <div class="row">
                <label class="col-l" for="zipcode">*주소</label>
                <div class="col-r zip">
                  <input id="zipcode" name="zipcode" type="text" value="${applicant.zipcode}" readonly>
                  <button type="button" id="btnPost">우편번호 검색</button>
                </div>
              </div>
              <div class="row">
                <label class="col-l" for="addr1">기본주소</label>
                <div class="col-r"><input id="addr1" name="addr1" type="text" value="${applicant.addr1}" readonly></div>
              </div>
              <div class="row">
                <label class="col-l" for="addr2">상세주소</label>
                <div class="col-r"><input id="addr2" name="addr2" type="text" value="${applicant.addr2}"></div>
              </div>
            </div>
          </div>
        </section>

        <!-- 농기계 정보 -->
        <section class="card">
          <h2 class="card-title">농기계 정보</h2>
          <div class="card-body">
            <div class="asset">
              <div class="asset-thumb">
                <c:choose>
                  <c:when test="${empty assetImage}">이미지 준비중</c:when>
                  <c:otherwise>
                    <img src="<c:url value='${assetImage}'/>" alt="${assetName} 이미지">
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="asset-info">
                <p class="asset-note">※ 예약할 농기계 정보를 다시 확인하세요.</p>
                <dl class="asset-spec stacked">
                  <div class="field"><dt>*장비명</dt><dd>${assetName}</dd></div>
                  <div class="field"><dt>*모델명</dt><dd>${assetModel}</dd></div>
                  <div class="field"><dt>*제조사</dt><dd>${assetMaker}</dd></div>
                </dl>
              </div>
            </div>
          </div>
        </section>

        <!-- 신청상세 -->
        <section class="card">
          <h2 class="card-title">신청상세</h2>
          <div class="card-body">
            <p class="note">
              <b class="req">*</b> 예약기간 선택<br>
              - 다른 사람이 선택한 날짜는 예약 불가<br>
              - 시작일/종료일 + 시간을 반드시 선택<br>
              - 반납은 종료일 오후 18시 전까지
            </p>

            <div class="picker-box">
              <div class="calendar-area"><div id="rangeCalendar"></div></div>
              <div class="date-summary">
                <div><label>예약(시작)</label><input id="startSummary" type="text" readonly></div>
                <div><label>반납(종료)</label><input id="endSummary" type="text" readonly></div>
              </div>

              <div class="time-area">
                <div class="time-head">
                  <span>시간 선택</span>
                  <div class="tabs" id="timeTabs">
                    <button class="tab active" data-target="start" type="button">시작 시간</button>
                    <button class="tab" data-target="end" type="button">종료 시간</button>
                  </div>
                </div>
                <div class="time-scroll">
                  <button class="arrow left" id="timePrev" type="button">&lsaquo;</button>
                  <div class="time-track" id="timeTrack"></div>
                  <button class="arrow right" id="timeNext" type="button">&rsaquo;</button>
                </div>
                <div class="picked">
                  <div><label>선택된 시작</label><output id="pickedStart">-</output></div>
                  <div><label>선택된 종료</label><output id="pickedEnd">-</output></div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 사용 목적 -->
        <section class="card">
          <h2 class="card-title">사용 목적</h2>
          <div class="card-body">
            <p class="note"><b class="req">*</b> 농기계 사용 목적을 입력해주세요. (100자 이내)</p>
            <textarea id="purpose" name="purpose" class="purpose" maxlength="100"
              placeholder="ex) 논 갈이 및 파종 준비를 위해 경운 작업에 사용하고자 함."><c:out value="${purpose}"/></textarea>
          </div>
        </section>

      </div>
    </main>

    <!-- 페이지 하단 버튼 -->
    <div class="page-actions">
      <div class="container">
        <button type="button" class="btn ghost" onclick="location.href='<c:url value="/"/>'">취소</button>
        <button id="applyBtn" type="button" class="btn primary">예약신청</button>
      </div>
    </div>
  </form>

  <!-- JavaScript 라이브러리들 -->
  <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
  <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/ko.js"></script>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

  <script>
    // 페이지 로드 시 CSS 로딩 확인
    console.log('CSS 파일 경로 확인:');
    console.log('Header CSS:', '<c:url value="/static/css/layout/user/header/style.css"/>');
    console.log('Apply CSS:', '<c:url value="/static/css/layout/user/reservation/apply.css"/>');
    
    const startSummary = document.getElementById("startSummary");
    const endSummary   = document.getElementById("endSummary");
    
    // 블록된 날짜들을 저장할 배열
    let blockedDates = [];

    // 블록된 날짜 API 호출
    function loadBlockedDates() {
      const assetId = document.querySelector('input[name="assetId"]').value;
      if (!assetId) return;
      
      const now = new Date();
      const oneYearLater = new Date();
      oneYearLater.setFullYear(now.getFullYear() + 1);
      
      const fromDate = now.getFullYear() + '-' + 
                      String(now.getMonth() + 1).padStart(2, '0') + '-' + 
                      String(now.getDate()).padStart(2, '0') + 'T00:00:00';
      
      const toDate = oneYearLater.getFullYear() + '-' + 
                    String(oneYearLater.getMonth() + 1).padStart(2, '0') + '-' + 
                    String(oneYearLater.getDate()).padStart(2, '0') + 'T23:59:59';
      
      const baseUrl = window.location.origin;
      const params = new URLSearchParams({ assetId, from: fromDate, to: toDate });
      const fullUrl = baseUrl + '/api/reservation/blocked?' + params.toString();
      
      fetch(fullUrl)
        .then(response => response.ok ? response.json() : Promise.reject('API 호출 실패'))
        .then(blockedRanges => {
          blockedDates = [];
          if (blockedRanges && Array.isArray(blockedRanges)) {
            blockedRanges.forEach(range => {
              try {
                const start = new Date(range.startAt);
                const end = new Date(range.endAt);
                for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
                  blockedDates.push(new Date(d));
                }
              } catch (error) {
                console.error('날짜 파싱 오류:', error);
              }
            });
          }
          if (cal) cal.redraw();
        })
        .catch(error => {
          console.error('블록된 날짜 로딩 실패:', error);
          blockedDates = [];
        });
    }

    const cal = flatpickr("#rangeCalendar", {
      mode: "range",
      inline: true,
      dateFormat: "Y-m-d",
      locale: flatpickr.l10ns.ko,
      minDate: "today",
      disable: [
        function(date) {
          return blockedDates.some(blockedDate => 
            date.getFullYear() === blockedDate.getFullYear() &&
            date.getMonth() === blockedDate.getMonth() &&
            date.getDate() === blockedDate.getDate()
          );
        }
      ],
      onChange: (selectedDates, _, instance) => {
        const [s, e] = selectedDates;
        startSummary.value = s ? instance.formatDate(s, "Y-m-d") : "";
        endSummary.value   = e ? instance.formatDate(e, "Y-m-d") : "";
        document.getElementById("reserveStartDate").value = startSummary.value;
        document.getElementById("reserveEndDate").value   = endSummary.value;
      }
    });
    
    // 페이지 로드 시 블록된 날짜 로딩
    loadBlockedDates();

    // 시간 버튼 생성
    const times = [];
    for (let h = 9; h <= 18; h++) {
      if (h === 18) { times.push("18:00"); break; }
      times.push((h < 10 ? "0" + h : h) + ":00", (h < 10 ? "0" + h : h) + ":30");
    }
    const filtered = times.filter(t => !["12:00","12:30","13:00"].includes(t));
    const timeTrack = document.getElementById("timeTrack");
    filtered.forEach(t => {
      const btn = document.createElement("button");
      btn.type = "button";
      btn.className = "time-btn";
      btn.dataset.time = t;
      btn.textContent = t;
      timeTrack.appendChild(btn);
    });

    const timeTabs = document.getElementById("timeTabs");
    let activeTarget = "start", startTime = null, endTime = null;
    
    timeTabs.addEventListener("click", e => {
      const tab = e.target.closest(".tab");
      if (!tab) return;
      [...timeTabs.children].forEach(el => el.classList.remove("active"));
      tab.classList.add("active");
      activeTarget = tab.dataset.target;
    });

    timeTrack.addEventListener("click", e => {
      const btn = e.target.closest(".time-btn");
      if (!btn) return;
      const t = btn.dataset.time;
      
      if (activeTarget === "start") {
        startTime = t;
        document.getElementById("pickedStart").textContent = t;
      } else {
        endTime = t;
        document.getElementById("pickedEnd").textContent = t;
      }
      
      // 선택 상태 업데이트
      timeTrack.querySelectorAll(".time-btn").forEach(b => b.classList.remove("selected-start","selected-end"));
      if (startTime) {
        const startBtn = timeTrack.querySelector(`[data-time="${startTime}"]`);
        if (startBtn) startBtn.classList.add("selected-start");
      }
      if (endTime) {
        const endBtn = timeTrack.querySelector(`[data-time="${endTime}"]`);
        if (endBtn) endBtn.classList.add("selected-end");
      }
      
      document.getElementById("reserveStartTime").value = startTime || "";
      document.getElementById("reserveEndTime").value = endTime || "";
    });

    // 시간 스크롤 버튼
    document.getElementById("timePrev").addEventListener("click", () => {
      timeTrack.scrollBy({ left: -200, behavior: 'smooth' });
    });
    document.getElementById("timeNext").addEventListener("click", () => {
      timeTrack.scrollBy({ left: 200, behavior: 'smooth' });
    });

    // Daum 우편번호 검색
    document.getElementById("btnPost").addEventListener("click", () => {
      new daum.Postcode({
        oncomplete: function(data) {
          document.getElementById("zipcode").value = data.zonecode || "";
          document.getElementById("addr1").value = data.roadAddress || data.jibunAddress || "";
          document.getElementById("addr2").focus();
        }
      }).open();
    });

    // 시간을 분으로 변환
    function timeToMinutes(timeStr) {
      if (!timeStr) return -1;
      const [hours, minutes] = timeStr.split(':').map(Number);
      return hours * 60 + minutes;
    }

    // 폼 제출 유효성 검사
    document.getElementById("applyBtn").addEventListener("click", () => {
      const [sDate, eDate] = cal.selectedDates;
      if (!sDate || !eDate) {
        alert("날짜를 선택하세요.");
        return;
      }
      if (!startTime || !endTime) {
        alert("시간을 선택하세요.");
        return;
      }

      // 같은 날인 경우 시간 순서 검증
      const isSameDay = startSummary.value === endSummary.value;
      if (isSameDay) {
        const startMinutes = timeToMinutes(startTime);
        const endMinutes = timeToMinutes(endTime);
        if (endMinutes <= startMinutes) {
          alert("같은 날 예약의 경우 종료 시간은 시작 시간보다 늦어야 합니다.");
          return;
        }
      }

      // 주소 입력 확인
      if (!document.getElementById("zipcode").value.trim() ||
          !document.getElementById("addr1").value.trim()) {
        alert("사용할 주소를 입력하세요.");
        return;
      }

      // 사용 목적 입력 확인
      if (!document.getElementById("purpose").value.trim()) {
        alert("사용 목적을 입력하세요.");
        return;
      }

      document.getElementById("reservationForm").submit();
    });
  </script>
</body>
</html>