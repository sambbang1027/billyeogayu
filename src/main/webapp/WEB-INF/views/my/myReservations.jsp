<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 예약 내역</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/login/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/my/myReservation.css'/>">
    
    <!-- 공통 모달 CSS/JS -->
    <link rel="stylesheet" href="<c:url value='/static/css/common/commonModal.css'/>">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
    <script src="<c:url value='/static/js/common/commonModal.js'/>"></script>
</head>
<body>
    <!-- 헤더 -->
    <div class="header">
        <div class="header-content">
            <a href="<c:url value='/resource/list'/>" class="logo-link">
                <img src="<c:url value='/assets/layout/user/logo.svg'/>" alt="로고" class="logo">
            </a>
            <div class="header-links">
                <a href="<c:url value='/resource/list'/>" class="header-link">농기계 목록</a>
                <a href="<c:url value='/my/reservations'/>" class="header-link">내 예약</a>
                <a href="<c:url value='/my/usage-history'/>" class="header-link">사용 내역</a>
                <form action="<c:url value='/logout'/>" method="post" style="display: inline;" id="logoutForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="header-link logout-btn">로그아웃</button>
                </form>
            </div>
        </div>
    </div>

    <div class="component">
        <div class="frame">
            <div class="content-area">
                <!-- 페이지 헤더 -->
                <div class="page-header">
                    <h1 class="page-title">내 예약 내역</h1>
                    <a href="<c:url value='/my/usage-history'/>" class="nav-link">사용 내역 보기</a>
                </div>

				<!-- 예약 현황 요약 -->
				<c:if test="${not empty reservationSummary}">
				    <div class="summary-card">
				        <div class="summary-grid">
				            <div class="summary-item">
				                <h4><c:out value="${reservationSummary.TOTALCOUNT != null ? reservationSummary.TOTALCOUNT : 0}"/></h4>
				                <small>전체 예약</small>
				            </div>
				            <div class="summary-item">
				                <h4><c:out value="${reservationSummary.PENDINGCOUNT != null ? reservationSummary.PENDINGCOUNT : 0}"/></h4>
				                <small>승인 대기</small>
				            </div>
				            <div class="summary-item">
				                <h4><c:out value="${reservationSummary.APPROVEDCOUNT != null ? reservationSummary.APPROVEDCOUNT : 0}"/></h4>
				                <small>승인 완료</small>
				            </div>
				            <div class="summary-item">
				                <h4><c:out value="${reservationSummary.COMPLETEDCOUNT != null ? reservationSummary.COMPLETEDCOUNT : 0}"/></h4>
				                <small>사용 완료</small>
				            </div>
				        </div>
				    </div>
				</c:if>

                <!-- 필터 영역 -->
                <div class="filter-card">
                    <form method="get" action="<c:url value='/my/reservations'/>" class="filter-form">
                        <div class="form-group">
                            <label for="status" class="form-label">상태</label>
                            <select name="status" id="status" class="form-select">
                                <option value="">전체</option>
                                <option value="pending" ${currentStatus == 'pending' ? 'selected' : ''}>승인 대기</option>
                                <option value="approved" ${currentStatus == 'approved' ? 'selected' : ''}>승인 완료</option>
                                <option value="rejected" ${currentStatus == 'rejected' ? 'selected' : ''}>반려</option>
                                <option value="completed" ${currentStatus == 'completed' ? 'selected' : ''}>사용 완료</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="startDate" class="form-label">시작일</label>
                            <input type="date" name="startDate" id="startDate" value="${currentStartDate}" class="form-input">
                        </div>
                        <div class="form-group">
                            <label for="endDate" class="form-label">종료일</label>
                            <input type="date" name="endDate" id="endDate" value="${currentEndDate}" class="form-input">
                        </div>
                        <div class="form-group">
                            <label for="category" class="form-label">카테고리</label>
                            <select name="category" id="category" class="form-select">
                                <option value="">전체</option>
                                <option value="트랙터" ${currentCategory == '트랙터' ? 'selected' : ''}>트랙터</option>
                                <option value="콤바인" ${currentCategory == '콤바인' ? 'selected' : ''}>콤바인</option>
                                <option value="이앙기" ${currentCategory == '이앙기' ? 'selected' : ''}>이앙기</option>
                            </select>
                        </div>
                        <button type="submit" class="btn-search">조회</button>
                    </form>
                </div>

                <!-- 예약 내역 목록 -->
                <div class="reservation-list">
                    <c:choose>
                        <c:when test="${not empty reservations}">
                            <c:forEach var="reservation" items="${reservations}">
                                <div class="reservation-card" data-reservation-id="${reservation.reservationId}">
                                    <div class="card-header">
                                        <div class="asset-info">
                                            <img src="<c:url value='${empty reservation.assetImage ? "/assets/default.png" : reservation.assetImage}'/>" 
                                                 alt="<c:out value='${reservation.assetName}'/>" 
                                                 class="asset-image">
                                            <div class="asset-details">
                                                <h3><c:out value="${reservation.assetName}"/></h3>
                                                <div class="asset-meta"><c:out value="${reservation.assetCategory}"/> | <c:out value="${reservation.assetCompany}"/></div>
                                            </div>
                                        </div>
                                        <div class="status-container">
                                            <span class="status-badge status-${reservation.statusClass}">
                                                <c:out value="${reservation.statusText}"/>
                                            </span>
                                            <!-- 승인 대기 상태일 때만 취소 버튼 표시 -->
                                            <c:if test="${reservation.statusClass eq 'pending'}">
                                                <button type="button" 
                                                        class="btn-cancel-small" 
                                                        data-reservation-id="${reservation.reservationId}"
                                                        onclick="cancelReservation('${reservation.reservationId}')">
                                                    취소
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                    
                                    <div class="card-body">
                                        <div class="info-item">
                                            <span class="info-label">예약 기간</span>
                                            <span class="info-value">
                                                <fmt:formatDate value="${reservation.startTime}" pattern="yyyy-MM-dd HH:mm"/> ~ 
                                                <fmt:formatDate value="${reservation.endTime}" pattern="yyyy-MM-dd HH:mm"/>
                                            </span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 시간</span>
                                            <span class="info-value">
                                                <c:choose>
                                                    <c:when test="${not empty reservation.usageDuration and reservation.usageDuration > 0}">
                                                        <c:out value="${reservation.formattedUsageDuration}"/>
                                                    </c:when>
                                                    <c:when test="${not empty reservation.completedAt}">
                                                        <c:out value="${reservation.actualUsageTime}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        미정
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 목적</span>
                                            <span class="info-value"><c:out value="${reservation.purpose}"/></span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">신청일</span>
                                            <span class="info-value">
                                                <fmt:formatDate value="${reservation.createdAt}" pattern="yyyy-MM-dd"/>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <img src="<c:url value='/assets/empty-state.svg'/>" alt="예약 없음" class="empty-icon">
                                <p class="empty-message">조건에 맞는 예약 내역이 없습니다.</p>
                                <a href="<c:url value='/resource/list'/>" class="btn-primary">농기계 둘러보기</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- 공통 모달 include -->
    <jsp:include page="/WEB-INF/views/common/commonModal.jsp"/>

    <script>
        /**
         * 내 예약 내역 페이지 JavaScript
         */

        let currentReservationId = null;

        /**
         * 예약 취소 - 공통 모달 사용
         */
        function cancelReservation(reservationId) {
            if (!reservationId) return;
            
            currentReservationId = reservationId;
            
            showConfirm('정말로 예약을 취소하시겠습니까?', 
                () => {
                    // 확인 클릭시 실행
                    $.ajax({
                        url: '<c:url value="/my/reservations/"/>' + reservationId + '/cancel',
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function(data) {
                            if (data.success) {
                                showAlert('예약이 취소되었습니다.', () => location.reload());
                            } else {
                                showAlert(data.message || '예약 취소 중 오류가 발생했습니다.');
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error('Error:', error);
                            if (xhr.status === 401) {
                                showAlert('로그인이 필요합니다.', () => {
                                    window.location.href = '<c:url value="/login"/>';
                                });
                            } else {
                                showAlert('예약 취소 중 오류가 발생했습니다.');
                            }
                        }
                    });
                },
                () => {
                    // 취소 클릭시 실행 (아무 것도 하지 않음)
                    console.log('예약 취소가 취소됨');
                    currentReservationId = null;
                }
            );
        }

        /**
         * 날짜/시간 포맷팅 함수들
         */
        function formatDateTime(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return date.toLocaleDateString('ko-KR') + ' ' + date.toLocaleTimeString('ko-KR', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: false
            });
        }

        function formatDate(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return date.toLocaleDateString('ko-KR');
        }
    </script>
</body>
</html>