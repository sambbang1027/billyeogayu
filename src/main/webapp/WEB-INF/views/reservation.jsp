<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<link rel="stylesheet"
	href="<c:url value='/static/css/layout/user/reservation/apply.css'/>">
<link rel="stylesheet"
	href="<c:url value='/static/css/layout/user/header/style.css'/>" />

<c:if test="${not empty successMessage}">
	<div class="success-message"
		style="background: #d4edda; color: #155724; padding: 15px; margin: 20px; border-radius: 5px; border: 1px solid #c3e6cb;">
		${successMessage}</div>
</c:if>

<jsp:include page="/WEB-INF/views/layout/user/header.jsp" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>자원 예약 신청</title>
    
    <!-- Flatpickr CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- Flatpickr JS -->
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/ko.js"></script>
</head>
<body>

<div class="reservation-apply">
    <div class="container">
        
        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
            <div class="error-message">
                <span class="error-icon">⚠️</span>
                <div class="error-content">
                    <strong>예약 신청 실패</strong>
                    <p><c:out value="${error}"/></p>
                </div>
            </div>
        </c:if>

        <!-- 페이지 헤더 -->
        <div class="page-head">
            <h1>자원 예약 신청</h1>
        </div>

        <form method="post" action="<c:url value='/reservation/apply'/>">
            <input type="hidden" id="assetId" name="assetId" value="${assetId}">

            <!-- 사용조건 및 준수사항 -->
            <div class="card">
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
            </div>

            <!-- 신청인 정보 카드 -->
            <div class="card">
                <h2 class="card-title">👤 신청인 정보</h2>
                <div class="card-body">
                    <p class="note"><b class="req">*</b> 주소 입력칸에는 <b>농기계를 이용할 주소</b>를 작성해주세요. (사용자 주소와 다름)</p>
                    <div class="form-grid">
                        <div class="row">
                            <div class="col-l">성명</div>
                            <div class="col-r">
                                <input type="text" value="<c:out value='${applicant.name}'/>" readonly>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-l">생년월일</div>
                            <div class="col-r">
                                <input type="text" value="<c:out value='${applicant.birth}'/>" readonly>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-l">연락처</div>
                            <div class="col-r phone">
                                <input type="text" value="<c:out value='${phone1}'/>" readonly>
                                <span>-</span>
                                <input type="text" value="<c:out value='${phone2}'/>" readonly>
                                <span>-</span>
                                <input type="text" value="<c:out value='${phone3}'/>" readonly>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-l">우편번호</div>
                            <div class="col-r zip">
                                <input type="text" id="zipcode" name="zipcode" value="<c:out value='${applicant.zipcode}'/>" readonly required>
                                <button type="button" id="btnPost">우편번호 찾기</button>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-l">주소</div>
                            <div class="col-r">
                                <input type="text" id="addr1" name="addr1" value="<c:out value='${applicant.addr1}'/>" readonly required>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-l">상세주소</div>
                            <div class="col-r">
                                <input type="text" id="addr2" name="addr2" value="<c:out value='${applicant.addr2}'/>" placeholder="상세주소를 입력하세요">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 자원 정보 카드 -->
            <div class="card">
                <h2 class="card-title">📦 예약 자원 정보</h2>
                <div class="card-body">
                    <div class="asset">
                        <div class="asset-thumb">
                            <c:choose>
                                <c:when test="${not empty assetImage}">
                                    <img src="<c:url value='${assetImage}'/>" alt="<c:out value='${assetName}'/>" onerror="this.parentElement.innerHTML='이미지 준비중';">
                                </c:when>
                                <c:otherwise>
                                    이미지 준비중
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="asset-info">
                            <p class="asset-note">※ 예약할 농기계 정보를 다시 확인하세요.</p>
                            <dl class="asset-spec stacked">
                                <div class="field">
                                    <dt>자원명</dt>
                                    <dd><c:out value="${assetName}"/></dd>
                                </div>
                                <div class="field">
                                    <dt>카테고리</dt>
                                    <dd><c:out value="${assetModel}"/></dd>
                                </div>
                                <div class="field">
                                    <dt>제조사</dt>
                                    <dd><c:out value="${assetMaker}"/></dd>
                                </div>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 예약 상세 카드 -->
            <div class="card">
                <h2 class="card-title">📅 예약 상세</h2>
                <div class="card-body">
                    <div class="picker-box">
                        <!-- 달력 영역 -->
                        <div class="calendar-area">
                            <input type="text" id="rangeCalendar" placeholder="예약 가능 날짜 확인 중...">
                        </div>

                        <!-- 선택된 날짜 표시 -->
                        <div class="date-summary">
                            <label for="reserveStartDate">시작일</label>
                            <input type="date" id="reserveStartDate" name="reserveStartDate" readonly>
                            
                            <label for="reserveEndDate">종료일</label>
                            <input type="date" id="reserveEndDate" name="reserveEndDate" readonly>
                        </div>

                        <!-- 시간대별 예약 현황이 여기에 동적으로 추가됩니다 -->

                        <!-- 시간 선택 영역 -->
                        <div class="time-area">
                            <div class="time-head">
                                <span>예약 시간 선택</span>
                                <div class="tabs">
                                    <div class="tab active" data-period="start">시작</div>
                                    <div class="tab" data-period="end">종료</div>
                                </div>
                            </div>

                            <div class="time-scroll">
                                <button type="button" class="arrow" id="timeLeft">◀</button>
                                <div id="timeTrack">
                                    <!-- 시간 버튼들이 JavaScript로 생성됩니다 -->
                                </div>
                                <button type="button" class="arrow" id="timeRight">▶</button>
                            </div>

                            <!-- 선택된 시간 표시 -->
                            <div class="picked">
                                <label for="reserveStartTime">시작 시간</label>
                                <output id="reserveStartTimeDisplay">미선택</output>
                                <input type="hidden" id="reserveStartTime" name="reserveStartTime">
                                
                                <label for="reserveEndTime">종료 시간</label>
                                <output id="reserveEndTimeDisplay">미선택</output>
                                <input type="hidden" id="reserveEndTime" name="reserveEndTime">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 사용 목적 카드 -->
            <div class="card">
                <h2 class="card-title">📝 사용 목적</h2>
                <div class="card-body">
                    <textarea name="purpose" class="purpose" placeholder="자원 사용 목적을 상세히 작성해주세요 (최대 100자)" maxlength="100" required><c:out value="${purpose}"/></textarea>
                </div>
            </div>

        </form>
    </div>
