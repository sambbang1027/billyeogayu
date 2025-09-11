<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 예약 내역</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/login/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/myReservations.css'/>">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
</head>
<body>
    <!-- 헤더 -->
    <div class="header">
        <div class="header-content">
            <img src="<c:url value='/static/images/logo.png'/>" alt="로고" class="logo">
            <div class="header-links">
                <a href="<c:url value='/assets'/>" class="header-link">농기계 목록</a>
                <a href="<c:url value='/my/reservations'/>" class="header-link">내 예약</a>
                <a href="<c:url value='/my/usage-history'/>" class="header-link">사용 내역</a>
                <a href="<c:url value='/logout'/>" class="header-link">로그아웃</a>
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
                                <h4><c:out value="${reservationSummary.totalCount != null ? reservationSummary.totalCount : 0}"/></h4>
                                <small>전체 예약</small>
                            </div>
                            <div class="summary-item">
                                <h4><c:out value="${reservationSummary.pendingCount != null ? reservationSummary.pendingCount : 0}"/></h4>
                                <small>승인 대기</small>
                            </div>
                            <div class="summary-item">
                                <h4><c:out value="${reservationSummary.approvedCount != null ? reservationSummary.approvedCount : 0}"/></h4>
                                <small>승인됨</small>
                            </div>
                            <div class="summary-item">
                                <h4><c:out value="${reservationSummary.completedCount != null ? reservationSummary.completedCount : 0}"/></h4>
                                <small>완료됨</small>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- 필터링 -->
                <div class="filter-card">
                    <form method="get" action="<c:url value='/my/reservations'/>" class="filter-form">
                        <div class="form-group">
                            <label class="form-label">상태</label>
                            <select name="status" class="form-select">
                                <option value="">전체</option>
                                <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>승인 대기</option>
                                <option value="APPROVED" ${currentStatus == 'APPROVED' ? 'selected' : ''}>승인됨</option>
                                <option value="REJECTED" ${currentStatus == 'REJECTED' ? 'selected' : ''}>거절됨</option>
                                <option value="COMPLETED" ${currentStatus == 'COMPLETED' ? 'selected' : ''}>완료됨</option>
                                <option value="ACTIVE" ${currentStatus == 'ACTIVE' ? 'selected' : ''}>사용 중</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">시작일</label>
                            <input type="date" name="startDate" class="form-input" value="<c:out value='${currentStartDate}'/>">
                        </div>
                        <div class="form-group">
                            <label class="form-label">종료일</label>
                            <input type="date" name="endDate" class="form-input" value="<c:out value='${currentEndDate}'/>">
                        </div>
                        <div class="form-group">
                            <label class="form-label">카테고리</label>
                            <select name="category" class="form-select">
                                <option value="">전체</option>
                                <option value="트랙터" ${currentCategory == '트랙터' ? 'selected' : ''}>트랙터</option>
                                <option value="콤바인" ${currentCategory == '콤바인' ? 'selected' : ''}>콤바인</option>
                                <option value="이앙기" ${currentCategory == '이앙기' ? 'selected' : ''}>이앙기</option>
                                <option value="기타" ${currentCategory == '기타' ? 'selected' : ''}>기타</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn-search">검색</button>
                        </div>
                    </form>
                </div>

                <!-- 예약 목록 -->
                <div class="reservation-grid">
                    <c:choose>
                        <c:when test="${empty reservations}">
                            <div class="empty-state">
                                <h3>예약 내역이 없습니다</h3>
                                <p>새로운 농기계를 예약해보세요.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="reservation" items="${reservations}">
                                <div class="reservation-card" 
                                     data-reservation-id="${reservation.reservationId}"
                                     data-reservation='{
                                         "reservationId": "${reservation.reservationId}",
                                         "assetName": "<c:out value='${reservation.assetName}'/>",
                                         "assetCategory": "<c:out value='${reservation.assetCategory}'/>",
                                         "assetCompany": "<c:out value='${reservation.assetCompany}'/>",
                                         "assetImage": "${reservation.assetImage}",
                                         "status": "<c:out value='${reservation.statusText}'/>",
                                         "startTime": "<fmt:formatDate value='${reservation.startTime}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "endTime": "<fmt:formatDate value='${reservation.endTime}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "purpose": "<c:out value='${reservation.purpose}'/>",
                                         "address": "<c:out value='${reservation.fullAddress}'/>",
                                         "createdAt": "<fmt:formatDate value='${reservation.createdAt}' pattern='yyyy-MM-dd'/>",
                                         "usageDuration": "<c:out value='${reservation.formattedUsageDuration}'/>",
                                         "actualUsageTime": "<c:out value='${reservation.actualUsageTime}'/>",
                                         "adminName": "<c:out value='${reservation.adminName}'/>",
                                         "completedAt": "<fmt:formatDate value='${reservation.completedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "returnedAt": "<fmt:formatDate value='${reservation.returnedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "rejectReason": "<c:out value='${reservation.rejectReason}'/>",
                                         "cancellable": ${reservation.cancellable}
                                     }'
                                     onclick="openReservationModal('${reservation.reservationId}')">>
                                    <div class="card-header">
                                        <div class="asset-info">
                                            <img src="<c:url value='/static/images/assets/${reservation.assetImage}'/>" 
                                                 alt="<c:out value='${reservation.assetName}'/>" 
                                                 class="asset-image"
                                                 onerror="this.src='<c:url value='/static/images/assets/default.png'/>'">
                                            <div class="asset-details">
                                                <h3><c:out value="${reservation.assetName}"/></h3>
                                                <div class="asset-meta"><c:out value="${reservation.assetCategory}"/> | <c:out value="${reservation.assetCompany}"/></div>
                                            </div>
                                        </div>
                                        <span class="status-badge status-${reservation.statusClass}">
                                            <c:out value="${reservation.statusText}"/>
                                        </span>
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
                                            <span class="info-label">사용 목적</span>
                                            <span class="info-value"><c:out value="${reservation.purpose}"/></span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 장소</span>
                                            <span class="info-value"><c:out value="${reservation.fullAddress}"/></span>
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
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- 예약 상세 모달 -->
    <div id="reservationModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">예약 상세 정보</h2>
                <button class="modal-close" onclick="closeReservationModal()">&times;</button>
            </div>
            <div class="modal-body" id="modalBody">
                <!-- 내용이 동적으로 로드됩니다 -->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-secondary" onclick="closeReservationModal()">닫기</button>
                <button type="button" id="cancelBtn" class="btn-cancel" onclick="cancelReservation()" style="display: none;">예약 취소</button>
            </div>
        </div>
    </div>

    <script>
        /**
         * 내 예약 내역 페이지 JavaScript
         */

        let currentReservationId = null;

        /**
         * 예약 상세 모달 열기
         */
        function openReservationModal(reservationId) {
            currentReservationId = reservationId;
            
            // AJAX로 상세 정보 가져오기
            $.ajax({
                url: `/my/reservations/${reservationId}`,
                method: 'GET',
                success: function(data) {
                    // HTML 응답을 파싱하여 모달에 표시
                    const $data = $(data);
                    const reservationData = extractReservationData($data);
                    
                    displayReservationModal(reservationData);
                    
                    // 취소 가능한 예약인지 확인하여 버튼 표시
                    if (reservationData.cancellable) {
                        $('#cancelBtn').show();
                    } else {
                        $('#cancelBtn').hide();
                    }
                    
                    $('#reservationModal').show();
                },
                error: function(xhr, status, error) {
                    console.error('Error:', error);
                    alert('상세 정보를 불러오는 중 오류가 발생했습니다.');
                }
            });
        }

        /**
         * 예약 데이터 추출
         */
        function extractReservationData($data) {
            // 서버에서 받은 HTML에서 필요한 데이터 추출
            return {
                reservationId: currentReservationId,
                assetName: $data.find('.asset-name').text() || '농기계명',
                assetCategory: $data.find('.asset-category').text() || '카테고리',
                assetCompany: $data.find('.asset-company').text() || '제조사',
                assetImage: $data.find('.asset-image').attr('src') || '/static/images/assets/default.png',
                status: $data.find('.status').text() || '상태',
                startTime: $data.find('.start-time').text() || '시작시간',
                endTime: $data.find('.end-time').text() || '종료시간',
                purpose: $data.find('.purpose').text() || '사용목적',
                address: $data.find('.address').text() || '주소',
                createdAt: $data.find('.created-at').text() || '신청일',
                cancellable: $data.find('.cancellable').data('value') === 'true'
            };
        }

        /**
         * 모달에 예약 정보 표시
         */
        function displayReservationModal(data) {
            const modalBody = $('#modalBody');
            
            const modalContent = `
                <div class="modal-asset-info">
                    <img src="${data.assetImage}" alt="${data.assetName}" class="modal-asset-image" 
                         onerror="this.src='/static/images/assets/default.png'">
                    <div class="modal-asset-details">
                        <h3>${data.assetName}</h3>
                        <div class="modal-asset-meta">${data.assetCategory} | ${data.assetCompany}</div>
                        <span class="status-badge modal-status">${data.status}</span>
                    </div>
                </div>
                
                <div class="modal-info-grid">
                    <div class="modal-info-item">
                        <div class="modal-info-label">예약 기간</div>
                        <div class="modal-info-value">${data.startTime}<br>~ ${data.endTime}</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">사용 목적</div>
                        <div class="modal-info-value">${data.purpose}</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">사용 장소</div>
                        <div class="modal-info-value">${data.address}</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">신청일</div>
                        <div class="modal-info-value">${data.createdAt}</div>
                    </div>
                </div>
            `;
            
            modalBody.html(modalContent);
        }

        /**
         * 예약 상세 모달 닫기
         */
        function closeReservationModal() {
            $('#reservationModal').hide();
            currentReservationId = null;
        }

        /**
         * 예약 취소
         */
        function cancelReservation() {
            if (!currentReservationId) return;
            
            if (!confirm('정말로 예약을 취소하시겠습니까?')) return;
            
            $.ajax({
                url: `/my/reservations/${currentReservationId}/cancel`,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                success: function(data) {
                    if (data.success) {
                        alert('예약이 취소되었습니다.');
                        location.reload();
                    } else {
                        alert(data.message || '예약 취소 중 오류가 발생했습니다.');
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Error:', error);
                    alert('예약 취소 중 오류가 발생했습니다.');
                }
            });
        }

        /**
         * 문서 로드 완료 후 이벤트 바인딩
         */
        $(document).ready(function() {
            // 모달 외부 클릭시 닫기
            $(window).click(function(event) {
                if (event.target.id === 'reservationModal') {
                    closeReservationModal();
                }
            });
            
            // ESC 키로 모달 닫기
            $(document).keydown(function(event) {
                if (event.keyCode === 27) { // ESC key
                    closeReservationModal();
                }
            });
            
            // 필터 폼 자동 제출 (선택사항)
            $('.form-select').change(function() {
                // 자동으로 폼 제출하고 싶다면 주석 해제
                // $(this).closest('form').submit();
            });
        });
    </script>
</body>
</html>