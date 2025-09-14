<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="reservation-content">
    <h2 class="rv-page-title">예약관리</h2> 
    <!-- filter --> 
    <div class="rv-filter-bar">
        <div class="rv-filter-list">
            <!-- 종류 드롭다운 -->
            <div class="rv-custom-dropdown rv-filter-dropdown">
                <label class="rv-custom-dropdown-name">종류</label>
                <div class="rv-dropdown-box">
                    <button class="rv-dropdown-toggle">
                        <span class="rv-dropdown-label" data-value="">전체</span>
                        <img src="<c:url value='/assets/maintenance/arrow-down.svg'/>">
                    </button>
                    <ul class="rv-dropdown-menu">
                        <!-- >>모델들 list로 불러와서 for문으로 뿌려야함 -->
                        <c:forEach var="category" items="${categoryList }">
                            <li data-value="${category} ">${category}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            
            <!-- 상태 드롭다운 -->
            <div class="rv-custom-dropdown rv-filter-dropdown">
                <label class="rv-custom-dropdown-name">상태</label>
                <div class="rv-dropdown-box">
                    <button class="rv-dropdown-toggle">
                        <span class="rv-dropdown-label" data-value="">전체</span>
                        <img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
                    </button>
                    <ul class="rv-dropdown-menu">
                        <li class="rv-active">전체</li>
                        <li data-value="PENDING">승인대기</li>
                        <li data-value="APPROVED">사용중</li>
                        <li data-value="REJECTED">반려</li>
                        <li data-value="COMPLETED">반납완료</li>
                    </ul>
                </div>
            </div>
            
            <!-- 신청일자 -->
            <div class="rv-custom-dropdown rv-filter-dropdown">
                <label class="rv-custom-dropdown-name">시작일</label>
                <div class="rv-dropdown-box">
                    <input type="text" id="rv-inspectionDate" class="rv-date-input" placeholder="날짜 선택">
                    <span class="rv-calendar-icon">
                        <img src="<c:url value='/assets/maintenance/calendar.svg'/>" alt="달력">
                    </span>
                </div>
            </div>
        </div>
        
        <div class="rv-filter-container">
            <!-- 적용된 필터 표시 -->
            <div class="rv-active-filters">
                적용된 필터 <span class="rv-count"></span>
                <div>
                    <button class="rv-btn-reset">
                        <img alt="reset-filter" src="<c:url value='/assets/asset/reset.svg'/>" class="rv-reset-img">
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <div class = "count-down-box">
				<text class ="rv-total-count"></text>
		</div>

    <!-- reservation table -->
    <div class="rv-table-wrapper">
        <table class="rv-reservation-table">
            <thead>
                <tr>
                    <th>예약번호</th>
                    <th>종류</th>
                    <th>시작일</th>
                    <th>종료일</th>
                    <th>신청자</th>
                    <th>예약상태</th>
                    <th>신청일시</th>
                    <th>처리</th>
                </tr>
            </thead>
            <tbody>
                <!-- 하드코딩 예시 -->
                <tr>
                    <td>1001</td>
                    <td>트랙터 A</td>
                    <td>2025-09-15</td>
                    <td>2025-09-15</td>
                    <td>홍길동</td>
                    <td>
                    	<div class="rv-status pending">
		                    <span>승인대기</span>                    	
                    	</div>
                    </td>
                    <td>2025-09-10</td>
                    <td>
                        <div class="rv-btn-container">
                            <div class="rv-btn-box">
                                <button class="rv-btn-approve">승인</button>        
                            </div>
                            <div class="rv-btn-box">
                                <button class="rv-btn-reject">반려</button>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>1002</td>
                    <td>콤바인 B</td>
                    <td>2025-09-16</td>
                    <td>2025-09-16</td>
                    <td>김영희</td>
                     <td>
                    	<div class="rv-status using">
		                    <span>사용중</span>                    	
                    	</div>
                    </td>
                    <td>2025-09-10</td>
                    <td>
                        <div class="rv-btn-box">
                            <button class="rv-btn-complete">반납</button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>1003</td>
                    <td>경운기 C</td>
                    <td>2025-09-17</td>
                    <td>2025-09-17</td>
                    <td>이철수</td>
                     <td>
                    	<div class="rv-status rejected">
		                    <span>반려</span>                    	
                    	</div>
                    </td>
                    <td>2025-09-10</td>
                    <td>
                        <div class="rv-btn-box">
	                        <button class="rv-btn-view">상세</button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>1004</td>
                    <td>경운기 A</td>
                    <td>2025-09-01</td>
                    <td>2025-09-01</td>
                    <td>봉미선</td>
                     <td>
                    	<div class="rv-status done">
		                    <span>반납완료</span>                    	
                    	</div>
                    </td>
                    <td>2025-08-20</td>
                    <td></td>
                </tr>
            </tbody>
        </table>
    </div>

    <!-- pagination -->
    <div class="rv-pagination"> 
        <!-- 페이지네이션 렌더링 -->
    </div>
</div>


<!-- 모달 include (하드코딩된 UI만) -->
<jsp:include page="/WEB-INF/views/admin-reservation/rejectModal.jsp" />
<jsp:include page="/WEB-INF/views/admin-reservation/rejectDetail.jsp" />

<!-- 모달 넣을 자리 -->
<div id="rv-modal-container"></div>
