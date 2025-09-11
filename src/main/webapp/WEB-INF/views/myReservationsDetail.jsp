<%-- users/myReservationDetail.jsp - 예약 상세 정보 페이지 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>예약 상세 정보 - 빌려가유</title>
    
    <!-- 공통 헤더 CSS -->
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/header/style.css'/>"/>
    
    <!-- 내 정보 전용 CSS -->
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/my-info/style.css'/>"/>
</head>
<body>
    <!-- 공통 헤더 -->
    <jsp:include page="/WEB-INF/views/layout/user/header.jsp"/>

    <div class="component">
        <div class="frame">
            <!-- 사이드바 -->
            <div class="sidebar">
                <div class="sidebar-heading">
                    <div class="sidebar-title">내 정보</div>
                </div>
                <div class="sidebar-menu">
                    <div class="menu-item active">
                        <a href="<c:url value='/my/reservations'/>" class="menu-link">
                            <span class="menu-text">예약 내역</span>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/my/usage-history'/>" class="menu-link">
                            <span class="menu-text">사용 내역</span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content reservation-detail-page">
                <!-- 페이지 헤더 -->
                <div class="page-header">
                    <div class="header-left">
                        <a href="<c:url value='/my/reservations'/>" class="back-btn">← 목록으로</a>
                        <h1 class="page-title">예약 상세 정보</h1>
                    </div>
                    <c:if test="${not empty reservation}">
                        <div class="header-right">
                            <span class="status-badge large ${reservation.statusClass}">
                                ${reservation.statusText}
                            </span>
                        </div>
                    </c:if>
                </div>

                <!-- 브레드크럼 -->
                <div class="breadcrumb">
                    <span class="breadcrumb-item">내 정보</span>
                    <span class="breadcrumb-arrow">></span>
                    <span class="breadcrumb-item">예약 내역</span>
                    <span class="breadcrumb-arrow">></span>
                    <span class="breadcrumb-item current">상세 정보</span>
                </div>

                <!-- 에러 메시지 -->
                <c:if test="${not empty error}">
                    <div class="error-message">
                        <div class="error-icon">⚠️</div>
                        <div class="error-content">
                            <strong>오류</strong>
                            <p>${error}</p>
                        </div>
                    </div>
                </c:if>

                <!-- 예약 정보 카드 -->
                <c:if test="${not empty reservation}">
                    <div class="detail-content">
                        
                        <!-- 농기계 정보 -->
                        <div class="detail-card asset-info-card">
                            <h2 class="card-title">농기계 정보</h2>
                            <div class="card-body">
                                <div class="asset-display">
                                    <div class="asset-image-large">
                                        <c:choose>
                                            <c:when test="${not empty reservation.assetImage}">
                                                <img src="<c:url value='${reservation.assetImage}'/>" alt="${reservation.assetName}">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="no-image-large">🚜</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="asset-details-large">
                                        <h3 class="asset-name">${reservation.assetName}</h3>
                                        <div class="asset-specs">
                                            <div class="spec-item">
                                                <span class="spec-label">카테고리</span>
                                                <span class="spec-value">${reservation.assetCategory}</span>
                                            </div>
                                            <div class="spec-item">
                                                <span class="spec-label">제조사</span>
                                                <span class="spec-value">${reservation.assetCompany}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 예약 상세 정보 -->
                        <div class="detail-card reservation-info-card">
                            <h2 class="card-title">예약 정보</h2>
                            <div class="card-body">
                                <div class="info-grid">
                                    <div class="info-item">
                                        <span class="info-label">예약 번호</span>
                                        <span class="info-value">#${reservation.reservationId}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">예약 상태</span>
                                        <span class="info-value">
                                            <span class="status-badge ${reservation.statusClass}">
                                                ${reservation.statusText}
                                            </span>
                                        </span>
                                    </div>
                                    <div class="info-item full-width">
                                        <span class="info-label">사용 기간</span>
                                        <span class="info-value period-value">
                                            <div class="period-display">
                                                <div class="period-start">
                                                    <span class="period-label">시작</span>
                                                    <span class="period-datetime">
                                                        <fmt:formatDate value="${reservation.startTime}" pattern="yyyy년 MM월 dd일 HH:mm"/>
                                                    </span>
                                                </div>
                                                <div class="period-arrow">→</div>
                                                <div class="period-end">
                                                    <span class="period-label">종료</span>
                                                    <span class="period-datetime">
                                                        <fmt:formatDate value="${reservation.endTime}" pattern="yyyy년 MM월 dd일 HH:mm"/>
                                                    </span>
                                                </div>
                                            </div>
                                        </span>
                                    </div>
                                    <div class="info-item full-width">
                                        <span class="info-label">사용 목적</span>
                                        <span class="info-value purpose-value">${reservation.purpose}</span>
                                    </div>
                                    <div class="info-item full-width">
                                        <span class="info-label">사용 장소</span>
                                        <span class="info-value address-value">${reservation.fullAddress}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">신청일</span>
                                        <span class="info-value">
                                            <fmt:formatDate value="${reservation.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                        </span>
                                    </div>
                                    <c:if test="${not empty reservation.updatedAt}">
                                        <div class="info-item">
                                            <span class="info-label">최종 수정일</span>
                                            <span class="info-value">
                                                <fmt:formatDate value="${reservation.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                                            </span>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <!-- 상태별 안내 메시지 -->
                        <div class="detail-card status-guide-card">
                            <h2 class="card-title">안내 사항</h2>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${reservation.status == 'PENDING'}">
                                        <div class="guide-message pending">
                                            <div class="guide-icon">⏳</div>
                                            <div class="guide-content">
                                                <h4>승인 대기 중</h4>
                                                <p>관리자가 검토 중입니다. 승인까지 1-2일 소요될 수 있습니다.</p>
                                                <p>승인 대기 중에는 예약 취소가 가능합니다.</p>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${reservation.status == 'APPROVED'}">
                                        <div class="guide-message approved">
                                            <div class="guide-icon">✅</div>
                                            <div class="guide-content">
                                                <h4>예약 승인됨</h4>
                                                <p>예약이 승인되었습니다. 예약된 시간에 농기계를 이용하실 수 있습니다.</p>
                                                <p>사용 전 농기계 상태를 확인하고 안전하게 사용해주세요.</p>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${reservation.status == 'REJECTED'}">
                                        <div class="guide-message rejected">
                                            <div class="guide-icon">❌</div>
                                            <div class="guide-content">
                                                <h4>예약 거절됨</h4>
                                                <p>죄송합니다. 이번 예약은 승인되지 않았습니다.</p>
                                                <p>다른 날짜로 다시 예약을 시도해보세요.</p>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${reservation.status == 'CANCELLED'}">
                                        <div class="guide-message cancelled">
                                            <div class="guide-icon">🚫</div>
                                            <div class="guide-content">
                                                <h4>예약 취소됨</h4>
                                                <p>이 예약은 취소되었습니다.</p>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:when test="${reservation.status == 'COMPLETED'}">
                                        <div class="guide-message completed">
                                            <div class="guide-icon">🏁</div>
                                            <div class="guide-content">
                                                <h4>사용 완료</h4>
                                                <p>농기계 사용이 완료되었습니다. 이용해 주셔서 감사합니다!</p>
                                                <p>사용 내역은 '내 사용 내역' 페이지에서 확인하실 수 있습니다.</p>
                                            </div>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- 액션 버튼 -->
                    <div class="detail-actions">
                        <div class="action-buttons">
                            <a href="<c:url value='/my/reservations'/>" class="btn secondary large">목록으로 돌아가기</a>
                            
                            <c:if test="${reservation.cancellable}">
                                <button type="button" class="btn danger large" onclick="cancelReservation(${reservation.reservationId})">
                                    예약 취소
                                </button>
                            </c:if>
                            
                            <c:if test="${reservation.status == 'REJECTED' || reservation.status == 'CANCELLED'}">
                                <a href="<c:url value='/reservation/apply?assetId=${reservation.assetId}'/>" class="btn primary large">
                                    다시 예약하기
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <script>
        // 예약 취소 함수
        function cancelReservation(reservationId) {
            if (!confirm('정말로 이 예약을 취소하시겠습니까?\n\n취소 후에는 되돌릴 수 없습니다.')) {
                return;
            }
            
            fetch('/my/reservations/' + reservationId + '/cancel', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    location.reload(); // 페이지 새로고침으로 상태 업데이트
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('예약 취소 중 오류가 발생했습니다. 다시 시도해주세요.');
            });
        }
        
        // 뒤로가기 버튼 키보드 접근성
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                window.location.href = '<c:url value="/my/reservations"/>';
            }
        });
    </script>
