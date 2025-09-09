<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <title><c:out value="${pageTitle}" /></title>
        <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/style.css'/>">
      	<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
        <!-- 달력 flatpickr css/js -->
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
		<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
		<!-- 드롭다운 (멀티)Choices.js css/js -->
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/choices.js/public/assets/styles/choices.min.css" />
		<script src="https://cdn.jsdelivr.net/npm/choices.js/public/assets/scripts/choices.min.js"></script>
		      
        <c:if test="${activePage eq 'asset'}">
            <link rel="stylesheet" href="<c:url value='/static/css/asset/assetlist/style.css'/>">
        </c:if>
         <c:if test="${activePage eq 'maintenance'}">
          	    <link rel="stylesheet" href="<c:url value='/static/css/maintenance/maintenance.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/custom-dropdown.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/search-dropdown.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/inspection-modal.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/inspection-apply.css'/>">
				<script src="<c:url value='/static/js/maintenance/customDropdown.js'/>"></script>
				<script src="<c:url value='/static/js/maintenance/searchDropdown.js'/>"></script>
				<script src="<c:url value='/static/js/maintenance/maintenanceDetail.js'/>"></script>				
				<script src="<c:url value='/static/js/maintenance/maintenanceApply.js'/>"></script>				
				<script src="<c:url value='/static/js/maintenance/maintenance.js'/>"></script>
        </c:if>
    </head>
    <body>
        <div class="container">
            <div class="sidebar-container">
                <jsp:include page="/WEB-INF/views/layout/admin/sidebar.jsp" />
            </div>

            <div class="right-container">
                <div class="header-container">
                    <jsp:include page="/WEB-INF/views/layout/admin/header.jsp" />
                </div>

                <div class="content-container">
                    <jsp:include page="${contentPage}" />
                </div>
            </div>
        </div>
    </body>
</html>