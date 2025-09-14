<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<div class="maintenance-content">
	    <h2 class="page-title">점검관리</h2> 
	    <!-- filter/search -->
	    <div class="search-bar">
		        <div class="search-dropdown">
					  <button class="search-dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
							  <span class="search-dropdown-label" data-value="" >모델명</span>
  							  <img src="<c:url value='/assets/maintenance/arrow-down.svg' />" alt="▼" class="search-dropdown-btn">
						</button>
					  <ul class="search-dropdown-menu">
					    <li data-value="assetName">모델명</li>
					    <li data-value="adminName">담당자</li>
					  </ul>
				</div>
		    <input type="text" class="search-input" placeholder="Search" id="keyword">
		    <button type="button"  id="btnSearch" class="search-btn">
		        <img alt="search-icon" src="<c:url value='/assets/asset/search.svg'/>">
		    </button>
		</div>

	    
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
								  		<c:forEach var="category" items="${categories }">
								  				<li data-value="${category} ">${category}</li>
								  		</c:forEach>
								  </ul>
						</div>
					</div>

				<!-- 제조사 드롭다운 -->
				<div class="custom-dropdown filter-dropdown">
					<label class="custom-dropdown-name">제조사</label>
					<div class="dropdown-box">
					  	<button class="dropdown-toggle">
					   		 <span class="dropdown-label" data-value="">전체</span>
					    	<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>" >
					  </button>
					  <ul class="dropdown-menu">
					  		<c:forEach var="company" items="${companies }">
						  				<li data-value="${company} ">${company}</li>
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
							    <li data-value="IN_PROGRESS">점검중</li>
							    <li data-value="COMPLETED">점검완료</li>
						  </ul>
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
	
	     <button class="btn-download">
	     			<img alt="download-list" src="<c:url value='/assets/asset/download.svg'/>">
	     </button>
	    <!-- maintenance table -->
	  <div class="table-wrapper">
	    <table class="maintenance-table">
			  <thead>
			    <tr>
			      <th>No</th>
			      <th>종류</th>
			      <th>모델명</th>
				  <th class="sortable" data-sort="maintDate" data-order="desc">점검 일시</th>
			      <th>점검 유형</th>
			      <th>점검 상태</th>
			      <th>담당자</th>
			      <th></th>
			    </tr>
			  </thead>
  			<tbody>
  			<!-- 리스트 내용  -->
	        </tbody>
	    </table>
	  </div>
	
		<!-- pagination -->
		<div class="pagination"> 
			<!-- 페이지네이션 렌더링  -->
		</div>
	</div>
	
	
	

<!-- 모달 include (하드코딩된 UI만) -->
<jsp:include page="/WEB-INF/views/maintenance/inspectionModal.jsp" />
 <jsp:include page="/WEB-INF/views/maintenance/inspectionEditModal.jsp" />



	<!-- 모달 넣을 자리 -->
<div id="modal-container"></div>


<script>
  var contextPath = "${pageContext.request.contextPath}";
</script>

