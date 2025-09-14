<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
         <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/header/style.css'/>">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="<c:url value='/static/js/admin/header.js'/>"></script>

        <!-- 알람 모달 전용 CSS/JS -->
        <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/alarm/style.css'/>">
        <script src="<c:url value='/static/js/alarm/alarm.js'/>" defer></script>

    </head>
    <body>
        <div class="header-container">
            <div class="alarm-container">
                <div class="alarm-img-box">
                    <img class="alarm-img"
                         src="<c:url value='/assets/layout/admin/alarm.svg'/>"
                         alt="알람" />
                </div>
                
                <div class="alarm-text-box" id="alarmCount">
                    <span class="alarm-count">0</span>
                </div>
            </div>
            <div class="user-container">
                <div class="user-img-box">
                    <img class="user-img"
                         src="<c:url value='/assets/layout/admin/person.svg'/>"
                         alt="유저" />
                </div>
                <div class="user-text-box">
                    관리자 A
                </div>
            </div>
            <div class="logout-img-box">
                <img class="logout-img"
                     src="<c:url value='/assets/layout/admin/logout.svg'/>"
                     alt="로그아웃" />
            </div>
        </div>

        <!-- 🔔 알람 모달 전역 include -->
        <jsp:include page="/WEB-INF/views/layout/admin/alarm.jsp"/>

    </body>
</html>