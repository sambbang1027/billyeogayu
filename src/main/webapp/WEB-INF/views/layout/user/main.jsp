<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>빌려가유 - 농기계 공유 플랫폼</title>

<!-- Google Fonts 추가 - Noto Sans KR로 통일 -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">

<!-- CSS 파일 링크 -->
<link rel="stylesheet" href="<c:url value='/static/css/layout/main/style.css'/>">
</head>
<body>
<div class="container">

<!-- 데코레이션 요소들 -->
<div class="decoration decoration-1"></div>
<div class="decoration decoration-2"></div>
<div class="decoration decoration-3">🌱</div>
<div class="decoration decoration-4">🏡</div>

<header class="header">
<div class="logo-container">
<h1 class="main-title">"농기계, 이제 <span class="highlight">빌려쓰자!</span>"</h1>
</div>
<h2 class="subtitle">
<img src="<c:url value='/assets/layout/user/main.png'/>" alt="빌려가유 로고" class="logo-image"
onerror="this.style.display='none';">에서 쉽고 빠르게
</h2>
<div class="description">
<p>누구나 쉽게 접속해 필요한 농기계를 찾아보고,</p>
<p>비용 걱정 없이 바로 사용할 수 있습니다.</p>
</div>
</header>

<div class="action-buttons">
<a href="<c:url value='/login'/>" class="btn btn-primary">로그인으로 시작하기</a>
<a href="<c:url value='/resource/list'/>" class="btn btn-secondary">농기계 조회하기</a>
</div>

<div class="main-visual">
<img src="<c:url value='/assets/layout/user/map.png'/>" alt="농기계 대여 과정" class="visual-content"
onerror="this.style.display='none'; document.getElementById('visual-fallback').style.display='block';">
<div id="visual-fallback" class="visual-fallback" style="display: none;">
🚜 농기계 공유 플랫폼 빌려가유 🚜<br>
<div class="fallback-detail">
📍 신청 → 조회 → 수령 → 반납<br>
간편한 4단계로 농기계를 빌려보세요!
</div>
</div>
</div>
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</body>
</html>