<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - 페이지를 찾을 수 없습니다</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
    <div class="container">
        <div class="error-page">
            <div class="error-content">
                <h1 class="error-code">404</h1>
                <h2 class="error-title">페이지를 찾을 수 없습니다</h2>
                <p class="error-message">
                    요청하신 페이지를 찾을 수 없습니다.<br>
                    URL을 확인하시거나 홈페이지로 돌아가시기 바랍니다.
                </p>
                <div class="error-actions">
                    <a href="<c:url value='/'/>" class="btn btn-primary">홈으로 돌아가기</a>
                    <button onclick="history.back()" class="btn btn-secondary">이전 페이지</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>