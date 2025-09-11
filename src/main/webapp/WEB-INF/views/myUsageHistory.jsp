<%-- users/myUsageHistory.jsp - 내 사용 내역 페이지 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 사용 내역 - 빌려가유</title>
    
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
                    <div class="menu-item">
                        <a href="<c:url value='/my/reservations'/>" class="menu-link">
                            <span class="menu-text">예약 내역</span>
                        </a>
                    </div>
                    <div class="menu-item active">
                        <a href="<c:url value='/my/usage-history'/>" class="menu-link">
                            <span class="menu-text">사용 내역</span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content">
                <!-- 페이지 제목 -->
                <h1 class="page-title">사용 내역</h1>

                <!-- 브레드크럼 -->
                <div class="breadcrumb">
                    <span class="breadcrumb-item">내 정보</span>
                    <span class="breadcrumb-arrow">></span>
                    <span class="breadcrumb-item current">사용 내역</span>
                </div>

                <!-- 사용 통계 요약 -->
                <div class="summary-container">
                    <div class="statistics-cards">
                        <div class="stat-card">
                            <div class="stat-icon">📊</div>
                            <div class="stat-content">
                                <h3>총 사용 횟수</h3>
                                <span class="stat-number">${usageStatistics.TOTAL_USAGE}</span>
                                <span class="stat-unit">회</span>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">✅</div>
                            <div class="stat-content">
                                <h3>완료된 사용</h3>
                                <span class="stat-number">${usageStatistics.COMPLETED_USAGE}</span>
                                <span class="stat-unit">회</span>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">⏰</div>
                            <div class="stat-content">
                                <h3>총 사용 시간</h3>
                                <span class="stat-number">${usageStatistics.TOTAL_HOURS}</span>
                                <span class="stat-unit">시간</span>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">🚜</div>
                            <div class="stat-content">
                                <h3>농기계 종류</h3>
                                <span class="stat-number">${usageStatistics.UNIQUE_ASSETS}</span>
                                <span class="stat-unit">종</span>
                            </div>
                        </div>
                        <div class="stat-card completion-rate">
                            <div class="stat-icon">📈</div>
                            <div class="stat-content">
                                <h3>완료율</h3>
                                <span class="stat-number">${usageStatistics.COMPLETION_RATE}</span>
                                <span class="stat-unit">%</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 필터링 영역 -->
                <div class="filter-container">
                    <form id="filterForm" method="get" action="<c:url value='/my/usage-history'/>">
                        <div class="filter-row">
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
                            
                            <div class="filter-group">
                                <label for="usageStatus" class="filter-label">사용 상태</label>
                                <select name="usageStatus" id="usageStatus" class="filter-select">
                                    <option value="">전체</option>
                                    <option value="STARTED" ${currentUsageStatus == 'STARTED' ? 'selected' : ''}>사용 시작</option>
                                    <option value="COMPLETED" ${currentUsageStatus == 'COMPLETED' ? 'selected' : ''}>사용 완료</option>
                                    <option value="CANCELLED" ${currentUsageStatus == 'CANCELLED' ? 'selected' : ''}>사용 취소</option>
                                </select>
                            </div>
                            
                            <div class="filter-actions">
                                <button type="submit" class="btn primary">검색</button>
                                <button type="button" class="btn secondary" onclick="resetFilter()">초기화</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- 사용 내역 목록 -->
                <div class="usage-history-list">
                    <c:choose>
                        <c:when test="${empty usageHistory}">
                            <div class="empty-state">
                                <div class="empty-icon">📈</div>
                                <div class="empty-title">사용 내역이 없습니다</div>
                                <div class="empty-desc">농기계를 사용하시면 여기에 내역이 표시됩니다.</div>
                                <a href="<c:url value='/my/reservations'/>" class="btn primary">내 예약 확인</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="usage-table-container">
                                <table class="usage-table">
                                    <thead>
                                        <tr>
                                            <th>농기계</th>
                                            <th>사용 기간</th>
                                            <th>사용 시간</th>
                                            <th>사용 목적</th>
                                            <th>상태</th>
                                            <th>처리자</th>
                                            <th>등록일</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${usageHistory}" var="usage">
                                            <tr class="usage-row" data-status="${usage.usageStatus}">
                                                <td class="asset-info">
                                                    <div class="asset-basic">
                                                        <c:choose>
                                                            <c:when test="${not empty usage.assetImage}">
                                                                <img src="<c:url value='${usage.assetImage}'/>" alt="${usage.assetName}" class="asset-thumb">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="asset-thumb no-image">🚜</div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div class="asset-text">
                                                            <div class="asset-name">${usage.assetName}</div>
                                                            <div class="asset-meta">${usage.assetCategory} | ${usage.assetCompany}</div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="usage-period">
                                                    <div class="period-start">
                                                        <fmt:formatDate value="${usage.startTime}" pattern="MM/dd HH:mm"/>
                                                    </div>
                                                    <div class="period-separator">~</div>
                                                    <div class="period-end">
                                                        <fmt:formatDate value="${usage.endTime}" pattern="MM/dd HH:mm"/>
                                                    </div>
                                                </td>
                                                <td class="usage-duration">
                                                    <span class="duration-formatted">${usage.formattedDuration}</span>
                                                    <span class="duration-actual">(실제: ${usage.actualUsagePeriod})</span>
                                                </td>
                                                <td class="usage-purpose">
                                                    <div class="purpose-text">${usage.purpose}</div>
                                                </td>
                                                <td class="usage-status">
                                                    <span class="status-badge ${usage.usageStatusClass}">
                                                        ${usage.usageStatusText}
                                                    </span>
                                                </td>
                                                <td class="processed-by">
                                                    <c:choose>
                                                        <c:when test="${not empty usage.processedByName}">
                                                            ${usage.processedByName}
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="created-date">
                                                    <fmt:formatDate value="${usage.createdAt}" pattern="yyyy-MM-dd"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
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
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            document.getElementById('category').value = '';
            document.getElementById('usageStatus').value = '';
            document.getElementById('filterForm').submit();
        }
        
        // 테이블 반응형 처리
        function handleTableResponsive() {
            const table = document.querySelector('.usage-table');
            const container = document.querySelector('.usage-table-container');
            
            if (window.innerWidth < 768) {
                table.classList.add('mobile-view');
                container.style.overflowX = 'auto';
            } else {
                table.classList.remove('mobile-view');
                container.style.overflowX = 'visible';
            }
        }
        
        window.addEventListener('resize', handleTableResponsive);
        handleTableResponsive();
    </script>
