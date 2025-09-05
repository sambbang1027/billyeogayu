<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
	<div class="maintenance-container">
	    <h2 class="page-title">점검관리</h2> 
	    <!-- filter/search -->
	    <div class="search-bar">
		        <div class="custom-dropdown">
					  <button class="dropdown-toggle">자원명</button>
					  <ul class="dropdown-menu">
					    <li >자원명</li>
					    <li>점검자</li>
					  </ul>
				</div>
		    <input type="text" class="search-input" placeholder="Search">
		    <button type="submit" class="search-btn">
		        <img alt="search-icon" src="<c:url value='/assets/maintenance/search.png'/>">
		    </button>
		</div>

	    
	    <div class="filter-bar">
	        <div class="filter-item">
	            <label>제조사</label>
	            <select>
	                <option value="">전체</option>
	                <option value="현대">현대</option>
	                <option value="대동">대동</option>
	            </select>
	        </div>
	        <div class="filter-item">
	            <label>위치</label>
	            <select>
	                <option value="">전체</option>
	                <option value="창고1">창고1</option>
	                <option value="창고2">창고2</option>
	            </select>
	        </div>
	        <div class="filter-item">
	            <label>상태</label>
	            <select>
	                <option value="">전체</option>
	                <option value="점검중">점검중</option>
	                <option value="완료">완료</option>
	            </select>
	        </div>
	        <button class="btn-reset">필터 초기화</button>
 				<!-- 적용된 필터 표시 -->
		    <div class="active-filters">
		        적용된 필터 <span class="count">2</span>
		        <span class="filter-tag">트랙터 ✕</span>
		        <span class="filter-tag">사용중 ✕</span>
		    </div>
	    </div>
	
	
	     <button class="btn-download">엑셀 다운로드</button>
	    <!-- maintenance table -->
	    <table class="maintenance-table">
	        <thead>
	            <tr>
	                <th>No</th>
	                <th>자원명</th>
	                <th>점검 일시</th>
	                <th>점검 유형</th>
	                <th>점검 상태</th>
	                <th>담당자</th>
	            </tr>
	        </thead>
	        <tbody>
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
	                            <c:otherwise>
	                                <span class="status-pending">대기</span>
	                            </c:otherwise>
	                        </c:choose>
	                    </td>
	                    <td>${row.manager}</td>
	                </tr>
	            </c:forEach>
	        </tbody>
	    </table>
	
	    <!-- pagination -->
	    <div class="pagination">
	        <a href="#">«</a>
	        <a href="#" class="active">1</a>
	        <a href="#">2</a>
	        <a href="#">3</a>
	        <a href="#">4</a>
	        <a href="#">5</a>
	        <a href="#">»</a>
	    </div>
	
	</div>