</div>

<!-- 페이지 하단 버튼 영역 -->
<div class="page-actions">
    <div class="container">
        <button type="button" class="btn ghost" onclick="history.back()">이전으로</button>
        <button type="submit" class="btn primary" id="submitBtn">예약 신청</button>
    </div>
</div>

<!-- Daum 우편번호 API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
$(document).ready(function() {
    console.log('페이지 로드 완료 - 데이터 우선 로드 후 달력 생성');
    
    // 전역 변수
    let activeTarget = "start";
    window.startTime = null;
    window.endTime = null;
    window.cachedTimeSlots = null;
    window.fp = null;
    
    // 핵심: API 데이터를 먼저 로드한 후 달력 생성
    loadDataFirstThenCreateCalendar();
    
    // 시간 버튼 생성
    const times = [];
    for (let h = 9; h <= 18; h++) {
        if (h === 18) { 
            times.push("18:00"); 
            break; 
        }
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
    
    // 탭 클릭 이벤트
    $('.tab').on('click', function() {
        $('.tab').removeClass('active');
        $(this).addClass('active');
        activeTarget = $(this).data('period');
        
        if (window.cachedTimeSlots) {
            updateTimeButtonAvailability(window.cachedTimeSlots);
        }
    });

    // 시간 버튼 클릭 이벤트
    $('#timeTrack').on('click', '.time-btn', function() {
        const $button = $(this);
        
        if ($button.is(':disabled') || 
            $button.hasClass('disabled') || 
            $button.hasClass('unavailable')) {
            return;
        }
        
        const time = $button.data('time');
        
        if (activeTarget === "start") {
            window.startTime = time;
            $('#reserveStartTimeDisplay').text(time);
            $('#reserveStartTime').val(time);
        } else {
            window.endTime = time;
            $('#reserveEndTimeDisplay').text(time);
            $('#reserveEndTime').val(time);
        }
        
        updateButtonSelectionGlobal();
    });

    // 시간 스크롤 버튼
    $('#timeLeft').on('click', function() {
        $('#timeTrack').animate({scrollLeft: '-=200'}, 300);
    });
    
    $('#timeRight').on('click', function() {
        $('#timeTrack').animate({scrollLeft: '+=200'}, 300);
    });

    // 우편번호 찾기
    $('#btnPost').on('click', function() {
        new daum.Postcode({
            oncomplete: function(data) {
                $('#zipcode').val(data.zonecode);
                $('#addr1').val(data.address);
                $('#addr2').focus();
            }
        }).open();
    });

    // 뒤로가기 방지 및 중복 제출 방지 초기화
    initBackButtonPrevention();
    initDuplicateSubmissionPrevention();
});

// 핵심 함수: 빠른 로딩을 위한 최적화된 달력 생성
function loadDataFirstThenCreateCalendar() {
    const assetId = $('#assetId').val();
    if (!assetId) {
        console.error('assetId가 없어 기본 달력만 생성');
        createBasicCalendar();
        return;
    }
    
    console.log('빠른 달력 로딩 시작...');
    
    // 즉시 기본 달력 생성 (사용자가 바로 볼 수 있도록)
    createBasicCalendar();
    
    // 백그라운드에서 실제 데이터 로드 (3초 타임아웃)
    loadRealDataInBackground();
}

// 백그라운드에서 실제 데이터 로드
function loadRealDataInBackground() {
    const assetId = $('#assetId').val();
    const today = new Date();
    const endDate = new Date();
    endDate.setDate(today.getDate() + 30);
    
    const fromDate = today.toISOString().split('T')[0];
    const toDate = endDate.toISOString().split('T')[0];
    
    console.log('백그라운드 데이터 로딩 시작...');
    
    $.ajax({
        url: '/api/reservation/timeslots',
        method: 'GET',
        data: { assetId: assetId, from: fromDate, to: toDate },
        timeout: 3000,
        cache: false,
        success: function(timeSlots) {
            console.log('백그라운드 데이터 로드 성공! 달력 업데이트');
            
            if (timeSlots && timeSlots.length > 0) {
                updateCalendarQuietly(timeSlots);
            } else {
                console.warn('빈 데이터 - 테스트 API 시도');
                loadTestDataQuietly();
            }
        },
        error: function(xhr, status, error) {
            if (status === 'timeout') {
                console.warn('API 타임아웃 (3초) - 기본 달력 유지');
            } else {
                console.error('백그라운드 API 실패:', error);
                console.log('테스트 API로 폴백...');
                loadTestDataQuietly();
            }
        }
    });
}

// 조용히 달력 업데이트 (사용자 선택 유지)
function updateCalendarQuietly(timeSlots) {
    const currentSelectedDates = window.fp ? window.fp.selectedDates : [];
    
    const dateAvailability = {};
    timeSlots.forEach(slot => {
        const dateStr = formatDateKey(slot.startTime);
        if (!dateAvailability[dateStr]) dateAvailability[dateStr] = 0;
        if (slot.availableCount > 0) dateAvailability[dateStr]++;
    });
    
    const unavailableDates = [];
    Object.keys(dateAvailability).forEach(dateStr => {
        if (dateAvailability[dateStr] < 2) {
            unavailableDates.push(dateStr);
        }
    });
    
    if (unavailableDates.length > 0) {
        console.log('조용히 달력 업데이트 - 비활성화 날짜:', unavailableDates.length, '개');
        
        if (window.fp) {
            window.fp.destroy();
        }
        
        window.fp = flatpickr("#rangeCalendar", {
            mode: "range",
            locale: "ko",
            dateFormat: "Y-m-d",
            minDate: "today",
            maxDate: new Date().fp_incr(30),
            defaultDate: currentSelectedDates,
            disable: [
                function(date) {
                    if (date.getDay() === 0 || date.getDay() === 6) return true;
                    const dateStr = formatDateToString(date);
                    return unavailableDates.includes(dateStr);
                }
            ],
            onChange: function(selectedDates, dateStr, instance) {
                if (selectedDates.length === 2) {
                    const startDate = instance.formatDate(selectedDates[0], "Y-m-d");
                    const endDate = instance.formatDate(selectedDates[1], "Y-m-d");
                    $('#reserveStartDate').val(startDate);
                    $('#reserveEndDate').val(endDate);
                    updateTimeAvailability();
                }
            }
        });
    }
    
    window.cachedTimeSlots = timeSlots;
}

// 테스트 데이터 조용히 로드
function loadTestDataQuietly() {
    const assetId = $('#assetId').val();
    const today = new Date();
    const endDate = new Date();
    endDate.setDate(today.getDate() + 30);
    
    const fromDate = today.toISOString().split('T')[0];
    const toDate = endDate.toISOString().split('T')[0];
    
    $.ajax({
        url: '/api/reservation/timeslots-test',
        method: 'GET',
        data: { assetId: assetId, from: fromDate, to: toDate },
        timeout: 2000,
        success: function(timeSlots) {
            console.log('테스트 데이터 조용히 로드 성공');
            updateCalendarQuietly(timeSlots);
        },
        error: function(xhr, status, error) {
            console.error('테스트 API도 실패 - 기본 달력 유지:', error);
        }
    });
}

// 기본 달력 생성 (즉시 표시용)
function createBasicCalendar() {
    console.log('기본 달력 즉시 생성');
    
    if (window.fp) {
        window.fp.destroy();
    }
    
    window.fp = flatpickr("#rangeCalendar", {
        mode: "range",
        locale: "ko",
        dateFormat: "Y-m-d",
        minDate: "today",
        maxDate: new Date().fp_incr(30),
        disable: [
            function(date) {
                return (date.getDay() === 0 || date.getDay() === 6);
            }
        ],
        onChange: function(selectedDates, dateStr, instance) {
            if (selectedDates.length === 2) {
                const startDate = instance.formatDate(selectedDates[0], "Y-m-d");
                const endDate = instance.formatDate(selectedDates[1], "Y-m-d");
                $('#reserveStartDate').val(startDate);
                $('#reserveEndDate').val(endDate);
                updateTimeAvailability();
            }
        },
        onReady: function() {
            console.log('기본 달력 표시 완료 (백그라운드에서 업데이트 중...)');
            $('#rangeCalendar').attr('placeholder', '예약 기간을 선택하세요');
        }
    });
}

// 시간대별 예약 현황 업데이트 (날짜 초기화 추가)
function updateTimeAvailability() {
    const assetId = $('#assetId').val();
    const startDate = $('#reserveStartDate').val();
    const endDate = $('#reserveEndDate').val();
    
    if (!assetId || !startDate || !endDate) {
        return;
    }
    
    console.log('시간대별 현황 업데이트:', { startDate, endDate });
    
    // 날짜가 변경될 때 네비게이션 초기화
    resetDateNavigation();
    
    $.ajax({
        url: '/api/reservation/timeslots',
        method: 'GET',
        data: {
            assetId: assetId,
            from: startDate,
            to: endDate
        },
        timeout: 5000,
        success: function(timeSlots) {
            window.cachedTimeSlots = timeSlots;
            
            if (timeSlots.length === 0) {
                callTestAPI();
            } else {
                renderTimeAvailability(timeSlots);
                updateTimeButtonAvailability(timeSlots);
            }
        },
        error: function(xhr, status, error) {
            if (status === 'timeout') {
                console.warn('시간대 API 타임아웃 - 테스트 API 시도');
            } else {
                console.error('시간대 API 실패:', error);
            }
            callTestAPI();
        }
    });
}

// 테스트 API 호출 (최적화)
function callTestAPI() {
    const assetId = $('#assetId').val();
    const startDate = $('#reserveStartDate').val();
    const endDate = $('#reserveEndDate').val();
    
    $.ajax({
        url: '/api/reservation/timeslots-test',
        method: 'GET',
        data: {
            assetId: assetId,
            from: startDate,
            to: endDate
        },
        timeout: 3000,
        success: function(timeSlots) {
            window.cachedTimeSlots = timeSlots;
            renderTimeAvailability(timeSlots);
            updateTimeButtonAvailability(timeSlots);
        },
        error: function(xhr, status, error) {
            console.error('테스트 API도 실패:', error);
            $('.time-availability-container').remove();
        }
    });
}

// 시간대별 예약 현황 렌더링 (수정된 버전 - 페이지 스크롤 방지)
function renderTimeAvailability(timeSlots) {
    $('.time-availability-container').remove();
    
    if (!timeSlots || timeSlots.length === 0) {
        return;
    }
    
    // 날짜별로 그룹화
    const dateGroups = {};
    timeSlots.forEach(function(slot) {
        const date = formatDateKey(slot.startTime);
        if (!dateGroups[date]) {
            dateGroups[date] = [];
        }
        dateGroups[date].push(slot);
    });
    
    const dateKeys = Object.keys(dateGroups);
    
    let html = '<div class="time-availability-container">';
    
    // 헤더 (전체 선택된 기간 표시)
    if (dateKeys.length > 0) {
        const firstDate = formatDateForDisplay(dateKeys[0]);
        const lastDate = formatDateForDisplay(dateKeys[dateKeys.length - 1]);
        
        html += '<div class="availability-header">';
        html += '<button type="button" class="arrow-btn" id="prevDateBtn">‹</button>';
        if (dateKeys.length === 1) {
            html += '<h4>' + firstDate + ' 시간대별 예약 현황</h4>';
        } else {
            html += '<h4>' + firstDate + ' ~ ' + lastDate + ' 시간대별 예약 현황</h4>';
        }
        html += '<button type="button" class="arrow-btn" id="nextDateBtn">›</button>';
        html += '</div>';
    }
    
    // 현재 표시할 날짜 인덱스 초기화
    if (window.currentDateIndex === undefined) {
        window.currentDateIndex = 0;
    }
    
    // 현재 날짜의 시간대만 표시
    const currentDateKey = dateKeys[window.currentDateIndex] || dateKeys[0];
    const slots = dateGroups[currentDateKey];
    
    if (slots) {
        html += '<div class="current-date-slots" data-date="' + currentDateKey + '">';
        html += '<div class="date-title">' + formatDateForDisplay(currentDateKey) + '</div>';
        html += '<div class="time-grid">';
        
        slots.forEach(function(slot) {
            const startTime = formatTime(slot.startTime);
            const status = slot.availableCount + '/' + slot.totalCount;
            let statusClass = 'available';
            if (slot.availableCount === 0) {
                statusClass = 'unavailable';
            }
            
            html += '<div class="time-slot ' + statusClass + '">';
            html += startTime + '<br>';
            html += '<small>' + status + '</small>';
            html += '</div>';
        });
        
        html += '</div>';
        html += '</div>';
    }
    
    // 범례
    html += '<div class="time-legend">';
    html += '<span><div class="legend-color" style="background-color: #4CAF50;"></div> 예약가능</span>';
    html += '<span><div class="legend-color" style="background-color: #F44336;"></div> 예약불가</span>';
    html += '</div>';
    
    // 날짜 네비게이션 표시 (2개 이상일 때)
    if (dateKeys.length > 1) {
        html += '<div class="date-navigation">';
        html += '<div class="date-dots">';
        dateKeys.forEach(function(dateKey, index) {
            const isActive = index === (window.currentDateIndex || 0);
            html += '<span class="date-dot ' + (isActive ? 'active' : '') + '" data-date-index="' + index + '">';
            html += formatDateForDisplay(dateKey).replace(' ', '<br>');
            html += '</span>';
        });
        html += '</div>';
        html += '</div>';
    }
    
    html += '</div>';
    
    $('.date-summary').after(html);
    
    // 이벤트 리스너 추가 (onclick 대신 - 페이지 스크롤 방지)
    $('#prevDateBtn').off('click').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        scrollTimeAvailability(-1);
    });
    
    $('#nextDateBtn').off('click').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        scrollTimeAvailability(1);
    });
    
    $('.date-dot').off('click').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        const index = parseInt($(this).data('date-index'));
        goToDate(index);
    });
    
    // 화살표 버튼 상태 업데이트
    updateArrowButtons(dateKeys.length);
    
    // 전역 변수에 날짜 정보 저장
    window.availableDateGroups = dateGroups;
    window.availableDateKeys = dateKeys;
}

