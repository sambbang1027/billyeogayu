<%-- users/myReservations.jsp - 내 예약 내역 페이지 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 예약 내역 - 빌려가유</title>
    
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
            <div class="main-content">
                <!-- 페이지 제목 -->
                <h1 class="page-title">예약 내역</h1>

                <!-- 브레드크럼 -->
                <div class="breadcrumb">
                    <span class="breadcrumb-item">내 정보</span>
                    <span class="breadcrumb-arrow">></span>
                    <span class="breadcrumb-item current">예약 내역</span>
                </div>

                <!-- 예약 현황 요약 -->
                <div class="summary-container">
                    <div class="summary-cards">
                        <div class="summary-card pending">
                            <div class="summary-icon">⏳</div>
                            <div class="summary-content">
                                <div class="summary-label">승인 대기</div>
                                <div class="summary-count">${reservationSummary.PENDING_COUNT}</div>
                            </div>
                        </div>
                        <div class="summary-card approved">
                            <div class="summary-icon">✅</div>
                            <div class="summary-content">
                                <div class="summary-label">승인됨</div>
                                <div class="summary-count">${reservationSummary.APPROVED_COUNT}</div>
                            </div>
                        </div>
                        <div class="summary-card completed">
                            <div class="summary-icon">🏁</div>
                            <div class="summary-content">
                                <div class="summary-label">완료됨</div>
                                <div class="summary-count">${reservationSummary.COMPLETED_COUNT}</div>
                            </div>
                        </div>
                        <div class="summary-card total">
                            <div class="summary-icon">📊</div>
                            <div class="summary-content">
                                <div class="summary-label">전체 예약</div>
                                <div class="summary-count">${reservationSummary.TOTAL_RESERVATIONS}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 필터링 영역 -->
                <div class="filter-container">
                    <form id="filterForm" method="get" action="<c:url value='/my/reservations'/>">
                        <div class="filter-row">
                            <div class="filter-group">
                                <label for="status" class="filter-label">상태</label>
                                <select name="status" id="status" class="filter-select">
                                    <option value="">전체</option>
                                    <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>승인 대기</option>
                                    <option value="APPROVED" ${currentStatus == 'APPROVED' ? 'selected' : ''}>승인됨</option>
                                    <option value="REJECTED" ${currentStatus == 'REJECTED' ? 'selected' : ''}>거절됨</option>
                                    <option value="CANCELLED" ${currentStatus == 'CANCELLED' ? 'selected' : ''}>취소됨</option>
                                    <option value="COMPLETED" ${currentStatus == 'COMPLETED' ? 'selected' : ''}>완료됨</option>
                                </select>
                            </div>
                            
                            <div class="filter-group">
                                <label for="startDate" class="filter-label">시작일</label>
                                <input type="date" name="startDate" id="startDate" value="${currentStartDate}" class="filter-input">
                            </div>
                            
                            <div class="filter-group">
                                <label for="endDate" class="filter-label">종료일</label>
                                <input type="date" name="endDate" id="endDate" value="${currentEndDate}" class="filter-input">
                            </div>
                            
                            <div class="filter-group">
                                <label for="category" class="filter-label">카테고리</label>
                                <select name="category" id="category" class="filter-select">
                                    <option value="">전체</option>
                                    <option value="트랙터" ${currentCategory == '트랙터' ? 'selected' : ''}>트랙터</option>
                                    <option value="콤바인" ${currentCategory == '콤바인' ? 'selected' : ''}>콤바인</option>
                                    <option value="이앙기" ${currentCategory == '이앙기' ? 'selected' : ''}>이앙기</option>
                                    <option value="관리기" ${currentCategory == '관리기' ? 'selected' : ''}>관리기</option>
                                </select>
                            </div>
                            
                            <div class="filter-actions">
                                <button type="submit" class="btn primary">검색</button>
                                <button type="button" class="btn secondary" onclick="resetFilter()">초기화</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- 예약 목록 -->
                <div class="reservation-list-container">
                    <c:choose>
                        <c:when test="${empty reservations}">
                            <div class="empty-state">
                                <div class="empty-icon">📋</div>
                                <div class="empty-title">예약 내역이 없습니다</div>
                                <div class="empty-desc">농기계를 예약하고 편리하게 이용해보세요.</div>
                                <a href="<c:url value='/resource/list'/>" class="btn primary">농기계 둘러보기</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="reservation-list">
                                <c:forEach items="${reservations}" var="reservation">
                                    <div class="reservation-card" data-status="${reservation.status}">
                                        <div class="card-header">
                                            <div class="asset-info">
                                                <div class="asset-image">
                                                    <c:choose>
                                                        <c:when test="${not empty reservation.assetImage}">
                                                            <img src="<c:url value='${reservation.assetImage}'/>" alt="${reservation.assetName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="no-image">🚜</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="asset-details">
                                                    <h3 class="asset-name">${reservation.assetName}</h3>
                                                    <p class="asset-meta">${reservation.assetCategory} | ${reservation.assetCompany}</p>
                                                </div>
                                            </div>
                                            <div class="status-badge ${reservation.statusClass}">
                                                ${reservation.statusText}
                                            </div>
                                        </div>
                                        
                                        <div class="card-body">
                                            <div class="reservation-details">
                                                <div class="detail-row">
                                                    <span class="detail-label">예약 기간:</span>
                                                    <span class="detail-value">
                                                        <fmt:formatDate value="${reservation.startTime}" pattern="yyyy-MM-dd HH:mm"/> ~ 
                                                        <fmt:formatDate value="${reservation.endTime}" pattern="yyyy-MM-dd HH:mm"/>
                                                    </span>
                                                </div>
                                                <div class="detail-row">
                                                    <span class="detail-label">사용 목적:</span>
                                                    <span class="detail-value">${reservation.purpose}</span>
                                                </div>
                                                <div class="detail-row">
                                                    <span class="detail-label">사용 장소:</span>
                                                    <span class="detail-value">${reservation.fullAddress}</span>
                                                </div>
                                                <div class="detail-row">
                                                    <span class="detail-label">신청일:</span>
                                                    <span class="detail-value">
                                                        <fmt:formatDate value="${reservation.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="card-actions">
                                            <a href="<c:url value='/my/reservations/${reservation.reservationId}'/>" 
                                               class="btn secondary">상세 보기</a>
                                            
                                            <c:if test="${reservation.cancellable}">
                                                <button type="button" class="btn danger" 
                                                        onclick="cancelReservation(${reservation.reservationId})">
                                                    취소 요청
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 필터 초기화
        function resetFilter() {
            document.getElementById('status').value = '';
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            document.getElementById('category').value = '';
            document.getElementById('filterForm').submit();
        }
        
        // 예약 취소
        function cancelReservation(reservationId) {
            if (!confirm('정말로 이 예약을 취소하시겠습니까?')) {
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
                    location.reload();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('예약 취소 중 오류가 발생했습니다.');
            });
        }
        
        // 상태별 필터링 즉시 적용
        document.getElementById('status').addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });
    </script>
</body>
</html>