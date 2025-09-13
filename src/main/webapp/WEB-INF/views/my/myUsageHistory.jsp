<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 사용 내역</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/login/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/my/myUsageHistory.css'/>">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
</head>
<body>
    <!-- 헤더 -->
    <div class="header">
        <div class="header-content">
            <img src="<c:url value='/assets/layout/user/logo.svg'/>" alt="로고" class="logo">
            <div class="header-links">
                <a href="<c:url value='/resource/list'/>" class="header-link">농기계 목록</a>
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
                    <h1 class="page-title">내 사용 내역</h1>
                    <a href="<c:url value='/my/reservations'/>" class="nav-link">예약 내역 보기</a>
                </div>

                <!-- 사용 통계 -->
                <c:if test="${not empty usageStatistics}">
                    <div class="stats-card">
                        <div class="stats-grid">
                            <div class="stats-item">
                                <h4><c:out value="${usageStatistics.totalUsageCount != null ? usageStatistics.totalUsageCount : 0}"/></h4>
                                <small>총 사용 횟수</small>
                            </div>
                            <div class="stats-item">
                                <h4><c:out value="${usageStatistics.completedCount != null ? usageStatistics.completedCount : 0}"/></h4>
                                <small>완료된 사용</small>
                            </div>
                            <div class="stats-item">
                                <h4><c:out value="${usageStatistics.activeCount != null ? usageStatistics.activeCount : 0}"/></h4>
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
                        <div class="form-group">
                            <button type="submit" class="btn-search">검색</button>
                        </div>
                    </form>
                </div>

                <!-- 사용 내역 목록 -->
                <div class="usage-grid">
                    <c:choose>
                        <c:when test="${empty usageHistory}">
                            <div class="empty-state">
                                <h3>사용 내역이 없습니다</h3>
                                <p>농기계를 사용한 후 내역을 확인하실 수 있습니다.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="usage" items="${usageHistory}">
                                <div class="usage-card" 
                                     data-usage-id="${usage.reservationId}"
                                     data-usage='{
                                         "reservationId": "${usage.reservationId}",
                                         "assetName": "<c:out value='${usage.assetName}'/>",
                                         "assetCategory": "<c:out value='${usage.assetCategory}'/>",
                                         "assetCompany": "<c:out value='${usage.assetCompany}'/>",
                                         "assetImage": "${usage.assetImage}",
                                         "status": "<c:out value='${usage.statusText}'/>",
                                         "startTime": "<fmt:formatDate value='${usage.startTime}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "endTime": "<fmt:formatDate value='${usage.endTime}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "purpose": "<c:out value='${usage.purpose}'/>",
                                         "address": "<c:out value='${usage.fullAddress}'/>",
                                         "usageDuration": "<c:out value='${usage.formattedUsageDuration}'/>",
                                         "actualUsageTime": "<c:out value='${usage.actualUsageTime}'/>",
                                         "adminName": "<c:out value='${usage.adminName}'/>",
                                         "completedAt": "<fmt:formatDate value='${usage.completedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "returnedAt": "<fmt:formatDate value='${usage.returnedAt}' pattern='yyyy-MM-dd HH:mm'/>",
                                         "createdAt": "<fmt:formatDate value='${usage.createdAt}' pattern='yyyy-MM-dd'/>"
                                     }'
                                     onclick="openUsageModal('${usage.reservationId}')">
                                    <div class="card-header">
                                        <div class="asset-info">
                                            <img src="<c:url value='/static/images/assets/${empty usage.assetImage ? "default.png" : usage.assetImage}'/>" 
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
                                            <c:when test="${usage.currentlyInUse}">
                                                <span class="usage-status usage-active">사용 중</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="usage-status usage-overdue">연체</span>
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
                                                        <span class="duration-highlight">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="info-item">
                                            <span class="info-label">사용 장소</span>
                                            <span class="info-value"><c:out value="${usage.fullAddress}"/></span>
                                        </div>
                                        <c:if test="${not empty usage.adminName}">
                                            <div class="info-item">
                                                <span class="info-label">담당자</span>
                                                <span class="info-value"><c:out value="${usage.adminName}"/></span>
                                            </div>
                                        </c:if>
                                        <c:if test="${usage.completed and not empty usage.returnedAt}">
                                            <div class="info-item">
                                                <span class="info-label">반납일</span>
                                                <span class="info-value">
                                                    <fmt:formatDate value="${usage.returnedAt}" pattern="yyyy-MM-dd HH:mm"/>
                                                </span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- 사용 내역 상세 모달 -->
    <div id="usageModal" class="modal">
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

    <script>
        /**
         * 내 사용 내역 페이지 JavaScript
         */

        let currentUsageId = null;

        /**
         * 사용 내역 상세 모달 열기 (서버 데이터 직접 사용)
         */
        function openUsageModal(reservationId) {
            currentUsageId = reservationId;
            
            // 페이지에서 해당 사용 내역 데이터 찾기
            const usageCard = $(`.usage-card[data-usage-id="${reservationId}"]`);
            const usageData = usageCard.data('usage');
            
            if (usageData) {
                displayUsageModal(usageData);
                $('#usageModal').show();
            } else {
                alert('사용 내역 정보를 찾을 수 없습니다.');
            }
        }

        /**
         * 모달에 사용 내역 정보 표시
         */
        function displayUsageModal(data) {
            const modalBody = $('#modalBody');
            
            // 이미지 경로 안전하게 설정
            const imagePath = data.assetImage ? 
                '<c:url value="/static/images/assets/"/>' + data.assetImage : 
                '<c:url value="/static/images/assets/default.png"/>';
            
            const modalContent = `
                <div class="modal-asset-info">
                    <img src="` + imagePath + `" alt="` + data.assetName + `" class="modal-asset-image">
                    <div class="modal-asset-details">
                        <h3>` + data.assetName + `</h3>
                        <div class="modal-asset-meta">` + data.assetCategory + ` | ` + data.assetCompany + `</div>
                        <span class="usage-status modal-status">` + data.status + `</span>
                    </div>
                </div>
                
                <div class="modal-duration">
                    <div>총 사용 시간: ` + (data.usageDuration || '미기록') + `</div>
                </div>
                
                <div class="modal-info-grid">
                    <div class="modal-info-item">
                        <div class="modal-info-label">예약 기간</div>
                        <div class="modal-info-value">` + data.startTime + `<br>~ ` + data.endTime + `</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">실제 사용 시간</div>
                        <div class="modal-info-value">` + (data.actualUsageTime || '-') + `</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">사용 목적</div>
                        <div class="modal-info-value">` + data.purpose + `</div>
                    </div>
                    <div class="modal-info-item">
                        <div class="modal-info-label">사용 장소</div>
                        <div class="modal-info-value">` + data.address + `</div>
                    </div>
                    ` + (data.adminName ? `
                    <div class="modal-info-item">
                        <div class="modal-info-label">담당자</div>
                        <div class="modal-info-value">` + data.adminName + `</div>
                    </div>
                    ` : '') + `
                    ` + (data.completedAt ? `
                    <div class="modal-info-item">
                        <div class="modal-info-label">사용 완료일</div>
                        <div class="modal-info-value">` + data.completedAt + `</div>
                    </div>
                    ` : '') + `
                    ` + (data.returnedAt ? `
                    <div class="modal-info-item">
                        <div class="modal-info-label">반납일</div>
                        <div class="modal-info-value">` + data.returnedAt + `</div>
                    </div>
                    ` : '') + `
                    <div class="modal-info-item">
                        <div class="modal-info-label">예약 신청일</div>
                        <div class="modal-info-value">` + data.createdAt + `</div>
                    </div>
                </div>
            `;
            
            modalBody.html(modalContent);
        }

        /**
         * 사용 내역 상세 모달 닫기
         */
        function closeUsageModal() {
            $('#usageModal').hide();
            currentUsageId = null;
        }

        /**
         * 문서 로드 완료 후 이벤트 바인딩
         */
        $(document).ready(function() {
            // 모달 외부 클릭시 닫기
            $(window).click(function(event) {
                if (event.target.id === 'usageModal') {
                    closeUsageModal();
                }
            });
            
            // ESC 키로 모달 닫기
            $(document).keydown(function(event) {
                if (event.keyCode === 27) { // ESC key
                    closeUsageModal();
                }
            });
            
            // 필터 폼 자동 제출 (선택사항)
            $('.form-select').change(function() {
                // 자동으로 폼 제출하고 싶다면 주석 해제
                // $(this).closest('form').submit();
            });
            
            // 통계 카드 애니메이션
            $('.stats-item h4').each(function() {
                const $this = $(this);
                const countTo = parseInt($this.text());
                
                if (!isNaN(countTo)) {
                    $this.text('0');
                    
                    $({ countNum: 0 }).animate({
                        countNum: countTo
                    }, {
                        duration: 1000,
                        easing: 'swing',
                        step: function() {
                            $this.text(Math.floor(this.countNum));
                        },
                        complete: function() {
                            $this.text(countTo);
                        }
                    });
                }
            });
        });
    </script>
</body>
</html>