// 날짜 스크롤 함수 (페이지 스크롤 방지)
function scrollTimeAvailability(direction) {
    if (!window.availableDateKeys || window.availableDateKeys.length <= 1) {
        return;
    }
    
    const maxIndex = window.availableDateKeys.length - 1;
    window.currentDateIndex = window.currentDateIndex || 0;
    
    // 방향에 따라 인덱스 변경
    if (direction > 0 && window.currentDateIndex < maxIndex) {
        window.currentDateIndex++;
    } else if (direction < 0 && window.currentDateIndex > 0) {
        window.currentDateIndex--;
    }
    
    // 현재 날짜의 데이터 다시 렌더링
    updateCurrentDateSlots();
}

// 특정 날짜로 이동 (페이지 스크롤 방지)
function goToDate(index) {
    if (!window.availableDateKeys || index < 0 || index >= window.availableDateKeys.length) {
        return;
    }
    
    window.currentDateIndex = index;
    updateCurrentDateSlots();
}

// 현재 날짜 슬롯만 업데이트
function updateCurrentDateSlots() {
    if (!window.availableDateGroups || !window.availableDateKeys) {
        return;
    }
    
    const currentDateKey = window.availableDateKeys[window.currentDateIndex];
    const slots = window.availableDateGroups[currentDateKey];
    
    if (!slots) return;
    
    // 현재 날짜 슬롯 영역만 업데이트
    let html = '<div class="date-title">' + formatDateForDisplay(currentDateKey) + '</div>';
    html += '<div class="time-grid">';
    
    slots.forEach(function(slot) {
        const startTime = formatTime(slot.startTime);
        const status = slot.availableCount + '/' + slot.totalCount;
        let statusClass = 'available';
        if (slot.availableCount === 0) {
            statusClass = 'unavailable';
        } 
        
        html += '<div class="time-slot ' + statusClass + '">';
        html += startTime + '<br>';
        html += '<small>' + status + '</small>';
        html += '</div>';
    });
    
    html += '</div>';
    
    $('.current-date-slots').html(html);
    
    // 네비게이션 점 업데이트
    $('.date-dot').removeClass('active');
    $('.date-dot').eq(window.currentDateIndex).addClass('active');
    
    // 화살표 버튼 상태 업데이트
    updateArrowButtons(window.availableDateKeys.length);
}

