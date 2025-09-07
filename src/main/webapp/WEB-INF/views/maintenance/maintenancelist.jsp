<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
	<div class="maintenance-content">
	    <h2 class="page-title">점검관리</h2> 
	    <!-- filter/search -->
	    <div class="search-bar">
		        <div class="search-dropdown">
					  <button class="search-dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
							  <span class="search-dropdown-label">자원명</span>
  							  <img src="<c:url value='/assets/maintenance/arrow-down.svg' />" alt="▼" class="search-dropdown-btn">
						</button>
					  <ul class="search-dropdown-menu">
					    <li >자원명</li>
					    <li>담당자</li>
					  </ul>
				</div>
		    <input type="text" class="search-input" placeholder="Search">
		    <button type="submit" class="search-btn">
		        <img alt="search-icon" src="<c:url value='/assets/asset/search.svg'/>">
		    </button>
		</div>

	    
	    <div class="filter-bar">
		    <div class="filter-list">
		    		<!--  종류 드롭다운   -->
					<div class="custom-dropdown">
						<label class="custom-dropdown-name">종류</label>
						<div class="dropdown-box">
										<button class="dropdown-toggle">
								    			<span class="dropdown-label">전체</span>
								    			<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>">
								  		</button>
								  		<ul class="dropdown-menu">
								  <!-- >>모델들 list로 불러와서 for문으로 뿌려야함 -->
										    	<li class="active">전체</li>
										    	<li>트랙터</li>
										    	<li>제초기</li>
								  </ul>
						</div>
					</div>

				<!-- 제조사 드롭다운 -->
				<div class="custom-dropdown">
					<label class="custom-dropdown-name">제조사</label>
					<div class="dropdown-box">
					  	<button class="dropdown-toggle">
					   		 <span class="dropdown-label">전체</span>
					    	<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
					  </button>
					  <ul class="dropdown-menu">
						    <li class="active">전체</li>
						    <li>현대</li>
						    <li>대동</li>
					  </ul>
					</div>
				</div>

				<!-- 위치 드롭다운 -->
				<div class="custom-dropdown">
					<label class="custom-dropdown-name">위치</label>
					<div class="dropdown-box">
					  <button class="dropdown-toggle">
					   		 <span class="dropdown-label">전체</span>
					    	 <img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
					  </button>
					  <ul class="dropdown-menu">
						    <li class="active">전체</li>
						    <li>창고1</li>
						    <li>창고2</li>
					  </ul>
					</div>
				</div>
				
		        		<!-- 상태 드롭다운 -->
				<div class="custom-dropdown">
					<label class="custom-dropdown-name">종류</label>
					<div class="dropdown-box">
						  <button class="dropdown-toggle">
						    	<span class="dropdown-label">전체</span>
						    	<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
						  </button>
						  <ul class="dropdown-menu">
							    <li class="active">전체</li>
							    <li>점검중</li>
							    <li>점검완료</li>
						  </ul>
					</div>
				</div>
				
		     </div>
		     <div class = "filter-container">
 				<!-- 적용된 필터 표시 -->
		    		<div class="active-filters">
		 			적용된 필터 <span class="count">2</span>
		 			<div>
			 		     <button class="btn-reset">
			     			<img alt="reset-filter" src="<c:url value='/assets/asset/reset.svg'/>" class ="reset-img">
			     		</button>
		     		</div>
		     		<div class="filter-tag">
		     			<text class="filter-name">트랙터</text>
		     			 <img alt="cancel-filter" src="<c:url value='/assets/asset/xbtn.svg'/>" class="filter-cancel">
		     		</div>
		   		 </div>
		     </div>
	    </div>
	
	
	     <button class="btn-download">
	     			<img alt="download-list" src="<c:url value='/assets/asset/download.svg'/>">
	     </button>
	    <!-- maintenance table -->
	  <div class="table-wrapper">
	    <table class="maintenance-table">
			  <thead>
			    <tr>
			      <th>No</th>
			      <th>자원명</th>
			      <th>점검 일시</th>
			      <th>점검 유형</th>
			      <th>점검 상태</th>
			      <th>담당자</th>
			      <th></th>
			    </tr>
			  </thead>
			  <tbody>
				    <tr>
				        <td>1</td>
				        <td>트랙터 A</td>
				        <td>2025.09.01</td>
				        <td>정기</td>
				        <td>
				        	<div class="status-complete">
				        		<img class="complete-img" src="<c:url value='/assets/asset/canuse.svg'/>" >
				       	 		<span >점검완료</span>
				        	</div>
				        </td>
				        <td>매니저1</td>
				          <td>
						    <button class="edit-btn">
						      <img src="<c:url value='/assets/maintenance/edit-btn.svg'/>" alt="수정">
						    </button>
						  </td>
				    </tr>
				    <tr>
				        <td>2</td>
				        <td>예초기 B</td>
				        <td>2025.09.05</td>
				        <td>임시</td>
				        <td>
				        	<div class="status-progress">
				        		<img class="progress-img" src="<c:url value='/assets/asset/using.svg'/>" >
				       	 		 <span >점검중</span>
				        	</div>
				       </td>
				        <td>매니저2</td>
				        <td>
						    <button class="edit-btn">
						      <img src="<c:url value='/assets/maintenance/edit-btn.svg'/>" alt="수정">
						    </button>
						  </td>
				    </tr>
				    <tr>
				        <td>3</td>
				        <td>콤바인 C</td>
				        <td>2025.09.07</td>
				        <td>정기</td>
				        <td>
				        	<div class="status-complete">
				        		<img class="complete-img" src="<c:url value='/assets/asset/canuse.svg'/>" >
				       	 		<span >점검완료</span>
				        	</div>
				        </td>
				        <td>매니저3</td>
				        <td>
						    <button class="edit-btn">
						      <img src="<c:url value='/assets/maintenance/edit-btn.svg'/>" alt="수정">
						    </button>
						  </td>
				    </tr>
				</tbody>
			  
			  
			  
			  
			  
			  
	      <%--   <tbody>
	            <c:forEach var="row" items="${maintenanceList}" varStatus="status">
	                <tr>
	                    <td>${status.index + 1}</td>
	                    <td>${row.machineName}</td>
	                    <td><fmt:formatDate value="${row.maintenanceDate}" pattern="yyyy.MM.dd"/></td>
	                    <td>${row.type}</td>
	                    <td>
	                        <c:choose>
	                            <c:when test="${row.status eq '완료'}">
	                                <span class="status-complete">점검완료</span>
	                            </c:when>
	                            <c:when test="${row.status eq '점검중'}">
	                                <span class="status-progress">점검중</span>
	                            </c:when>
	                        </c:choose>
	                    </td>
	                    <td>${row.manager} </td>
	                </tr>
	            </c:forEach>
	        </tbody> --%>
	    </table>
	  </div>
	
	    <!-- pagination -->
		<div class="pagination">
				  <a href="#" class="arrow prev">
				    		<img  alt="이전"  src="<c:url value='/assets/asset/left.svg'/>">
				  </a>
				  <a href="#">1</a>
				  <a href="#">2</a>
				  <a href="#" class="active">3</a>
				  <a href="#">4</a>
				  <a href="#">5</a>
				  <a href="#" class="arrow next">
				    		<img alt="다음" src="<c:url value='/assets/asset/right.svg'/>">
				  </a>
		</div>
	</div>
	

<!-- 모달 include (하드코딩된 UI만) -->
<jsp:include page="/WEB-INF/views/maintenance/inspectionModal.jsp" />
<%-- <jsp:include page="/WEB-INF/views/maintenance/inspectionEditModal.jsp" /> --%>

<!-- 모달은 기본 숨김 -->
<style>
  #inspectionModal, #inspectionEditModal { display: none; }
</style>


	<!-- 모달 넣을 자리 -->
<div id="modal-container"></div>