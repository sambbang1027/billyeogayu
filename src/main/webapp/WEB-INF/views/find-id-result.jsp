<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 아이디 찾기 결과</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/verification/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
    <div class="component">
        <!-- 헤더 -->
        <div class="header">
            <div class="header-content">
                <img class="logo" src="<c:url value='/static/images/logo.png'/>" alt="빌려가유 로고" />
                <div class="header-links">
                    <span class="header-link">회원가입</span>
                    <span class="header-link">로그인</span>
                    <span class="header-link">마이페이지</span>
                    <span class="header-link">로그아웃</span>
                </div>
            </div>
        </div>

        <div class="frame">
            <!-- 사이드바 -->
            <div class="sidebar">
                <div class="sidebar-heading">
                    <div class="sidebar-title">회원</div>
                </div>
                <div class="sidebar-menu">
                    <div class="menu-item">
                        <a href="<c:url value='/login'/>" class="menu-link">
                            <div class="menu-text">로그인</div>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/verification?purpose=register'/>" class="menu-link">
                            <div class="menu-text">회원가입</div>
                        </a>
                    </div>
                    <div class="menu-item active">
                        <a href="<c:url value='/verification?purpose=find-id'/>" class="menu-link">
                            <div class="menu-text">아이디찾기</div>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/verification?purpose=reset-password'/>" class="menu-link">
                            <div class="menu-text">비밀번호 재설정</div>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content">
                <div class="page-title">아이디 찾기</div>
                
                <div class="register-container">
                    <div class="result-container">
                        <!-- 아이콘 -->
                        <div class="result-icon">
                            <img class="vector" src="vector.svg" />
                        </div>

                        <div class="result-title">
                            사용자님의 아이디는 <strong>"${foundId}"</strong> 입니다.
                        </div>
                        
                        
                        <!-- 버튼 그룹 -->
                        <div class="button-group">
                            <button type="button" class="modal-button secondary" onclick="location.href='<c:url value="/verification?purpose=reset-password"/>'">
                                비밀번호 재설정
                            </button>
                            <button type="button" class="modal-button" onclick="location.href='<c:url value="/login"/>'">
                                로그인하기
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 브레드크럼 -->
            <div class="breadcrumb">
                <span class="breadcrumb-item">홈</span>
                <span class="breadcrumb-arrow">></span>
                <span class="breadcrumb-item">회원</span>
                <span class="breadcrumb-arrow">></span>
                <span class="breadcrumb-item current">아이디 찾기</span>
            </div>
        </div>
    </div>

    <style>
        .result-container {
            text-align: center;
            padding: 60px 20px;
            max-width: 500px;
            margin: 0 auto;
        }
        
        .result-icon {
            font-size: 80px;
            margin-bottom: 30px;
        }
        
        .result-title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 20px;
        }
        
        .result-message {
            font-size: 18px;
            color: #555;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .result-message strong {
            color: #2E7D32;
            font-weight: bold;
            font-size: 20px;
        }
        
        .auth-info {
            background-color: #f5f5f5;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 40px;
            font-size: 14px;
            color: #666;
        }
        
        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
        }
        
        .modal-button {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.2s;
            min-width: 120px;
        }
        
        .modal-button.secondary {
            background-color: #f5f5f5;
            color: #333;
        }
        
        .modal-button.secondary:hover {
            background-color: #e0e0e0;
        }
        
        .modal-button:not(.secondary) {
            background-color: #2E7D32;
            color: white;
        }
        
        .modal-button:not(.secondary):hover {
            background-color: #1B5E20;
        }
    </style>
</body>
</html>