// 화살표 버튼 상태 업데이트
function updateArrowButtons(totalDates) {
    const currentIndex = window.currentDateIndex || 0;
    
    // 이전 버튼
    if (currentIndex <= 0) {
        $('#prevDateBtn').attr('disabled', true).addClass('disabled');
    } else {
        $('#prevDateBtn').attr('disabled', false).removeClass('disabled');
    }
    
    // 다음 버튼
    if (currentIndex >= totalDates - 1) {
        $('#nextDateBtn').attr('disabled', true).addClass('disabled');
    } else {
        $('#nextDateBtn').attr('disabled', false).removeClass('disabled');
    }
}

// 날짜가 변경될 때 인덱스 초기화
function resetDateNavigation() {
    window.currentDateIndex = 0;
    window.availableDateGroups = null;
    window.availableDateKeys = null;
}

// 시간 버튼 비활성화 처리
function updateTimeButtonAvailability(timeSlots) {
    const startDate = $('#reserveStartDate').val();
    const endDate = $('#reserveEndDate').val();
    
    if (!startDate || !endDate) {
        return;
    }
    
    // 날짜별 시간대 가용성 맵 생성
    const startDateAvailability = {};
    const endDateAvailability = {};
    
    if (timeSlots && timeSlots.length > 0) {
        timeSlots.forEach(slot => {
            const slotDate = formatDateKey(slot.startTime);
            const timeStr = formatTime(slot.startTime);
            const availability = {
                available: slot.availableCount > 0,
                availableCount: slot.availableCount,
                totalCount: slot.totalCount
            };
            
            if (slotDate === startDate) {
                startDateAvailability[timeStr] = availability;
            }
            
            if (slotDate === endDate) {
                endDateAvailability[timeStr] = availability;
            }
        });
    }
    
    // 현재 활성 탭에 따라 해당 날짜의 버튼들 업데이트
    const currentActiveTarget = $('.tab.active').data('period') || 'start';
    const currentAvailability = currentActiveTarget === 'start' ? startDateAvailability : endDateAvailability;
    
    // 모든 시간 버튼 업데이트
    $('#timeTrack .time-btn').each(function() {
        const $button = $(this);
        const timeStr = $button.data('time');
        const availability = currentAvailability[timeStr];
        
        // 기존 스타일 초기화
        $button.removeClass('disabled unavailable')
               .removeAttr('disabled')
               .css({
                   'cursor': '',
                   'opacity': '',
                   'background': '',
                   'color': '',
                   'pointer-events': ''
               });
        
        if (availability && !availability.available) {
            // 예약 불가능한 시간대
            $button.addClass('disabled unavailable')
                   .attr('disabled', 'true')
                   .css({
                       'cursor': 'not-allowed !important',
                       'opacity': '0.5 !important',
                       'background': '#f5f5f5 !important',
                       'color': '#999 !important',
                       'pointer-events': 'none !important'
                   });
        } else {
            // 예약 가능한 시간대
            $button.css('cursor', 'pointer');
        }
    });
    
    // 선택된 시간이 비활성화된 경우 선택 해제
    if (currentActiveTarget === 'start' && window.startTime && 
        startDateAvailability[window.startTime] && !startDateAvailability[window.startTime].available) {
        window.startTime = null;
        $('#reserveStartTimeDisplay').text('미선택');
        $('#reserveStartTime').val('');
    }
    
    if (currentActiveTarget === 'end' && window.endTime && 
        endDateAvailability[window.endTime] && !endDateAvailability[window.endTime].available) {
        window.endTime = null;
        $('#reserveEndTimeDisplay').text('미선택');
        $('#reserveEndTime').val('');
    }
    
    updateButtonSelectionGlobal();
}

