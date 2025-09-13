<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 로그인</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/login/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>                                     
<body>
    <div class="component">

        <div class="frame">
            <!-- 컨테이너 래퍼 -->
            <div class="content-wrapper">
                <!-- 사이드바 -->
                <div class="sidebar">
                    <div class="sidebar-heading">
                        <div class="sidebar-title">회원</div>
                    </div>
                    <div class="sidebar-menu">
                        <div class="menu-item active">
                            <a href="<c:url value='/login'/>" class="menu-link">
                                <div class="menu-text">로그인</div>
                            </a>
                        </div>
                        <div class="menu-item">
                            <a href="<c:url value='/verification'/>" class="menu-link">
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
                                <div class="menu-text">비밀번호찾기</div>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- 메인 컨텐츠 -->
                <div class="main-content">
                    <div class="page-title">로그인</div>
                    
                    <div class="login-container">
                        <div class="login-title">회원 로그인</div>
                        
                        <!-- 에러 메시지 표시 -->
                        <c:if test="${not empty error}">
                            <div class="error-message">
                                <c:choose>
                                    <c:when test="${error == 'true'}">아이디 또는 비밀번호가 잘못되었습니다.</c:when>
                                    <c:when test="${error == 'expired'}">세션이 만료되었습니다. 다시 로그인해주세요.</c:when>
                                    <c:otherwise>로그인 중 오류가 발생했습니다.</c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        
                        <!-- 성공 메시지 표시 -->
                        <c:if test="${not empty message}">
                            <div class="success-message">${message}</div>
                        </c:if>

                        <!-- 로그인 폼 -->
                        <form id="loginForm" action="#" method="post" class="login-form">
                            <div class="form-group">
                                <input type="text" 
                                       id="userid" 
                                       name="userid" 
                                       placeholder="아이디" 
                                       class="form-input" 
                                       value="${userid}"
                                       required />
                            </div>
                            
                            <div class="form-group">
                                <input type="password" 
                                       id="password" 
                                       name="password" 
                                       placeholder="비밀번호" 
                                       class="form-input" 
                                       required />
                            </div>
                            
                            <div class="checkbox-group">
                                <input type="checkbox" id="remember" name="remember" class="checkbox" />
                                <label for="remember" class="checkbox-label">로그인 상태 유지</label>
                            </div>
                            
                            <button type="button" class="login-button" id="loginBtn">
                                <span class="button-text">로그인</span>
                                <span class="loading-spinner" style="display: none;">로그인 중...</span>
                            </button>
                            
                            <!-- CSRF 토큰 (Spring Security 사용 시) -->
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        </form>
                        
                        <!-- 링크 메뉴 -->
                        <div class="link-menu">
                            <a href="<c:url value='/verification'/>" class="link-item">회원가입</a>
                            <div class="vertical-divider"></div>
                            <a href="<c:url value='/find-id'/>" class="link-item">아이디 찾기</a>
                            <div class="vertical-divider"></div>
                            <a href="<c:url value='/reset-password'/>" class="link-item">비밀번호찾기</a>
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
                <span class="breadcrumb-item current">로그인</span>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        $(document).ready(function() {
            // 로그인 버튼 클릭 처리
            $('#loginBtn').on('click', function(e) {
                e.preventDefault();
                handleLogin();
            });
            
            // Enter 키 처리
            $('.form-input').on('keypress', function(e) {
                if (e.which === 13) {
                    e.preventDefault();
                    handleLogin();
                }
            });
            
            function handleLogin() {
                const userid = $('#userid').val().trim();
                const password = $('#password').val().trim();
                
                // 유효성 검사
                if (!userid) {
                    alert('아이디를 입력해주세요.');
                    $('#userid').focus();
                    return;
                }
                
                if (!password) {
                    alert('비밀번호를 입력해주세요.');
                    $('#password').focus();
                    return;
                }
                
                // 로딩 상태 변경
                const $loginBtn = $('#loginBtn');
                const $buttonText = $('.button-text');
                const $loadingSpinner = $('.loading-spinner');
                
                $loginBtn.prop('disabled', true);
                $buttonText.hide();
                $loadingSpinner.show();
                
                // AJAX 로그인 요청
                $.ajax({

                    url: '<c:url value="/api/login"/>',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        loginId: userid,
                        password: password
                    }),
                    success: function(response) {
                        if (response.success) {
                            // 로그인 성공
                            console.log('로그인 성공:', response);
                            
                            // sessionStorage에서 returnUrl 확인
                            const returnUrl = sessionStorage.getItem('returnUrl');
                            
                            if (returnUrl) {
                                // 저장된 URL이 있으면 해당 페이지로 이동
                                sessionStorage.removeItem('returnUrl'); // 사용 후 제거
                                console.log('저장된 returnUrl로 이동:', returnUrl);
                                window.location.href = returnUrl;
                            } else if (response.data && response.data.returnUrl) {
                                // 서버에서 returnUrl이 온 경우
                                console.log('서버 returnUrl로 이동:', response.data.returnUrl);
                                window.location.href = response.data.returnUrl;
                            } else {
                                // 사용자 권한에 따라 다른 페이지로 이동
                                const authorities = response.data.authorities;
                                console.log('사용자 권한:', authorities);
                                
                                // ADMIN 권한이 있는지 확인
                                const isAdmin = authorities && authorities.some(auth => 
                                    auth.authority === 'ROLE_ADMIN' || auth === 'ROLE_ADMIN'
                                );
                                
                                if (isAdmin) {
                                    console.log('관리자로 로그인 - admin/dashboard로 이동');
                                    alert('관리자로 로그인되었습니다.');
                                    window.location.href = '<c:url value="/admin/dashboard"/>';
                                } else {
                                    console.log('일반사용자로 로그인 - resource/list로 이동');
                                    alert('로그인되었습니다.');
                                    window.location.href = '<c:url value="/resource/list"/>';
                                }
                            }
                        } else {
                            // 로그인 실패
                            alert(response.message || '로그인에 실패했습니다.');
                            resetLoginButton();
                        }
                    },
                    error: function(xhr) {
                        let errorMessage = '로그인 중 오류가 발생했습니다.';
                        
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            errorMessage = xhr.responseJSON.message;
                        } else if (xhr.status === 401) {
                            errorMessage = '아이디 또는 비밀번호가 잘못되었습니다.';
                        }
                        
                        alert(errorMessage);
                        resetLoginButton();
                    }
                });
                
                function resetLoginButton() {
                    $loginBtn.prop('disabled', false);
                    $buttonText.show();
                    $loadingSpinner.hide();
                    $('#password').val(''); // 비밀번호 필드 초기화
                }
            }
            
            // 에러 메시지가 있으면 포커스
            if ($('.error-message').length > 0) {
                $('#userid').focus();
            }
        });
    </script>
</body>
</html>
                