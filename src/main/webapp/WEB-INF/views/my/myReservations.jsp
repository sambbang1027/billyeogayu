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
				                <small>승인됨</small>
				            </div>
				            <div class="summary-item">
				                <h4><c:out value="${reservationSummary.COMPLETEDCOUNT != null ? reservationSummary.COMPLETEDCOUNT : 0}"/></h4>
				                <small>완료</small>
				            </div>
				        </div>
				    </div>
				</c:if>

                <!-- 필터링 -->
                <div class="filter-card">
                    <form method="get" action="<c:url value='/my/reservations'/>" class="filter-form">
                        <div class="form-group">
                            <label class="form-label">예약 상태</label>
                            <select name="status" class="form-select">
                                <option value="">전체</option>
                                <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>승인 대기</option>
                                <option value="APPROVED" ${currentStatus == 'APPROVED' ? 'selected' : ''}>승인됨</option>
                                <option value="COMPLETED" ${currentStatus == 'COMPLETED' ? 'selected' : ''}>완료</option>
                                <option value="REJECTED" ${currentStatus == 'REJECTED' ? 'selected' : ''}>반려</option>
                                <option value="ACTIVE" ${currentStatus == 'ACTIVE' ? 'selected' : ''}>사용 중</option>
                                <option value="CANCELLED" ${currentStatus == 'CANCELLED' ? 'selected' : ''}>취소</option>
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
                            </select>
                        </div>
                        <button type="submit" class="btn-primary">조회</button>
                    </form>
                </div>

                <!-- 예약 내역 목록 -->
                <div class="reservation-list">
                    <c:choose>
                        <c:when test="${not empty reservations}">
                            <c:forEach var="reservation" items="${reservations}">
                                <div class="reservation-card" 
                                     data-reservation-id="${reservation.reservationId}" 
                                     onclick="openReservationModal('${reservation.reservationId}')">
                                    <div class="card-header">
                                        <div class="asset-info">
                                            <img src="<c:url value='${empty reservation.assetImage ? "default.png" : reservation.assetImage}'/>" 
                                                 alt="<c:out value='${reservation.assetName}'/>" 
                                                 class="asset-image">
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
                                <h3>예약 내역이 없습니다</h3>
                                <p>아직 신청한 예약이 없습니다.</p>
                                <a href="<c:url value='/resource/list'/>" class="btn-primary">농기계 예약하러 가기</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- 예약 상세 모달 -->
    <div id="reservationModal" class="modal" style="display: none;">
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

    <!-- 공통 모달 include -->
    <jsp:include page="/WEB-INF/views/common/commonModal.jsp"/>

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
            
            // AJAX로 상세 정보 가져오기 (JSON 응답)
            $.ajax({
                url: '<c:url value="/my/reservations/"/>' + reservationId,
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    displayReservationModal(data);
                    
                    // 취소 가능한 예약인지 확인하여 버튼 표시
                    if (data.cancellable) {
                        $('#cancelBtn').show();
                    } else {
                        $('#cancelBtn').hide();
                    }
                    
                    $('#reservationModal').show();
                },
                error: function(xhr, status, error) {
                    console.error('Error:', error);
                    if (xhr.status === 404) {
                        showAlert('예약 정보를 찾을 수 없습니다.');
                    } else if (xhr.status === 401) {
                        showAlert('로그인이 필요합니다.', () => {
                            window.location.href = '<c:url value="/login"/>';
                        });
                    } else {
                        showAlert('상세 정보를 불러오는 중 오류가 발생했습니다.');
                    }
                }
            });
        }

        /**
         * 모달에 예약 정보 표시
         */
        function displayReservationModal(reservation) {
            const modalBody = $('#modalBody');
            
            // 상태 클래스 결정
            let statusClass = 'pending';
            if (reservation.status) {
                switch(reservation.status.toUpperCase()) {
                    case 'PENDING': statusClass = 'pending'; break;
                    case 'APPROVED': statusClass = 'approved'; break;
                    case 'REJECTED': statusClass = 'rejected'; break;
                    case 'COMPLETED': statusClass = 'completed'; break;
                    case 'ACTIVE': statusClass = 'active'; break;
                    default: statusClass = 'pending';
                }
            }

            // 이미지 경로 설정
            const imagePath = reservation.assetImage ? 
                '<c:url value="/static/images/assets/"/>' + reservation.assetImage : 
                '<c:url value="/static/images/assets/default.png"/>';
            
            // 모달 내용 구성
            let modalContent = '<div class="modal-asset-info">' +
                '<img src="' + imagePath + '" alt="' + reservation.assetName + '" class="modal-asset-image">' +
                '<div class="modal-asset-details">' +
                    '<h3>' + reservation.assetName + '</h3>' +
                    '<div class="modal-asset-meta">' + reservation.assetCategory + ' | ' + reservation.assetCompany + '</div>' +
                    '<span class="status-badge modal-status status-' + statusClass + '">' + reservation.statusText + '</span>' +
                '</div>' +
            '</div>';

            modalContent += '<div class="modal-info-grid">';

            // 예약 기간
            modalContent += '<div class="modal-info-item">' +
                '<div class="modal-info-label">예약 기간</div>' +
                '<div class="modal-info-value">' + formatDateTime(reservation.startTime) + '<br>~ ' + formatDateTime(reservation.endTime) + '</div>' +
            '</div>';

            // 사용 목적
            modalContent += '<div class="modal-info-item">' +
                '<div class="modal-info-label">사용 목적</div>' +
                '<div class="modal-info-value">' + reservation.purpose + '</div>' +
            '</div>';

            // 사용 장소
            if (reservation.address) {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">사용 장소</div>' +
                    '<div class="modal-info-value">' + reservation.address + '</div>' +
                '</div>';
            }

            // 신청일
            modalContent += '<div class="modal-info-item">' +
                '<div class="modal-info-label">신청일</div>' +
                '<div class="modal-info-value">' + formatDate(reservation.createdAt) + '</div>' +
            '</div>';

            // 반려 사유
            if (reservation.rejectReason && reservation.rejectReason !== '사용자 취소') {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">반려 사유</div>' +
                    '<div class="modal-info-value">' + reservation.rejectReason + '</div>' +
                '</div>';
            }

            // 완료 정보
            if (reservation.status === 'COMPLETED') {
                if (reservation.completedAt) {
                    modalContent += '<div class="modal-info-item">' +
                        '<div class="modal-info-label">완료 시간</div>' +
                        '<div class="modal-info-value">' + formatDateTime(reservation.completedAt) + '</div>' +
                    '</div>';
                }
                if (reservation.actualUsageTime) {
                    modalContent += '<div class="modal-info-item">' +
                        '<div class="modal-info-label">실제 사용 시간</div>' +
                        '<div class="modal-info-value">' + reservation.actualUsageTime + '</div>' +
                    '</div>';
                }
            }

            // 담당 관리자
            if (reservation.adminName) {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">담당 관리자</div>' +
                    '<div class="modal-info-value">' + reservation.adminName + '</div>' +
                '</div>';
            }

            modalContent += '</div>'; // modal-info-grid 닫는 태그
            
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
         * 예약 취소 - 공통 모달 사용
         */
        function cancelReservation() {
            if (!currentReservationId) return;
            
            showConfirm('정말로 예약을 취소하시겠습니까?', 
                () => {
                    // 확인 클릭시 실행
                    $.ajax({
                        url: '<c:url value="/my/reservations/"/>' + currentReservationId + '/cancel',
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