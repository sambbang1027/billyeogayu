<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 농기계 공유 플랫폼</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/main/style.css'/>">
</head>
<body>
    <div class="container">
        <!-- 로그인된 사용자 정보 (로그인 시에만 표시) -->
        <c:if test="${isLoggedIn}">
            <div class="user-info">
                <span class="user-greeting">${loginUser.name}님 환영합니다!</span>
                <button class="logout-btn" onclick="logout()">로그아웃</button>
            </div>
        </c:if>


        <header class="header">
            <div class="logo-container">
                <h1 class="main-title">"농기계, 이제 <span class="highlight">빌려쓰자!</span>"</h1>
            </div>
            <h2 class="subtitle">
                <span class="tractor-icon">🚜</span>
                <img src="<c:url value='/static/images/logo.png'/>" alt="빌려가유" class="logo-image"
                     onerror="this.outerHTML='<span class=&quot;logo-text&quot;>빌려가유</span>';">
                에서 쉽고 빠르게
            </h2>
            <div class="description">
                <p>누구나 쉽게 접속해 필요한 농기계를 찾아보고,</p>
                <p>비용 걱정 없이 바로 사용할 수 있습니다.</p>
            </div>
        </header>

        <div class="action-buttons">
            <c:choose>
                <c:when test="${isLoggedIn}">
                    <!-- 로그인된 상태 -->
                    <a href="<c:url value='/resource/list'/>" class="btn btn-primary">농기계 조회하기</a>
                    <a href="<c:url value='/reservation/list'/>" class="btn btn-secondary">내 예약 현황</a>
                </c:when>
                <c:otherwise>
                    <!-- 로그아웃된 상태 -->
                    <a href="<c:url value='/login'/>" class="btn btn-primary">로그인하기</a>
                    <a href="<c:url value='/resource/list'/>" class="btn btn-secondary">농기계 조회하기</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="main-visual">
            <!-- 실제 파일 경로로 수정 -->
            <img src="<c:url value='/assets/layout/user/map.png'/>" alt="" class="visual-content" 
                 onerror="this.style.display='none'; document.getElementById('visual-fallback').style.display='block';">
            <div id="visual-fallback" class="visual-fallback" style="display: none;">
                🚜 농기계 공유 플랫폼 빌려가유 🚜<br>
                <div class="fallback-detail">
                    📍 신청 → 조회 → 수령 → 반납<br>
                    간편한 4단계로 농기계를 빌려보세요!
                </div>
            </div>
        </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    
    <script>
        // 로그아웃 처리
        function logout() {
            if (confirm('로그아웃 하시겠습니까?')) {
                $.ajax({
                    url: '<c:url value="/api/logout"/>',
                    type: 'POST',
                    success: function(data) {
                        if (data.success) {
                            alert('로그아웃되었습니다.');
                            window.location.reload();
                        } else {
                            alert('로그아웃 처리 중 오류가 발생했습니다.');
                        }
                    },
                    error: function() {
                        // 오류가 발생해도 로그아웃 처리
                        alert('로그아웃되었습니다.');
                        window.location.reload();
                    }
                });
            }
        }
    </script>
</body>
</html>