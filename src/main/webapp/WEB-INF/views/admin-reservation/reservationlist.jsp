<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<div class="reservation-content">
	    <h2 class="page-title">예약관리</h2> 
	    <!-- filter --> 
	    <div class="filter-bar">
		    <div class="filter-list">
		    		<!--  종류 드롭다운   -->
					<div class="custom-dropdown filter-dropdown">
						<label class="custom-dropdown-name">종류</label>
						<div class="dropdown-box">
										<button class="dropdown-toggle">
								    			<span class="dropdown-label" data-value="">전체</span>
								    			<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>">
								  		</button>
								  		<ul class="dropdown-menu">
								  <!-- >>모델들 list로 불러와서 for문으로 뿌려야함 -->
								  		<c:forEach var="category" items="${categoryList }">
								  				<li data-value="${category} ">${category}</li>
								  		</c:forEach>
								  </ul>
						</div>
					</div>
				
		        		<!-- 상태 드롭다운 -->
				<div class="custom-dropdown filter-dropdown">
					<label class="custom-dropdown-name">상태</label>
					<div class="dropdown-box">
						  <button class="dropdown-toggle">
						    	<span class="dropdown-label" data-value="">전체</span>
						    	<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
						  </button>
						  <ul class="dropdown-menu">
							    <li class="active">전체</li>
							    <li data-value="IN_PROGRESS">승인대기</li>
							    <li data-value="COMPLETED">사용중</li>
							    <li data-value="COMPLETED">반려</li>
							    <li data-value="COMPLETED">반납완료</li>
						  </ul>
					</div>
				</div>
				   <div class="custom-dropdown filter-dropdown">
				    <label class="custom-dropdown-name">신청일자</label>
				      <div class="dropdown-box">
				        <input type="text" id="inspectionDate" class="date-input" placeholder="날짜 선택">
					  <span class="calendar-icon">
					    <img src="<c:url value='/assets/maintenance/calendar.svg'/>" alt="달력">
					  </span>
				      </div>
				  </div> 
		     </div>
		     
		     <div class = "filter-container">
 				<!-- 적용된 필터 표시 -->
		    		<div class="active-filters">
		 			적용된 필터 <span class="count"></span>
		 			<div>
			 		     <button class="btn-reset">
			     			<img alt="reset-filter" src="<c:url value='/assets/asset/reset.svg'/>" class ="reset-img">
			     		</button>
		     		</div>
		   		 </div>
		     </div>
	    </div>
	
	    <!-- reservation table -->
	  <div class="table-wrapper">
	    <table class="reservation-table">
			  <thead>
			    <tr>
			      <th>예약번호</th>
			      <th>자원명</th>
			      <th>시작일</th>
			      <th>종료일</th>
			      <th>신청자</th>
			      <th>예약상태</th>
			      <th>신청일시</th>
			      <th>처리</th>
			    </tr>
			  </thead>
  			<tbody>
  			<!-- 리스트 내용  -->
  					<tbody>
  					
  					
  					<!--백 연결후 삭제 -->
    <tr>
        <td>1001</td>
        <td>트랙터 A</td>
        <td>2025-09-15</td>
        <td>2025-09-15</td>
        <td>홍길동</td>
        <td><span class="status pending">승인대기</span></td>
        <td>2025-09-10</td>
        <td>
	        <div class="btn-containter reservation">
			        <div class="btn-box reservation">
			            <button class="btn-approve">승인</button>        
			        </div>
			        <div class="btn-box">
			            <button class="btn-reject">반려</button>
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
        <td><span class="status approved">사용중</span></td>
        <td>2025-09-10</td>
        <td>
	        <div class="btn-box">
	            <button class="btn-complete">반납</button>
	        </div>
        </td>
    </tr>
    <tr>
        <td>1003</td>
        <td>경운기 C</td>
        <td>2025-09-17</td>
        <td>2025-09-17</td>
        <td>이철수</td>
        <td><span class="status rejected">반려됨</span></td>
        <td>2025-09-10 </td>
        <td>
            <button class="btn-view">상세</button>
        </td>
    </tr>
    
     <tr>
        <td>1004</td>
        <td>경운기 A</td>
        <td>2025-09-01</td>
        <td>2025-09-01</td>
        <td>봉미선</td>
        <td><span class="status rejected">반납완료</span></td>
        <td>2025-08-20</td>
        <td>
        </td>
    </tr>
</tbody>
  					
  			
  			
	        </tbody>
	    </table>
	  </div>
	
		<!-- pagination -->
		<div class="pagination"> 
			<!-- 페이지네이션 렌더링  -->
		</div>
	</div>
	
	

	<!-- 모달 넣을 자리 -->
<div id="modal-container"></div>