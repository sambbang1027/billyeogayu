<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 회원가입</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/register/style.css'/>">
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
                    <div class="menu-item active">
                        <a href="<c:url value='/register'/>" class="menu-link">
                            <div class="menu-text">회원가입</div>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/find-id'/>" class="menu-link">
                            <div class="menu-text">아이디찾기</div>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/reset-password'/>" class="menu-link">
                            <div class="menu-text">비밀번호 재설정</div>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content">
                <div class="page-title">회원가입</div>
                
                <div class="register-container">
                    <div class="register-title">본인 확인</div>
                    
                    <!-- 안내 박스 -->
                    <div class="info-box">
                        <div class="info-list">
                            <div class="info-item">
                                <div class="info-text">아이디를 잊으신 경우 휴대폰인증 또는 이메일 인증을 통해 아이디를 찾을 수 있습니다.
                                입력하신 정보는 본인확인을 위해 사용되며, 본인확인 용도 외에 사용되거나 저장되지 않습니다.
                                개인정보는 본인 동의 없이 공개되지 않으며 개인정보보호정책에 의해 보호받고 있습니다.</div>
                            </div>
                        </div>
                    </div>

                    <!-- 인증 방법 선택 -->
                    <div class="auth-methods">
                        <div class="auth-method">
                            <div class="auth-title">이메일 인증</div>
                            <div class="auth-description">이메일으로 본인인증을 진행합니다.</div>
                            <button class="auth-button email-auth" type="button">
                                이메일으로 인증하기
                            </button>
                        </div>
                        
                        <div class="auth-method">
                            <div class="auth-title">휴대폰 인증</div>
                            <div class="auth-description">본인명의 휴대폰으로 본인인증을 진행합니다.</div>
                            <button class="auth-button phone-auth" type="button">
                                휴대폰으로 인증하기
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
                <span class="breadcrumb-item current">회원가입</span>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        $(document).ready(function() {
            // 이메일 인증 버튼 클릭
            $('.email-auth').on('click', function() {
                // 이메일 인증 페이지로 이동
                window.location.href = '<c:url value="/register/email"/>';
            });
            
            // 휴대폰 인증 버튼 클릭
            $('.phone-auth').on('click', function() {
                // 휴대폰 인증 페이지로 이동
                window.location.href = '<c:url value="/register/phone"/>';
            });
        });
    </script>
</body>
</html>