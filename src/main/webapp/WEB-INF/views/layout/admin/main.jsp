<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <title><c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/style.css'/>">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- 자산 리스트 -->
    <c:if test="${activePage eq 'asset'}">
        <link rel="stylesheet" href="<c:url value='/static/css/asset/assetlist/style.css'/>">
    </c:if>

    <!-- 대시보드 -->
    <c:if test="${activePage eq 'dashboard'}">
        <link rel="stylesheet" href="<c:url value='/static/css/dashboard/style.css'/>">
        <!-- Chart.js: UMD 번들 -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns@3.0.0/dist/chartjs-adapter-date-fns.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@2.0.1/dist/chartjs-plugin-zoom.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-annotation@3.0.1/dist/chartjs-plugin-annotation.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0/dist/chartjs-plugin-datalabels.min.js"></script>

        <!-- 대시보드 전용 JS: 반드시 Chart.js 다음, defer 권장 -->
        <script src="<c:url value='/static/js/dashboard/dashboard.js'/>" defer></script>
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

</body>

</html>