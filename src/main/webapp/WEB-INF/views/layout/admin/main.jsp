<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
		
		<!-- 공통 모달(confirm/ alert) -->
		<link rel="stylesheet" href="<c:url value='/static/css/common/commonModal.css'/>">
		<script src="<c:url value='/static/js/common/commonModal.js'/>"></script>

        <c:if test="${activePage eq 'asset'}">
            <link rel="stylesheet" href="<c:url value='/static/css/asset/assetlist/style.css'/>">
            
            <link rel="stylesheet" href="<c:url value='/static/css/maintenance/custom-dropdown.css'/>">
            <link rel="stylesheet" href="<c:url value='/static/css/maintenance/inspection-apply.css'/>">
			<script src="<c:url value='/static/js/common/dropdown.js'/>"></script>
			<script src="<c:url value='/static/js/maintenance/inspectionApply.js'/>"></script>
            <script src="<c:url value='/static/js/maintenance/maintenanceApply.js'/>"></script>			
            	
            
        </c:if>
         <c:if test="${activePage eq 'maintenance'}">
          	    <link rel="stylesheet" href="<c:url value='/static/css/maintenance/maintenance.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/custom-dropdown.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/search-dropdown.css'/>">
		 		<link rel="stylesheet" href="<c:url value='/static/css/maintenance/inspection-modal.css'/>">
				<script src="<c:url value='/static/js/common/dropdown.js'/>"></script>
				<script src="<c:url value='/static/js/maintenance/filter.js'/>"></script>
				<script src="<c:url value='/static/js/maintenance/searchDropdown.js'/>"></script>
				<script src="<c:url value='/static/js/maintenance/maintenanceDetail.js'/>"></script>					
				<script src="<c:url value='/static/js/maintenance/maintenance.js'/>"></script>
        </c:if>
        <c:if test="${activePage eq 'dashboard'}">
            <link rel="stylesheet" href="<c:url value='/static/css/dashboard/style.css'/>">
            <!-- Chart.js -->
            <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns@3.0.0/dist/chartjs-adapter-date-fns.bundle.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@2.0.1/dist/chartjs-plugin-zoom.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-annotation@3.0.1/dist/chartjs-plugin-annotation.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0/dist/chartjs-plugin-datalabels.min.js"></script>

            <script src="<c:url value='/static/js/dashboard/dashboard.js'/>" defer></script>
        </c:if>
        
        <c:if test="${activePage eq 'reservation'}">
        		<link rel="stylesheet" href="<c:url value='/static/css/admin-reservation/reservation.css'/>">
        		<link rel="stylesheet" href="<c:url value='/static/css/admin-reservation/reservationFilter.css'/>">
        		<link rel="stylesheet" href="<c:url value='/static/css/admin-reservation/rejectModal.css'/>">
        		<script src="<c:url value='/static/js/admin-reservation/reservationFilter.js'/>"></script>
        		 <script src="<c:url value='/static/js/admin-reservation/reservation.js'/>"></script>
         		 <script src="<c:url value='/static/js/admin-reservation/action.js'/>"></script>
        		
        		
        		
        </c:if>
    </head>

<body>
<div class="container">
    <div class="sidebar-container">
        <jsp:include page="/WEB-INF/views/layout/admin/sidebar.jsp"/>
    </div>

    <div class="right-container">
        <div class="header-container">
            <jsp:include page="/WEB-INF/views/layout/admin/header.jsp"/>
        </div>

        <div class="content-container">
            <jsp:include page="${contentPage}"/>
        </div>
    </div>
</div>

<!-- 공통 모달 JSP include -->
<jsp:include page="/WEB-INF/views/common/commonModal.jsp"/>
</body>

</html>