</body>
</html>edDuration}</span>
                                                <span class="duration-actual">(실제: ${usage.actualUsagePeriod})</span>
                                            </td>
                                            <td class="usage-purpose">
                                                <div class="purpose-text">${usage.purpose}</div>
                                            </td>
                                            <td class="usage-status">
                                                <span class="status-badge ${usage.usageStatusClass}">
                                                    ${usage.usageStatusText}
                                                </span>
                                            </td>
                                            <td class="processed-by">
                                                <c:choose>
                                                    <c:when test="${not empty usage.processedByName}">
                                                        ${usage.processedByName}
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="created-date">
                                                <fmt:formatDate value="${usage.createdAt}" pattern="yyyy-MM-dd"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>

    <script>
        // 필터 초기화
        function resetFilter() {
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            document.getElementById('category').value = '';
            document.getElementById('usageStatus').value = '';
            document.getElementById('filterForm').submit();
        }
        
        // 테이블 반응형 처리
        function handleTableResponsive() {
            const table = document.querySelector('.usage-table');
            const container = document.querySelector('.usage-table-container');
            
            if (window.innerWidth < 768) {
                table.classList.add('mobile-view');
                container.style.overflowX = 'auto';
            } else {
                table.classList.remove('mobile-view');
                container.style.overflowX = 'visible';
            }
        }
        
        window.addEventListener('resize', handleTableResponsive);
        handleTableResponsive();
    </script>
</body>
</html>