</body>
</html><%-- users/myReservationDetail.jsp - 예약 상세 정보 페이지 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>예약 상세 정보 - 빌려가유</title>
    
    <!-- 공통 헤더 CSS -->
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/header/style.css'/>"/>
    
    <!-- 내 정보 전용 CSS -->
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/my/style.css'/>"/>
</head>
<body>
    <!-- 공통 헤더 -->
    <jsp:include page="/WEB-INF/views/layout/user/header.jsp"/>

    <main class="reservation-detail-page">
        <div class="container">
            <!-- 페이지 헤더 -->
            <header class="page-header">
                <div class="header-left">
                    <a href="<c:url value='/my/reservations'/>" class="back-btn">← 목록으로</a>
                    <h1>예약 상세 정보</h1>
                </div>
                <div class="header-right">
                    <span class="status-badge large ${reservation.statusClass}">
                        ${reservation.statusText}
                    </span>
                </div>
            </header>

            <!-- 에러 메시지 -->
            <c:if test="${not empty error}">
                <div class="error-message">
                    <div class="error-icon">⚠️</div>
                    <div class="error-content">
                        <strong>오류</strong>
                        <p>${error}</p>
                    </div>
                </div>
            </c:if>

            <!-- 예약 정보 카드 -->
            <c:if test="${not empty reservation}">
                <section class="detail-content">
                    
                    <!-- 농기계 정보 -->
                    <div class="detail-card asset-info-card">
                        <h2 class="card-title">농기계 정보</h2>
                        <div class="card-body">
                            <div class="asset-display">
                                <div class="asset-image-large">
                                    <c:choose>
                                        <c:when test="${not empty reservation.assetImage}">
                                            <img src="<c:url value='${reservation.assetImage}'/>" alt="${reservation.assetName}">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="no-image-large">🚜</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="asset-details-large">
                                    <h3 class="asset-name">${reservation.assetName}</h3>
                                    <div class="asset-specs">
                                        <div class="spec-item">
                                            <span class="spec-label">카테고리</span>
                                            <span class="spec-value">${reservation.assetCategory}</span>
                                        </div>
                                        <div class="spec-item">
                                            <span class="spec-label">제조사</span>
                                            <span class="spec-value">${reservation.assetCompany}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 예약 상세 정보 -->
                    <div class="detail-card reservation-info-card">
                        <h2 class="card-title">예약 정보</h2>
                        <div class="card-body">
                            <div class="info-grid">
                                <div class="info-item">
                                    <span class="info-label">예약 번호</span>
                                    <span class="info-value">#${reservation.reservationId}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">예약 상태</span>
                                    <span class="info-value">
                                        <span class="status-badge ${reservation.statusClass}">
                                            ${reservation.statusText}
                                        </span>
                                    </span>
                                </div>
                                <div class="info-item full-width">
                                    <span class="info-label">사용 기간</span>
                                    <span class="info-value period-value">
                                        <div class="period-display">
                                            <div class="period-start">
                                                <span class="period-label">시작</span>
                                                <span class="period-datetime">
                                                    <fmt:formatDate value="${reservation.startTime}" pattern="yyyy년 MM월 dd일 HH:mm"/>
                                                </span>
                                            </div>
                                            <div class="period-arrow">→</div>
                                            <div class="period-end">
                                                <span class="period-label">종료</span>
                                                <span class="period-datetime">
                                                    <fmt:formatDate value="${reservation.endTime}" pattern="yyyy년 MM월 dd일 HH:mm"/>
                                                </span>
                                            </div>
                                        </div>
                                    </span>
                                </div>
                                <div class="info-item full-width">
                                    <span class="info-label">사용 목적</span>
                                    <span class="info-value purpose-value">${reservation.purpose}</span>
                                </div>
                                <div class="info-item full-width">
                                    <span class="info-label">사용 장소</span>
                                    <span class="info-value address-value">${reservation.fullAddress}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">신청일</span>
                                    <span class="info-value">
                                        <fmt:formatDate value="${reservation.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                    </span>
                                </div>
                                <c:if test="${not empty reservation.updatedAt}">
                                    <div class="info-item">
                                        <span class="info-label">최종 수정일</span>
                                        <span class="info-value">
                                            <fmt:formatDate value="${reservation.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                                        </span>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- 상태별 안내 메시지 -->
                    <div class="detail-card status-guide-card">
                        <h2 class="card-title">안내 사항</h2>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${reservation.status == 'PENDING'}">
                                    <div class="guide-message pending">
                                        <div class="guide-icon">⏳</div>
                                        <div class="guide-content">
                                            <h4>승인 대기 중</h4>
                                            <p>관리자가 검토 중입니다. 승인까지 1-2일 소요될 수 있습니다.</p>
                                            <p>승인 대기 중에는 예약 취소가 가능합니다.</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${reservation.status == 'APPROVED'}">
                                    <div class="guide-message approved">
                                        <div class="guide-icon">✅</div>
                                        <div class="guide-content">
                                            <h4>예약 승인됨</h4>
                                            <p>예약이 승인되었습니다. 예약된 시간에 농기계를 이용하실 수 있습니다.</p>
                                            <p>사용 전 농기계 상태를 확인하고 안전하게 사용해주세요.</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${reservation.status == 'REJECTED'}">
                                    <div class="guide-message rejected">
                                        <div class="guide-icon">❌</div>
                                        <div class="guide-content">
                                            <h4>예약 거절됨</h4>
                                            <p>죄송합니다. 이번 예약은 승인되지 않았습니다.</p>
                                            <p>다른 날짜로 다시 예약을 시도해보세요.</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${reservation.status == 'CANCELLED'}">
                                    <div class="guide-message cancelled">
                                        <div class="guide-icon">🚫</div>
                                        <div class="guide-content">
                                            <h4>예약 취소됨</h4>
                                            <p>이 예약은 취소되었습니다.</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:when test="${reservation.status == 'COMPLETED'}">
                                    <div class="guide-message completed">
                                        <div class="guide-icon">🏁</div>
                                        <div class="guide-content">
                                            <h4>사용 완료</h4>
                                            <p>농기계 사용이 완료되었습니다. 이용해 주셔서 감사합니다!</p>
                                            <p>사용 내역은 '내 사용 내역' 페이지에서 확인하실 수 있습니다.</p>
                                        </div>
                                    </div>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </section>

                <!-- 액션 버튼 -->
                <section class="detail-actions">
                    <div class="action-buttons">
                        <a href="<c:url value='/my/reservations'/>" class="btn secondary large">목록으로 돌아가기</a>
                        
                        <c:if test="${reservation.cancellable}">
                            <button type="button" class="btn danger large" onclick="cancelReservation(${reservation.reservationId})">
                                예약 취소
                            </button>
                        </c:if>
                        
                        <c:if test="${reservation.status == 'REJECTED' || reservation.status == 'CANCELLED'}">
                            <a href="<c:url value='/reservation/apply?assetId=${reservation.assetId}'/>" class="btn primary large">
                                다시 예약하기
                            </a>
                        </c:if>
                    </div>
                </section>
            </c:if>
        </div>
    </main>

    <script>
        // 예약 취소 함수
        function cancelReservation(reservationId) {
            if (!confirm('정말로 이 예약을 취소하시겠습니까?\n\n취소 후에는 되돌릴 수 없습니다.')) {
                return;
            }
            
            fetch('/my/reservations/' + reservationId + '/cancel', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    location.reload(); // 페이지 새로고침으로 상태 업데이트
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('예약 취소 중 오류가 발생했습니다. 다시 시도해주세요.');
            });
        }
        
        // 뒤로가기 버튼 키보드 접근성
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                window.location.href = '<c:url value="/my/reservations"/>';
            }
        });
    </script>
</body>
</html>