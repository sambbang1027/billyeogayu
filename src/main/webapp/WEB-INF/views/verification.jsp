<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>빌려가유 - 회원가입</title>
<link rel="stylesheet"
	href="<c:url value='/static/css/layout/user/verification/style.css'/>">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
	<div class="component">


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
					<div class="menu-item ${activePage == 'register' ? 'active' : ''}">
						<a href="<c:url value='/verification?purpose=register'/>"
							class="menu-link">
							<div class="menu-text">회원가입</div>
						</a>
					</div>
					<div class="menu-item ${activePage == 'find-id' ? 'active' : ''}">
						<a href="<c:url value='/verification?purpose=find-id'/>"
							class="menu-link">
							<div class="menu-text">아이디찾기</div>
						</a>
					</div>
					<div
						class="menu-item ${activePage == 'reset-password' ? 'active' : ''}">
						<a href="<c:url value='/verification?purpose=reset-password'/>"
							class="menu-link">
							<div class="menu-text">비밀번호찾기</div>
						</a>
					</div>
				</div>
			</div>

			<!-- 메인 컨텐츠 -->
			<div class="main-content">
				<div class="page-title">${pageTitle}</div>

				<div class="register-container">
					<div class="register-title">본인 확인</div>

					<!-- 안내 박스 -->
					<div class="info-box">
						<div class="info-list">
							<div class="info-item">
								<div class="info-text">
									${pageDescription}<br> 입력하신 정보는 본인확인을 위해 사용되며, 본인확인 용도 외에
									사용되거나 저장되지 않습니다.<br> 개인정보는 본인 동의 없이 공개되지 않으며 개인정보보호정책에 의해
									보호받고 있습니다.
								</div>
							</div>
						</div>
					</div>

					<!-- 인증 방법 선택 -->
					<div class="auth-methods">
						<div class="auth-method">
							<div class="auth-title">이메일 인증</div>
							<div class="auth-description">이메일으로 본인인증을 진행합니다.</div>
							<button class="auth-button email-auth" type="button">
								이메일으로 인증하기</button>
						</div>

						<div class="auth-method">
							<div class="auth-title">휴대폰 인증</div>
							<div class="auth-description">본인명의 휴대폰으로 본인인증을 진행합니다.</div>
							<button class="auth-button phone-auth" type="button">
								휴대폰으로 인증하기</button>
						</div>
					</div>
				</div>
			</div>

			<!-- 브레드크럼 -->
			<div class="breadcrumb">
				<span class="breadcrumb-item">홈</span> <span
					class="breadcrumb-arrow">></span> <span class="breadcrumb-item">회원</span>
				<span class="breadcrumb-arrow">></span> <span
					class="breadcrumb-item current">${pageTitle}</span>
			</div>
		</div>
	</div>

	<!-- 이메일 인증 모달 -->
	<div id="emailModal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h2>이메일 인증</h2>
				<span class="close">&times;</span>
			</div>
			<div class="modal-body">
				<!-- 정보 입력 단계 -->
				<div id="inputStep" class="step-content">
					<p class="modal-description">본인확인을 위해 정보를 입력해주세요.</p>
					<form id="emailAuthForm">
						<div class="form-group">
							<label for="userName">이름</label> <input type="text" id="userName"
								name="userName" required>
						</div>
						<div class="form-group">
							<label for="birthDate">생년월일 (8자리)</label> <input type="text"
								id="birthDate" name="birthDate" placeholder="예: 19900101"
								maxlength="8" required>
						</div>
						<div class="form-group">
							<label for="email">이메일</label> <input type="email" id="email"
								name="email" required>
						</div>
						<button type="submit" class="modal-button" id="sendCodeBtn">
							이메일 인증 요청</button>
					</form>
				</div>

				<!-- 인증번호 입력 단계 -->
				<div id="verificationStep" class="step-content"
					style="display: none;">
					<p class="modal-description">입력하신 이메일로 인증번호를 발송했습니다.</p>
					<div class="email-info">
						<span>발송된 이메일: </span><strong id="sentEmail"></strong>
					</div>

					<div class="form-group">
						<label for="verificationCode">인증번호 (6자리)</label> <input
							type="text" id="verificationCode" maxlength="6"
							placeholder="인증번호 입력">
					</div>

					<div class="timer-section">
						<div class="timer">
							남은시간: <span id="timer">03:00</span>
						</div>
						<button type="button" class="resend-btn" id="resendBtn">재전송</button>
					</div>

					<div class="button-group">
						<button type="button" class="modal-button secondary" id="backBtn">이전</button>
						<button type="button" class="modal-button" id="verifyBtn">인증확인</button>
					</div>
				</div>

				<!-- 인증 성공 단계 -->
				<div id="successStep" class="step-content" style="display: none;">
					<div class="success-icon">✓</div>
					<p class="success-message">이메일 인증이 완료되었습니다!</p>
					<button type="button" class="modal-button" id="continueBtn">
						회원가입 계속하기</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 휴대폰 인증 모달 -->
	<div id="phoneModal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h2>휴대폰 인증</h2>
				<span class="close">&times;</span>
			</div>
			<div class="modal-body">
				<!-- 정보 입력 단계 -->
				<div id="phoneInputStep" class="step-content">
					<p class="modal-description">본인확인을 위해 정보를 입력해주세요.</p>
					<form id="phoneAuthForm">
						<div class="form-group">
							<label for="phoneUserName">이름</label> <input type="text"
								id="phoneUserName" name="phoneUserName" required>
						</div>
						<div class="form-group">
							<label for="phoneBirthDate">생년월일 (8자리)</label> <input type="text"
								id="phoneBirthDate" name="phoneBirthDate"
								placeholder="예: 19900101" maxlength="8" required>
						</div>
						<div class="form-group">
							<label for="phoneNumber">휴대폰 번호</label> <input type="tel"
								id="phoneNumber" name="phoneNumber" placeholder="01012345678"
								maxlength="11" required>
						</div>
						<button type="submit" class="modal-button" id="sendSmsBtn">
							휴대폰 인증 요청</button>
					</form>
				</div>

				<!-- 인증번호 입력 단계 -->
				<div id="phoneVerificationStep" class="step-content"
					style="display: none;">
					<p class="modal-description">입력하신 휴대폰 번호로 인증번호를 발송했습니다.</p>
					<div class="phone-info">
						<span>발송된 번호: </span><strong id="sentPhoneNumber"></strong>
					</div>

					<div class="form-group">
						<label for="phoneVerificationCode">인증번호 (6자리)</label> <input
							type="text" id="phoneVerificationCode" maxlength="6"
							placeholder="인증번호 입력">
					</div>

					<div class="timer-section">
						<div class="timer">
							남은시간: <span id="phoneTimer">03:00</span>
						</div>
						<button type="button" class="resend-btn" id="phoneResendBtn">재전송</button>
					</div>

					<div class="button-group">
						<button type="button" class="modal-button secondary"
							id="phoneBackBtn">이전</button>
						<button type="button" class="modal-button" id="phoneVerifyBtn">인증확인</button>
					</div>
				</div>

				<!-- 인증 성공 단계 -->
				<div id="phoneSuccessStep" class="step-content"
					style="display: none;">
					<div class="success-icon">✓</div>
					<p class="success-message">휴대폰 인증이 완료되었습니다!</p>
					<button type="button" class="modal-button" id="phoneContinueBtn">
						회원가입 계속하기</button>
				</div>
			</div>
		</div>
	</div>

	<!-- JavaScript -->
	<script>
        $(document).ready(function() {
            let timerInterval;
            let phoneTimerInterval;
            let remainingTime = 180; // 3분
            let phoneRemainingTime = 180; // 3분
            let userInfo = {}; // 사용자 입력 정보 저장
            let phoneUserInfo = {}; // 휴대폰 사용자 입력 정보 저장

            // JSP에서 전달받은 purpose 값
            const PURPOSE = '${purpose}' || 'register';
            
            console.log('현재 purpose:', PURPOSE);

            // 이메일 인증 버튼 클릭
            $('.email-auth').on('click', function() {
                $('#emailModal').show();
                resetEmailModal();
            });
            
            // 휴대폰 인증 버튼 클릭
            $('.phone-auth').on('click', function() {
                $('#phoneModal').show();
                resetPhoneModal();
            });

            // 모달 닫기
            $('.close, .modal').on('click', function(e) {
                if (e.target === this || $(e.target).hasClass('close')) {
                    closeEmailModal();
                    closePhoneModal();
                }
            });

            // ===== 이메일 인증 관련 =====
            
            // 이메일 인증 요청
            $('#emailAuthForm').on('submit', function(e) {
                e.preventDefault();
                
                const userName = $('#userName').val().trim();
                const birthDate = $('#birthDate').val().trim();
                const email = $('#email').val().trim();

                // 유효성 검사
                if (!validateEmailForm(userName, birthDate, email)) {
                    return;
                }

                // 사용자 정보 저장
                userInfo = { userName, birthDate, email };

                // 이메일 인증번호 발송
                sendVerificationCode(email);
            });

            // 인증번호 확인
            $('#verifyBtn').on('click', function() {
                const code = $('#verificationCode').val().trim();
                if (!code) {
                    alert('인증번호를 입력해주세요.');
                    return;
                }
                
                verifyCode(userInfo.email, code);
            });

            // 재전송 버튼
            $('#resendBtn').on('click', function() {
                resendVerificationCode(userInfo.email);
            });

            // 이전 버튼
            $('#backBtn').on('click', function() {
                showEmailStep('inputStep');
                clearEmailTimer();
            });

            // 계속하기 버튼 - purpose별 분기 처리
            $('#continueBtn').on('click', function() {
                console.log('이메일 인증 완료 - purpose:', PURPOSE);
                
                if (PURPOSE === 'register') {
                    // 기존 회원가입 로직
                    localStorage.setItem('registrationData', JSON.stringify(userInfo));
                    window.location.href = '<c:url value="/register/info"/>';
                } else {
                    // 아이디 찾기 또는 비밀번호 재설정
                    const userData = {
                        purpose: PURPOSE,
                        authType: 'email',
                        userName: userInfo.userName,
                        birthDate: userInfo.birthDate,
                        email: userInfo.email
                    };
                    
                    handleAuthComplete(userData);
                }
            });

            // ===== 휴대폰 인증 관련 =====
            
            // 휴대폰 인증 요청
            $('#phoneAuthForm').on('submit', function(e) {
                e.preventDefault();
                
                const phoneUserName = $('#phoneUserName').val().trim();
                const phoneBirthDate = $('#phoneBirthDate').val().trim();
                const phoneNumber = $('#phoneNumber').val().trim();

                // 유효성 검사
                if (!validatePhoneForm(phoneUserName, phoneBirthDate, phoneNumber)) {
                    return;
                }

                // 사용자 정보 저장
                phoneUserInfo = { userName: phoneUserName, birthDate: phoneBirthDate, phoneNumber };

                // 휴대폰 인증번호 발송
                sendSmsVerificationCode(phoneNumber);
            });

            // 휴대폰 인증번호 확인
            $('#phoneVerifyBtn').on('click', function() {
                const code = $('#phoneVerificationCode').val().trim();
                if (!code) {
                    alert('인증번호를 입력해주세요.');
                    return;
                }
                
                verifySmsCode(phoneUserInfo.phoneNumber, code);
            });

            // 휴대폰 재전송 버튼
            $('#phoneResendBtn').on('click', function() {
                resendSmsVerificationCode(phoneUserInfo.phoneNumber);
            });

            // 휴대폰 이전 버튼
            $('#phoneBackBtn').on('click', function() {
                showPhoneStep('phoneInputStep');
                clearPhoneTimer();
            });

            // 휴대폰 계속하기 버튼 - purpose별 분기 처리
            $('#phoneContinueBtn').on('click', function() {
                console.log('휴대폰 인증 완료 - purpose:', PURPOSE);
                
                if (PURPOSE === 'register') {
                    // 기존 회원가입 로직
                    localStorage.setItem('registrationData', JSON.stringify(phoneUserInfo));
                    window.location.href = '<c:url value="/register/info"/>';
                } else {
                    // 아이디 찾기 또는 비밀번호 재설정
                    const userData = {
                        purpose: PURPOSE,
                        authType: 'phone',
                        userName: phoneUserInfo.userName,
                        birthDate: phoneUserInfo.birthDate,
                        phoneNumber: phoneUserInfo.phoneNumber
                    };
                    
                    handleAuthComplete(userData);
                }
            });

            // 인증 완료 처리 함수

			function handleAuthComplete(userData) {
			    console.log('인증 완료 처리 시작:', userData);
			    
			    $.ajax({
			        url: '<c:url value="/api/auth-complete"/>',
			        method: 'POST',
			        contentType: 'application/json',
			        data: JSON.stringify(userData),
			        success: function(response) {
			            console.log('API 응답:', response);
			            
			            if (response.success) {
			                const result = response.data;
			                console.log('응답 데이터:', result);
			                
			                switch (userData.purpose) {
			                    case 'find-id':
			                        console.log('아이디 찾기 결과 페이지로 이동');
			                        console.log('foundId:', result.foundId);
			                        console.log('authType:', result.authType);
			                        console.log('authKey:', result.authKey);
			                        
			                        // URL 생성 시 undefined 체크 (encodeURIComponent 제거)
			                        const authType = result.authType || '';
			                        const authKey = result.authKey || '';
			                        const foundId = result.foundId || '';
			                        
			                        console.log('최종 URL 파라미터:', { authType, authKey, foundId });
			                        
			                        // 아이디 찾기 결과 페이지로 이동
			                        const findIdUrl = '<c:url value="/find-id/result"/>' + 
			                                        '?authType=' + authType + 
			                                        '&authKey=' + authKey + 
			                                        '&foundId=' + foundId;
			                        
			                        console.log('이동할 URL:', findIdUrl);
			                        window.location.href = findIdUrl;
			                        break;
			                        
			                    case 'reset-password':
			                        console.log('비밀번호 재설정 폼 페이지로 이동');
			                        console.log('userId:', result.userId);
			                        console.log('authType:', result.authType);
			                        console.log('authKey:', result.authKey);
			                        
			                        // URL 생성 시 undefined 체크 (encodeURIComponent 제거)
			                        const pwAuthType = result.authType || '';
			                        const pwAuthKey = result.authKey || '';
			                        const userId = result.userId || '';
			                        
			                        console.log('최종 URL 파라미터:', { pwAuthType, pwAuthKey, userId });
			                        
			                        // 비밀번호 재설정 폼 페이지로 이동
			                        const resetPasswordUrl = '<c:url value="/reset-password/form"/>' + 
			                                               '?authType=' + pwAuthType + 
			                                               '&authKey=' + pwAuthKey + 
			                                               '&userId=' + userId;
			                        
			                        console.log('이동할 URL:', resetPasswordUrl);
			                        window.location.href = resetPasswordUrl;
			                        break;
			                        
			                    default:
			                        console.log('알 수 없는 purpose:', userData.purpose);
			                        alert('처리 중 오류가 발생했습니다.');
			                        break;
			                }
			            } else {
			                alert(response.message || '처리 중 오류가 발생했습니다.');
			            }
			        },
			        error: function(xhr, status, error) {
			            console.error('API 호출 오류:', xhr, status, error);
			            console.error('응답 텍스트:', xhr.responseText);
			            alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
			        }
			    });
			}

            // 성공 메시지 동적 변경
            function updateSuccessMessage() {
                let message = '';
                let buttonText = '';
                
                switch (PURPOSE) {
                    case 'find-id':
                        message = '인증이 완료되었습니다!';
                        buttonText = '아이디 찾기 계속하기';
                        break;
                    case 'reset-password':
                        message = '인증이 완료되었습니다!';
                        buttonText = '비밀번호 찾기 계속하기';
                        break;
                    case 'register':
                    default:
                        message = '인증이 완료되었습니다!';
                        buttonText = '회원가입 계속하기';
                        break;
                }
                
                // 이메일 성공 단계 메시지 업데이트
                $('#successStep .success-message').text('이메일 ' + message);
                $('#continueBtn').text(buttonText);
                
                // 휴대폰 성공 단계 메시지 업데이트
                $('#phoneSuccessStep .success-message').text('휴대폰 ' + message);
                $('#phoneContinueBtn').text(buttonText);
            }

            // 페이지 로드 시 성공 메시지 업데이트
            updateSuccessMessage();

            // 생년월일 숫자만 입력
            $('#birthDate, #phoneBirthDate').on('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
            });

            // 인증번호 숫자만 입력
            $('#verificationCode, #phoneVerificationCode').on('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
            });

            // 휴대폰 번호 숫자만 입력
            $('#phoneNumber').on('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
            });

            // ===== 이메일 유효성 검사 함수 =====
            function validateEmailForm(userName, birthDate, email) {
                if (!userName) {
                    alert('이름을 입력해주세요.');
                    $('#userName').focus();
                    return false;
                }

                if (!birthDate || birthDate.length !== 8) {
                    alert('생년월일 8자리를 정확히 입력해주세요.');
                    $('#birthDate').focus();
                    return false;
                }

                if (!isValidBirthDate(birthDate)) {
                    alert('올바른 생년월일을 입력해주세요.');
                    $('#birthDate').focus();
                    return false;
                }

                if (!email || !isValidEmail(email)) {
                    alert('올바른 이메일 주소를 입력해주세요.');
                    $('#email').focus();
                    return false;
                }

                return true;
            }

            // ===== 휴대폰 유효성 검사 함수 =====
            function validatePhoneForm(userName, birthDate, phoneNumber) {
                if (!userName) {
                    alert('이름을 입력해주세요.');
                    $('#phoneUserName').focus();
                    return false;
                }

                if (!birthDate || birthDate.length !== 8) {
                    alert('생년월일 8자리를 정확히 입력해주세요.');
                    $('#phoneBirthDate').focus();
                    return false;
                }

                if (!isValidBirthDate(birthDate)) {
                    alert('올바른 생년월일을 입력해주세요.');
                    $('#phoneBirthDate').focus();
                    return false;
                }

                if (!phoneNumber || !isValidPhoneNumber(phoneNumber)) {
                    alert('올바른 휴대폰 번호를 입력해주세요.');
                    $('#phoneNumber').focus();
                    return false;
                }

                return true;
            }

            // 생년월일 유효성 검사
            function isValidBirthDate(birthDate) {
                if (birthDate.length !== 8) return false;
                
                const year = parseInt(birthDate.substring(0, 4));
                const month = parseInt(birthDate.substring(4, 6));
                const day = parseInt(birthDate.substring(6, 8));
                
                const currentYear = new Date().getFullYear();
                
                if (year < 1900 || year > currentYear) return false;
                if (month < 1 || month > 12) return false;
                if (day < 1 || day > 31) return false;
                
                // 간단한 날짜 유효성 검사
                const date = new Date(year, month - 1, day);
                return date.getFullYear() === year && 
                       date.getMonth() === month - 1 && 
                       date.getDate() === day;
            }

            // 이메일 유효성 검사
            function isValidEmail(email) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(email);
            }

            // 휴대폰 번호 유효성 검사
            function isValidPhoneNumber(phoneNumber) {
                const phoneRegex = /^01[0-9]\d{7,8}$/;
                return phoneRegex.test(phoneNumber);
            }

            // ===== 이메일 API 함수들 =====
            
            // 이메일 인증번호 발송
            function sendVerificationCode(email) {
                $('#sendCodeBtn').prop('disabled', true).text('발송 중...');
                
                $.ajax({
                    url: '<c:url value="/api/email/send"/>',
                    method: 'POST',
                    data: { email: email },
                    success: function(response) {
                        if (response.success) {
                            $('#sentEmail').text(email);
                            showEmailStep('verificationStep');
                            startEmailTimer();
                        } else {
                            alert(response.message || '인증번호 발송에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#sendCodeBtn').prop('disabled', false).text('이메일 인증 요청');
                    }
                });
            }

            // 인증번호 확인
            function verifyCode(email, code) {
                $('#verifyBtn').prop('disabled', true).text('확인 중...');
                
                $.ajax({
                    url: '<c:url value="/api/email/verify"/>',
                    method: 'POST',
                    data: { 
                        email: email,
                        code: code 
                    },
                    success: function(response) {
                        if (response.success) {
                            clearEmailTimer();
                            showEmailStep('successStep');
                        } else {
                            alert(response.message || '인증번호가 일치하지 않습니다.');
                            $('#verificationCode').val('').focus();
                        }
                    },
                    error: function() {
                        alert('인증번호 확인에 실패했습니다. 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#verifyBtn').prop('disabled', false).text('인증확인');
                    }
                });
            }

            // 인증번호 재발송
            function resendVerificationCode(email) {
                $('#resendBtn').prop('disabled', true).text('재발송 중...');
                
                $.ajax({
                    url: '<c:url value="/api/email/resend"/>',
                    method: 'POST',
                    data: { email: email },
                    success: function(response) {
                        if (response.success) {
                            alert('인증번호가 재발송되었습니다.');
                            remainingTime = 180;
                            startEmailTimer();
                        } else {
                            alert(response.message || '재발송에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('재발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#resendBtn').prop('disabled', false).text('재전송');
                    }
                });
            }

            // ===== 휴대폰 API 함수들 =====
            
            // 휴대폰 인증번호 발송
            function sendSmsVerificationCode(phoneNumber) {
                $('#sendSmsBtn').prop('disabled', true).text('발송 중...');
                
                $.ajax({
                    url: '<c:url value="/api/sms/send"/>',
                    method: 'POST',
                    data: { phoneNumber: phoneNumber },
                    success: function(response) {
                        if (response.success) {
                            $('#sentPhoneNumber').text(formatPhoneNumber(phoneNumber));
                            showPhoneStep('phoneVerificationStep');
                            startPhoneTimer();
                        } else {
                            alert(response.message || '인증번호 발송에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('인증번호 발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#sendSmsBtn').prop('disabled', false).text('휴대폰 인증 요청');
                    }
                });
            }

            // 휴대폰 인증번호 확인
            function verifySmsCode(phoneNumber, code) {
                $('#phoneVerifyBtn').prop('disabled', true).text('확인 중...');
                
                $.ajax({
                    url: '<c:url value="/api/sms/verify"/>',
                    method: 'POST',
                    data: { 
                        phoneNumber: phoneNumber,
                        verificationCode: code 
                    },
                    success: function(response) {
                        if (response.success) {
                            clearPhoneTimer();
                            showPhoneStep('phoneSuccessStep');
                        } else {
                            alert(response.message || '인증번호가 일치하지 않습니다.');
                            $('#phoneVerificationCode').val('').focus();
                        }
                    },
                    error: function() {
                        alert('인증번호 확인에 실패했습니다. 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#phoneVerifyBtn').prop('disabled', false).text('인증확인');
                    }
                });
            }

            // 휴대폰 인증번호 재발송
            function resendSmsVerificationCode(phoneNumber) {
                $('#phoneResendBtn').prop('disabled', true).text('재발송 중...');
                
                $.ajax({
                    url: '<c:url value="/api/sms/resend"/>',
                    method: 'POST',
                    data: { phoneNumber: phoneNumber },
                    success: function(response) {
                        if (response.success) {
                            alert('인증번호가 재발송되었습니다.');
                            phoneRemainingTime = 180;
                            startPhoneTimer();
                        } else {
                            alert(response.message || '재발송에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('재발송에 실패했습니다. 잠시 후 다시 시도해주세요.');
                    },
                    complete: function() {
                        $('#phoneResendBtn').prop('disabled', false).text('재전송');
                    }
                });
            }

            // ===== 이메일 타이머 함수들 =====
            
            // 타이머 시작
            function startEmailTimer() {
                clearEmailTimer();
                timerInterval = setInterval(function() {
                    remainingTime--;
                    updateEmailTimerDisplay();
                    
                    if (remainingTime <= 0) {
                        clearEmailTimer();
                        alert('인증시간이 만료되었습니다. 재발송을 클릭해주세요.');
                        $('#verifyBtn').prop('disabled', true);
                    }
                }, 1000);
            }

            // 타이머 표시 업데이트
            function updateEmailTimerDisplay() {
                const minutes = Math.floor(remainingTime / 60);
                const seconds = remainingTime % 60;
                $('#timer').text(
                    String(minutes).padStart(2, '0') + ':' + 
                    String(seconds).padStart(2, '0')
                );
            }

            // 타이머 정리
            function clearEmailTimer() {
                if (timerInterval) {
                    clearInterval(timerInterval);
                    timerInterval = null;
                }
            }

            // ===== 휴대폰 타이머 함수들 =====
            
            // 휴대폰 타이머 시작
            function startPhoneTimer() {
                clearPhoneTimer();
                phoneTimerInterval = setInterval(function() {
                    phoneRemainingTime--;
                    updatePhoneTimerDisplay();
                    
                    if (phoneRemainingTime <= 0) {
                        clearPhoneTimer();
                        alert('인증시간이 만료되었습니다. 재발송을 클릭해주세요.');
                        $('#phoneVerifyBtn').prop('disabled', true);
                    }
                }, 1000);
            }

            // 휴대폰 타이머 표시 업데이트
            function updatePhoneTimerDisplay() {
                const minutes = Math.floor(phoneRemainingTime / 60);
                const seconds = phoneRemainingTime % 60;
                $('#phoneTimer').text(
                    String(minutes).padStart(2, '0') + ':' + 
                    String(seconds).padStart(2, '0')
                );
            }

            // 휴대폰 타이머 정리
            function clearPhoneTimer() {
                if (phoneTimerInterval) {
                    clearInterval(phoneTimerInterval);
                    phoneTimerInterval = null;
                }
            }

            // ===== 단계 표시 함수들 =====
            
            // 이메일 단계 표시
            function showEmailStep(stepId) {
                $('#emailModal .step-content').hide();
                $('#' + stepId).show();
            }

            // 휴대폰 단계 표시
            function showPhoneStep(stepId) {
                $('#phoneModal .step-content').hide();
                $('#' + stepId).show();
            }

            // ===== 모달 초기화 함수들 =====
            
            // 이메일 모달 초기화
            function resetEmailModal() {
                showEmailStep('inputStep');
                $('#emailAuthForm')[0].reset();
                $('#verificationCode').val('');
                remainingTime = 180;
                clearEmailTimer();
                userInfo = {};
            }

            // 휴대폰 모달 초기화
            function resetPhoneModal() {
                showPhoneStep('phoneInputStep');
                $('#phoneAuthForm')[0].reset();
                $('#phoneVerificationCode').val('');
                phoneRemainingTime = 180;
                clearPhoneTimer();
                phoneUserInfo = {};
            }

            // ===== 모달 닫기 함수들 =====
            
            // 이메일 모달 닫기
            function closeEmailModal() {
                $('#emailModal').hide();
                clearEmailTimer();
                resetEmailModal();
            }

            // 휴대폰 모달 닫기
            function closePhoneModal() {
                $('#phoneModal').hide();
                clearPhoneTimer();
                resetPhoneModal();
            }

            // ===== 유틸리티 함수들 =====
            
            // 휴대폰 번호 포맷팅 (01012345678 → 010-1234-5678)
            function formatPhoneNumber(phoneNumber) {
                if (phoneNumber.length === 11) {
                    return phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
                } else if (phoneNumber.length === 10) {
                    return phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
                }
                return phoneNumber;
            }
        });
	</script>
</body>
</html>