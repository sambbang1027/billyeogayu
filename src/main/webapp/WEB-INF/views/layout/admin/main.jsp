<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <title><c:out value="${pageTitle}" /></title>
          <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/style.css'/>">
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