// 전역 버튼 선택 상태 업데이트
function updateButtonSelectionGlobal() {
    $('.time-btn').removeClass('selected-start selected-end');
    if (window.startTime) {
        $('#timeTrack').find('[data-time="' + window.startTime + '"]').addClass('selected-start');
    }
    if (window.endTime) {
        $('#timeTrack').find('[data-time="' + window.endTime + '"]').addClass('selected-end');
    }
}

// 유틸리티 함수들
function formatTime(dateString) {
    const date = new Date(dateString);
    return String(date.getHours()).padStart(2, '0') + ':' + 
           String(date.getMinutes()).padStart(2, '0');
}

function formatDateForDisplay(dateString) {
    const date = new Date(dateString);
    return (date.getMonth() + 1) + '월 ' + date.getDate() + '일';
}

function formatDateKey(dateString) {
    const date = new Date(dateString);
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0');
}

function formatDateToString(date) {
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0');
}

//========================================
//뒤로가기 방지 및 중복 제출 방지
//========================================

//뒤로가기 방지 초기화 (페이지 로드 즉시 차단)
function initBackButtonPrevention() {
 console.log('뒤로가기 완전 차단 초기화');
 
 // 페이지 고유 토큰 생성
 window.pageToken = 'page_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
 
 // 페이지 로드 즉시 뒤로가기 감지 및 차단
 (function() {
     // Navigation API를 통한 뒤로가기 감지
     if (performance.navigation && performance.navigation.type === 2) {
         console.log('뒤로가기로 페이지 진입 감지 - 즉시 리다이렉트');
         window.location.replace('/resource/list');
         return;
     }
     
     // PerformanceNavigationTiming API를 통한 감지
     if (performance.getEntriesByType) {
         const navEntry = performance.getEntriesByType('navigation')[0];
         if (navEntry && navEntry.type === 'back_forward') {
             console.log('뒤로가기 네비게이션 감지 - 즉시 리다이렉트');
             window.location.replace('/resource/list');
             return;
         }
     }
     
     // 문서 상태 확인
     if (document.referrer && document.referrer.includes('/resource/list')) {
         // 자원 목록에서 왔는데 브라우저 히스토리가 짧다면 뒤로가기일 가능성
         if (window.history.length <= 2) {
             console.log('의심스러운 네비게이션 감지 - 리다이렉트');
             window.location.replace('/resource/list');
             return;
         }
     }
 })();
 
 // 브라우저 히스토리 조작으로 뒤로가기 완전 차단 (대량 히스토리 생성)
 if (window.history && window.history.pushState) {
     // 현재 페이지를 대량으로 히스토리에 추가
     for (let i = 0; i < 100; i++) {
         window.history.pushState({page: i}, null, window.location.href);
     }
     
     // popstate 이벤트 리스너 (뒤로가기 감지 시 즉시 자원 목록으로 이동)
     window.addEventListener('popstate', function(event) {
         console.log('뒤로가기 시도 감지됨 - 즉시 자원 목록으로 이동');
         
         // 페이지 숨기기
         document.body.style.display = 'none';
         
         // 즉시 자원 목록 페이지로 리다이렉트
         window.location.replace('/resource/list');
         
         // 추가 방어코드
         event.preventDefault();
         event.stopPropagation();
         return false;
     }, true); // useCapture = true
     
     // 페이지가 로드된 후에도 지속적으로 히스토리 상태 유지 (매우 자주)
     setInterval(function() {
         if (window.history.length <= 20) {
             for (let i = 0; i < 50; i++) {
                 window.history.pushState({page: Date.now() + i}, null, window.location.href);
             }
         }
     }, 100);
 }
 
 // 페이지 가시성 즉시 체크
 document.addEventListener('visibilitychange', function() {
     if (document.hidden) {
         // 페이지가 숨겨질 때 (뒤로가기 준비 중일 수 있음)
         setTimeout(function() {
             if (!document.hidden) {
                 // 다시 보이게 되면 뒤로가기일 가능성
                 console.log('페이지 가시성 변경 감지 - 리다이렉트');
                 window.location.replace('/resource/list');
             }
         }, 50);
     }
 });
 
 // 페이지 로드 시점에서 뒤로가기 감지 (매우 빠른 차단)
 window.addEventListener('pageshow', function(event) {
     if (event.persisted) {
         console.log('캐시된 페이지 복원 감지 - 즉시 리다이렉트');
         document.body.style.display = 'none';
         window.location.replace('/resource/list');
     }
 });
 
 // DOMContentLoaded 이전에도 체크
 if (document.readyState === 'loading') {
     document.addEventListener('DOMContentLoaded', function() {
         // DOM 로드 후 추가 체크
         const submittedPages = JSON.parse(sessionStorage.getItem('submittedPages') || '[]');
         if (submittedPages.includes(window.pageToken)) {
             console.log('이미 제출된 페이지 토큰 감지 - 리다이렉트');
             document.body.style.display = 'none';
             window.location.replace('/resource/list');
         }
     });
 }
 
 // 키보드 단축키 차단 (Alt+Left, Backspace 등)
 document.addEventListener('keydown', function(event) {
     // Backspace 키 차단 (입력 필드가 아닌 경우)
     if (event.keyCode === 8) {
         const target = event.target;
         const isInputField = target.tagName === 'INPUT' || 
                             target.tagName === 'TEXTAREA' || 
                             target.isContentEditable;
         
         if (!isInputField) {
             event.preventDefault();
             event.stopPropagation();
             return false;
         }
     }
     
     // Alt + Left Arrow (뒤로가기) 차단
     if (event.altKey && event.keyCode === 37) {
         event.preventDefault();
         event.stopPropagation();
         return false;
     }
     
     // Ctrl + [ (일부 브라우저의 뒤로가기) 차단
     if (event.ctrlKey && event.keyCode === 219) {
         event.preventDefault();
         event.stopPropagation();
         return false;
     }
 });
 
 // 마우스 버튼 차단 (마우스 뒤로가기 버튼) - 더 포괄적으로
 document.addEventListener('mousedown', function(event) {
     // 마우스 뒤로가기 버튼 (button 3, 4)
     if (event.button === 3 || event.button === 4) {
         event.preventDefault();
         event.stopPropagation();
         return false;
     }
 });
 
 // 페이지 언로드 시 확인 (다른 방법으로 페이지를 벗어날 때만)
 let formSubmitted = false;
 
 window.addEventListener('beforeunload', function(event) {
     // 폼이 제출된 경우에는 경고하지 않음
     if (formSubmitted) {
         return;
     }
     
     // 폼에 변경사항이 있는지 확인
     if (checkFormChanges()) {
         const message = '페이지를 벗어나시겠습니까? 작성 중인 내용이 사라질 수 있습니다.';
         event.preventDefault();
         event.returnValue = message;
         return message;
     }
 });
 
 // 폼 제출 시 플래그 설정
 $('form').on('submit', function() {
     formSubmitted = true;
 });
 
 // 페이지 가시성 변경 시 처리 (캐시된 페이지 복귀 방지)
 document.addEventListener('visibilitychange', function() {
     if (!document.hidden) {
         // 페이지가 다시 보이게 될 때
         const submittedPages = JSON.parse(sessionStorage.getItem('submittedPages') || '[]');
         if (submittedPages.includes(window.pageToken)) {
             console.log('이미 제출된 페이지로 복귀 - 자원 목록으로 리다이렉트');
             window.location.href = '<c:url value="/resource/list"/>';
         }
     }
 });
 
 console.log('뒤로가기 완전 차단 설정 완료');
}

