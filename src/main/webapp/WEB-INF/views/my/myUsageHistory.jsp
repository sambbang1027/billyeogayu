<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 사용 내역</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/login/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/my/myUsageHistory.css'/>">
    
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
                    <h1 class="page-title">내 사용 내역</h1>
                    <a href="<c:url value='/my/reservations'/>" class="nav-link">예약 내역 보기</a>
                </div>

                <!-- 사용 통계 -->
				<c:if test="${not empty usageStatistics}">
				    <div class="stats-card">
				        <div class="stats-grid">
				            <div class="stats-item">
				                <h4><c:out value="${usageStatistics.TOTALUSAGECOUNT != null ? usageStatistics.TOTALUSAGECOUNT : 0}"/></h4>
				                <small>총 사용 횟수</small>
				            </div>
				            <div class="stats-item">
				                <h4><c:out value="${usageStatistics.COMPLETEDCOUNT != null ? usageStatistics.COMPLETEDCOUNT : 0}"/></h4>
				                <small>완료된 사용</small>
				            </div>
				            <div class="stats-item">
				                <h4><c:out value="${usageStatistics.ACTIVECOUNT != null ? usageStatistics.ACTIVECOUNT : 0}"/></h4>
				                <small>사용 중</small>
				            </div>
				            <div class="stats-item">
				                <h4><c:out value="${usageStatistics.totalUsageFormatted != null ? usageStatistics.totalUsageFormatted : '0분'}"/></h4>
				                <small>총 사용 시간</small>
				            </div>
				        </div>
				    </div>
				</c:if>

                <!-- 필터링 -->
                <div class="filter-card">
                    <form method="get" action="<c:url value='/my/usage-history'/>" class="filter-form">
                        <div class="form-group">
                            <label class="form-label">사용 상태</label>
                            <select name="usageStatus" class="form-select">
                                <option value="">전체</option>
                                <option value="COMPLETED" ${currentUsageStatus == 'COMPLETED' ? 'selected' : ''}>사용 완료</option>
                                <option value="ACTIVE" ${currentUsageStatus == 'ACTIVE' ? 'selected' : ''}>사용 중</option>
                                <option value="OVERDUE" ${currentUsageStatus == 'OVERDUE' ? 'selected' : ''}>연체</option>
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
                        <button type="submit" class="btn-search">조회</button>
                    </form>
                </div>

                <!-- 사용 내역 목록 -->
                <div class="usage-list">
                    <c:choose>
                        <c:when test="${not empty usageHistory}">
                            <c:forEach var="usage" items="${usageHistory}">
                                <div class="usage-card" 
                                     data-usage-id="${usage.reservationId}"
                                     onclick="openUsageModal('${usage.reservationId}')">
                                    <div class="card-header">
                                        <div class="asset-info">
                                            <img src="<c:url value='${empty usage.assetImage ? "/static/images/assets/default.png" : usage.assetImage}'/>" 
                                                 alt="<c:out value='${usage.assetName}'/>" 
                                                 class="asset-image">
                                            <div class="asset-details">
                                                <h3><c:out value="${usage.assetName}"/></h3>
                                                <div class="asset-meta"><c:out value="${usage.assetCategory}"/> | <c:out value="${usage.assetCompany}"/></div>
                                            </div>
                                        </div>
										<c:choose>
										    <c:when test="${usage.completed}">
										        <span class="usage-status usage-completed">사용 완료</span>
										    </c:when>
										    <c:when test="${usage.overdue}">
										        <span class="usage-status usage-overdue">연체</span>
										    </c:when>
										    <c:when test="${usage.currentlyInUse}">
										        <span class="usage-status usage-active">사용 중</span>
										    </c:when>
										    <c:otherwise>
										        <span class="usage-status usage-pending">대기중</span>
										    </c:otherwise>
										</c:choose>
                                    </div>
                                    
                                    <div class="card-body">
                                        <div class="info-item">
                                            <span class="info-label">사용 기간</span>
                                            <span class="info-value">
                                                <fmt:formatDate value="${usage.startTime}" pattern="yyyy-MM-dd HH:mm"/> ~ 
                                                <fmt:formatDate value="${usage.endTime}" pattern="yyyy-MM-dd HH:mm"/>
                                            </span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 목적</span>
                                            <span class="info-value"><c:out value="${usage.purpose}"/></span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 시간</span>
                                            <span class="info-value">
                                                <c:choose>
                                                    <c:when test="${not empty usage.formattedUsageDuration}">
                                                        <span class="duration-highlight"><c:out value="${usage.formattedUsageDuration}"/></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="duration-unavailable">미기록</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <h3>사용 내역이 없습니다</h3>
                                <p>아직 사용 완료된 내역이 없습니다.</p>
                                <a href="<c:url value='/resource/list'/>" class="btn-primary">농기계 예약하러 가기</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- 사용 내역 상세 모달 -->
    <div id="usageModal" class="modal" style="display: none;">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">사용 내역 상세 정보</h2>
                <button class="modal-close" onclick="closeUsageModal()">&times;</button>
            </div>
            <div class="modal-body" id="modalBody">
                <!-- 내용이 동적으로 로드됩니다 -->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-secondary" onclick="closeUsageModal()">닫기</button>
            </div>
        </div>
    </div>

    <!-- 공통 모달 include -->
    <jsp:include page="/WEB-INF/views/common/commonModal.jsp"/>

    <!-- JSP에서 JavaScript 변수로 데이터 전달 -->
    <script>
        // 사용 내역 데이터를 JavaScript 변수로 전달
        window.usageHistoryData = {
            <c:forEach var="usage" items="${usageHistory}" varStatus="status">
                "${usage.reservationId}": {
                    "reservationId": "${usage.reservationId}",
                    "assetName": "<c:out value='${usage.assetName}'/>",
                    "assetCategory": "<c:out value='${usage.assetCategory}'/>",
                    "assetCompany": "<c:out value='${usage.assetCompany}'/>",
                    "assetImage": "<c:out value='${usage.assetImage}'/>",
                    "startTime": "<fmt:formatDate value='${usage.startTime}' pattern='yyyy-MM-dd HH:mm'/>",
                    "endTime": "<fmt:formatDate value='${usage.endTime}' pattern='yyyy-MM-dd HH:mm'/>",
                    "purpose": "<c:out value='${usage.purpose}'/>",
                    "address": "<c:out value='${usage.address}'/>",
                    "status": "<c:out value='${usage.statusText}'/>",
                    "usageDuration": "<c:out value='${usage.formattedUsageDuration}'/>",
                    "actualUsageTime": "<c:out value='${usage.actualUsageTime}'/>",
                    "adminName": "<c:out value='${usage.adminName}'/>",
                    "completedAt": "<fmt:formatDate value='${usage.completedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                    "returnedAt": "<fmt:formatDate value='${usage.returnedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                    "createdAt": "<fmt:formatDate value='${usage.createdAt}' pattern='yyyy-MM-dd'/>"
                }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };
    </script>

    <script>
        /**
         * 내 사용 내역 페이지 JavaScript
         */

        let currentUsageId = null;

        /**
         * 사용 내역 상세 모달 열기 (개선된 버전)
         */
        function openUsageModal(reservationId) {
            console.log('openUsageModal 호출됨 - reservationId:', reservationId);
            currentUsageId = reservationId;
            
            // 전역 변수에서 데이터 찾기
            const usageData = window.usageHistoryData[reservationId];
            
            if (usageData) {
                console.log('찾은 데이터:', usageData);
                displayUsageModal(usageData);
                $('#usageModal').show();
            } else {
                console.error('해당 ID의 데이터를 찾을 수 없음:', reservationId);
                // 대안: AJAX로 서버에서 데이터 가져오기
                loadUsageDataFromServer(reservationId);
            }
        }

        /**
         * 서버에서 사용 내역 데이터 가져오기 (대안 방법)
         */
        function loadUsageDataFromServer(reservationId) {
            console.log('서버에서 데이터 로드 시도:', reservationId);
            
            $.ajax({
                url: '<c:url value="/my/reservations/"/>' + reservationId,
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    console.log('서버에서 받은 데이터:', data);
                    displayUsageModal(data);
                    $('#usageModal').show();
                },
                error: function(xhr, status, error) {
                    console.error('AJAX 오류:', error);
                    if (xhr.status === 404) {
                        showAlert('사용 내역을 찾을 수 없습니다.');
                    } else if (xhr.status === 401) {
                        showAlert('로그인이 필요합니다.', function() {
                            window.location.href = '<c:url value="/login"/>';
                        });
                    } else {
                        showAlert('상세 정보를 불러오는 중 오류가 발생했습니다.');
                    }
                }
            });
        }

        /**
         * 모달에 사용 내역 정보 표시 (개선된 버전)
         */
        function displayUsageModal(data) {
            const modalBody = $('#modalBody');
            
            // 데이터 안전성 검사
            const safeData = {
                assetName: data.assetName || '정보 없음',
                assetCategory: data.assetCategory || '정보 없음',
                assetCompany: data.assetCompany || '정보 없음',
                assetImage: data.assetImage || '',
                startTime: data.startTime || '정보 없음',
                endTime: data.endTime || '정보 없음',
                purpose: data.purpose || '정보 없음',
                address: data.address || '정보 없음',
                status: data.status || data.statusText || '정보 없음',
                usageDuration: data.usageDuration || data.formattedUsageDuration || '미기록',
                actualUsageTime: data.actualUsageTime || '-',
                adminName: data.adminName || '',
                completedAt: data.completedAt || '',
                returnedAt: data.returnedAt || '',
                createdAt: data.createdAt || '정보 없음'
            };
            
            // 이미지 경로 처리 - 완전한 경로 그대로 사용
            let imagePath;
            if (safeData.assetImage && safeData.assetImage.trim() !== '') {
                imagePath = safeData.assetImage; // 완전한 경로 그대로 사용
            } else {
                imagePath = '/static/images/assets/default.png'; // 기본 이미지
            }
            
            // 디버깅용 로그
            console.log('원본 assetImage:', data.assetImage);
            console.log('처리된 imagePath:', imagePath);
            
            // 모달 내용 생성
            let modalContent = '<div class="modal-asset-info">' +
                '<img src="' + imagePath + '" alt="' + safeData.assetName + '" class="modal-asset-image">' +
                '<div class="modal-asset-details">' +
                    '<h3>' + safeData.assetName + '</h3>' +
                    '<div class="modal-asset-meta">' + safeData.assetCategory + ' | ' + safeData.assetCompany + '</div>' +
                    '<span class="usage-status modal-status">' + safeData.status + '</span>' +
                '</div>' +
                '</div>' +
                '<div class="modal-duration">' +
                    '<div>총 사용 시간: ' + safeData.usageDuration + '</div>' +
                '</div>' +
                '<div class="modal-info-grid">' +
                    '<div class="modal-info-item">' +
                        '<div class="modal-info-label">예약 기간</div>' +
                        '<div class="modal-info-value">' + safeData.startTime + '<br>~ ' + safeData.endTime + '</div>' +
                    '</div>' +
                    '<div class="modal-info-item">' +
                        '<div class="modal-info-label">실제 사용 시간</div>' +
                        '<div class="modal-info-value">' + safeData.actualUsageTime + '</div>' +
                    '</div>' +
                    '<div class="modal-info-item">' +
                        '<div class="modal-info-label">사용 목적</div>' +
                        '<div class="modal-info-value">' + safeData.purpose + '</div>' +
                    '</div>' +
                    '<div class="modal-info-item">' +
                        '<div class="modal-info-label">사용 장소</div>' +
                        '<div class="modal-info-value">' + safeData.address + '</div>' +
                    '</div>';
                    
            // 담당자 정보가 있을 때만 추가
            if (safeData.adminName) {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">담당자</div>' +
                    '<div class="modal-info-value">' + safeData.adminName + '</div>' +
                    '</div>';
            }
            
            // 사용 완료일이 있을 때만 추가
            if (safeData.completedAt) {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">사용 완료일</div>' +
                    '<div class="modal-info-value">' + safeData.completedAt + '</div>' +
                    '</div>';
            }
            
            // 반납일이 있을 때만 추가
            if (safeData.returnedAt) {
                modalContent += '<div class="modal-info-item">' +
                    '<div class="modal-info-label">반납일</div>' +
                    '<div class="modal-info-value">' + safeData.returnedAt + '</div>' +
                    '</div>';
            }
            
            // 예약 신청일 추가
            modalContent += '<div class="modal-info-item">' +
                '<div class="modal-info-label">예약 신청일</div>' +
                '<div class="modal-info-value">' + safeData.createdAt + '</div>' +
                '</div>' +
                '</div>';
            
            modalBody.html(modalContent);
        }

        /**
         * 사용 내역 상세 모달 닫기
         */
        function closeUsageModal() {
            $('#usageModal').hide();
            currentUsageId = null;
        }

        // 페이지 로드 시 디버깅 정보 출력
        $(document).ready(function() {
            console.log('페이지 로드됨');
            console.log('사용 내역 데이터:', window.usageHistoryData);
            console.log('총 사용 내역 개수:', Object.keys(window.usageHistoryData || {}).length);
        });

        // 공통 모달 테스트용 함수들 (개발/테스트용)
        function testAlert() {
            showAlert("사용 내역이 업데이트되었습니다.", function() {
                console.log("알림 확인됨");
            });
        }

        function testConfirm() {
            showConfirm("이 사용 내역을 삭제하시겠습니까?",
                function() { console.log("삭제 확인"); },
                function() { console.log("삭제 취소"); }
            );
        }
    </script>
</body>
</html>