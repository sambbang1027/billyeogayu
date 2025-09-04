<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    </head>
    <body>
        <ul>
            <li><a href="<c:url value='/admin/dashboard'/>">대시보드</a></li>
            <li><a href="<c:url value='/admin/asset'/>">사용자 관리</a></li>
            <li><a href="<c:url value='/admin/reservation'/>">통계</a></li>
        </ul>
    </body>
</html>