//중복 제출 방지 초기화 (원래대로 유지)
function initDuplicateSubmissionPrevention() {
 console.log('중복 제출 방지 초기화');
 
 let isSubmitting = false;
 
 // 기존 제출 버튼 이벤트 핸들러 수정
 $('#submitBtn').off('click').on('click', function(e) {
     e.preventDefault();
     
     console.log('제출 버튼 클릭됨');
     
     // 이미 제출 중인 경우 방지
     if (isSubmitting) {
         console.log('이미 제출 중입니다');
         alert('이미 처리 중입니다. 잠시만 기다려주세요.');
         return false;
     }
     
     // 폼 유효성 검사
     if (!validateFormSubmission()) {
         return false;
     }
     
     // 제출 상태 변경
     isSubmitting = true;
     
     // 버튼 비활성화 및 텍스트 변경
     const $submitBtn = $(this);
     $submitBtn.prop('disabled', true)
               .text('처리중...')
               .css({
                   'opacity': '0.6',
                   'cursor': 'not-allowed'
               });
     
     // 모든 버튼 비활성화
     $('.btn').prop('disabled', true);
     
     // 페이지 토큰을 폼에 추가
     $('<input>').attr({
         type: 'hidden',
         name: 'pageToken',
         value: window.pageToken
     }).appendTo('form');
     
     // 제출된 페이지로 표시
     markPageAsSubmitted();
     
     console.log('폼 제출 진행 중... 토큰:', window.pageToken);
     
     // 실제 폼 제출
     setTimeout(function() {
         $('form').submit();
     }, 100);
 });
}

