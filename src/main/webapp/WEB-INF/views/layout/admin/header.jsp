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
    </head>
    <body>
        <div class="header-container">
            <div class="alarm-container">
                <div class="alarm-img-box">
                    <img class="alarm-img"
                         src="<c:url value='/assets/layout/admin/alarm.svg'/>"
                         alt="알람" />
                </div>
                <div class="alarm-text-box">
                    23
                </div>
            </div>
            <div class="user-container">
                <div class="user-img-box">
                    <img class="user-img"
                         src="<c:url value='/assets/layout/admin/person.svg'/>"
                         alt="유저" />
                </div>
                <div class="alarm-text-box">
                    관리자 A
                </div>
            </div>
            <div class="logout-img-box">
                <img class="logout-img"
                     src="<c:url value='/assets/layout/admin/logout.svg'/>"
                     alt="로그아웃" />
            </div>
        </div>
    </body>
</html>