//폼 변경사항 체크 (원래대로 유지)
function checkFormChanges() {
 let hasChanges = false;
 
 if ($('#reserveStartDate').val() || $('#reserveEndDate').val()) {
     hasChanges = true;
 }
 
 if ($('#reserveStartTime').val() || $('#reserveEndTime').val()) {
     hasChanges = true;
 }
 
 if ($('textarea[name="purpose"]').val().trim()) {
     hasChanges = true;
 }
 
 if ($('#zipcode').val() || $('#addr1').val() || $('#addr2').val().trim()) {
     hasChanges = true;
 }
 
 return hasChanges;
}

//폼 유효성 검사 (원래대로 유지)
function validateFormSubmission() {
 if (!$('#reserveStartDate').val() || !$('#reserveEndDate').val()) {
     alert('예약 날짜를 선택해주세요.');
     return false;
 }
 
 if (!$('#reserveStartTime').val() || !$('#reserveEndTime').val()) {
     alert('예약 시간을 선택해주세요.');
     return false;
 }
 
 if (!$('textarea[name="purpose"]').val().trim()) {
     alert('사용 목적을 입력해주세요.');
     return false;
 }
 
 if (!$('#zipcode').val() || !$('#addr1').val()) {
     alert('주소를 입력해주세요.');
     return false;
 }
 
 // 날짜 유효성 검사
 const startDate = new Date($('#reserveStartDate').val());
 const endDate = new Date($('#reserveEndDate').val());
 const now = new Date();
 
 if (startDate < now.setHours(0,0,0,0)) {
     alert('과거 날짜로는 예약할 수 없습니다.');
     return false;
 }
 
 if (startDate >= endDate) {
     alert('종료일은 시작일보다 늦어야 합니다.');
     return false;
 }
 
 return true;
}

//제출 완료 표시 (성공 시)
function markPageAsSubmitted() {
 if (window.pageToken) {
     const submittedPages = JSON.parse(sessionStorage.getItem('submittedPages') || '[]');
     if (!submittedPages.includes(window.pageToken)) {
         submittedPages.push(window.pageToken);
         sessionStorage.setItem('submittedPages', JSON.stringify(submittedPages));
     }
 }
}

console.log('뒤로가기 방지 및 중복 제출 방지 설정 완료');

// 폼 유효성 검사 (제출용)
function validateFormSubmission() {
    if (!$('#reserveStartDate').val() || !$('#reserveEndDate').val()) {
        alert('예약 날짜를 선택해주세요.');
        return false;
    }
    
    if (!$('#reserveStartTime').val() || !$('#reserveEndTime').val()) {
        alert('예약 시간을 선택해주세요.');
        return false;
    }
    
    if (!$('textarea[name="purpose"]').val().trim()) {
        alert('사용 목적을 입력해주세요.');
        return false;
    }
    
    if (!$('#zipcode').val() || !$('#addr1').val()) {
        alert('주소를 입력해주세요.');
        return false;
    }
    
    // 날짜 유효성 검사
    const startDate = new Date($('#reserveStartDate').val());
    const endDate = new Date($('#reserveEndDate').val());
    const now = new Date();
    
    if (startDate < now.setHours(0,0,0,0)) {
        alert('과거 날짜로는 예약할 수 없습니다.');
        return false;
    }
    
    if (startDate >= endDate) {
        alert('종료일은 시작일보다 늦어야 합니다.');
        return false;
    }
    
    return true;
}

// 페이지 가시성 변경 시 처리 (캐시된 페이지 복귀 방지)
document.addEventListener('visibilitychange', function() {
    if (!document.hidden) {
        // 페이지가 다시 보이게 될 때
        const submittedPages = JSON.parse(sessionStorage.getItem('submittedPages') || '[]');
        if (submittedPages.includes(window.pageToken)) {
            console.log('이미 제출된 페이지로 복귀 - 자원 목록으로 리다이렉트');
            window.location.href = '<c:url value="/resource/list"/>';
        }
    }
});

// 제출 완료 표시 (성공 시)
function markPageAsSubmitted() {
    if (window.pageToken) {
        const submittedPages = JSON.parse(sessionStorage.getItem('submittedPages') || '[]');
        if (!submittedPages.includes(window.pageToken)) {
            submittedPages.push(window.pageToken);
            sessionStorage.setItem('submittedPages', JSON.stringify(submittedPages));
        }
    }
}

console.log('뒤로가기 방지 및 중복 제출 방지 설정 완료');
</script>

</body>
</html>