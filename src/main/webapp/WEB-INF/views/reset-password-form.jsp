<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 비밀번호찾기</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/verification/style.css'/>">
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
                    <div class="menu-item">
                        <a href="<c:url value='/verification?purpose=register'/>" class="menu-link">
                            <div class="menu-text">회원가입</div>
                        </a>
                    </div>
                    <div class="menu-item">
                        <a href="<c:url value='/verification?purpose=find-id'/>" class="menu-link">
                            <div class="menu-text">아이디찾기</div>
                        </a>
                    </div>
                    <div class="menu-item active">
                        <a href="<c:url value='/verification?purpose=reset-password'/>" class="menu-link">
                            <div class="menu-text">비밀번호찾기</div>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content">
                <div class="page-title">비밀번호찾기</div>
                
                <div class="register-container">
                    <div class="password-reset-container">
                        <div class="reset-title">새 비밀번호 설정</div>
                        <div class="reset-description">
                            새로 사용할 비밀번호를 입력해주세요.
                        </div>
                        
                        <!-- 비밀번호 재설정 폼 -->
                        <form id="resetPasswordForm">
                            <div class="form-group">
                                <label for="newPassword">새 비밀번호</label>
                                <input type="password" id="newPassword" name="newPassword" 
                                       placeholder="영문 대소문자, 숫자, 특수문자 포함 8-20자" required>
                                <div class="validation-msg" id="passwordMsg"></div>
                                
                                <!-- 비밀번호 강도 표시 -->
                                <div class="password-strength" id="passwordStrength">
                                    <div class="strength-bar">
                                        <div class="strength-fill"></div>
                                    </div>
                                    <div class="strength-text">비밀번호 강도</div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">새 비밀번호 확인</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" 
                                       placeholder="비밀번호를 다시 입력해주세요" required>
                                <div class="validation-msg" id="confirmPasswordMsg"></div>
                            </div>

                            <!-- 숨겨진 필드들 -->
                            <input type="hidden" id="userId" value="${userId}">
                            <input type="hidden" id="authType" value="${authType}">
                            <input type="hidden" id="authKey" value="${authKey}">

                            <div class="button-group">
                                <button type="button" class="modal-button secondary" onclick="history.back()">
                                    이전
                                </button>
                                <button type="submit" class="modal-button" id="resetBtn">
                                    비밀번호찾기
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- 브레드크럼 -->
            <div class="breadcrumb">
                <span class="breadcrumb-item">홈</span>
                <span class="breadcrumb-arrow">></span>
                <span class="breadcrumb-item">회원</span>
                <span class="breadcrumb-arrow">></span>
                <span class="breadcrumb-item current">비밀번호찾기</span>
            </div>
        </div>
    </div>

    <style>
        .password-reset-container {
            max-width: 500px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .reset-title {
            font-size: 24px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 15px;
            color: #333;
        }
        
        .reset-description {
            text-align: center;
            color: #666;
            margin-bottom: 40px;
            font-size: 16px;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #333;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 16px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.2s;
            box-sizing: border-box;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #2E7D32;
        }
        
        .validation-msg {
            margin-top: 8px;
            font-size: 14px;
            min-height: 20px;
        }
        
        .validation-msg.success {
            color: #2E7D32;
        }
        
        .validation-msg.error {
            color: #d32f2f;
        }
        
        .password-strength {
            margin-top: 10px;
        }
        
        .strength-bar {
            width: 100%;
            height: 4px;
            background-color: #e0e0e0;
            border-radius: 2px;
            overflow: hidden;
            margin-bottom: 5px;
        }
        
        .strength-fill {
            height: 100%;
            width: 0%;
            transition: all 0.3s ease;
            border-radius: 2px;
        }
        
        .strength-text {
            font-size: 12px;
            color: #666;
        }
        
        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 40px;
        }
        
        .modal-button {
            flex: 1;
            padding: 14px 20px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.2s;
            font-weight: 500;
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
        
        .modal-button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
    </style>

    <script>
        $(document).ready(function() {
            let isPasswordValid = false;
            let isConfirmValid = false;

            // 비밀번호 입력 시 실시간 검증
            $('#newPassword').on('input', function() {
                const password = $(this).val();
                isPasswordValid = validatePassword(password);
                checkPasswordStrength(password);
                
                // 비밀번호 확인도 다시 검증
                const confirmPassword = $('#confirmPassword').val();
                if (confirmPassword) {
                    isConfirmValid = validatePasswordConfirm(password, confirmPassword);
                }
                
                updateSubmitButton();
            });

            // 비밀번호 확인 입력 시 검증
            $('#confirmPassword').on('input', function() {
                const password = $('#newPassword').val();
                const confirmPassword = $(this).val();
                isConfirmValid = validatePasswordConfirm(password, confirmPassword);
                updateSubmitButton();
            });

            // 폼 제출
            $('#resetPasswordForm').on('submit', function(e) {
                e.preventDefault();
                
                if (isPasswordValid && isConfirmValid) {
                    submitPasswordReset();
                } else {
                    alert('입력한 정보를 다시 확인해주세요.');
                }
            });

            // 비밀번호 유효성 검사
            function validatePassword(password) {
                const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
                
                if (!password) {
                    clearValidationMessage('passwordMsg');
                    return false;
                }
                
                if (!regex.test(password)) {
                    showValidationMessage('passwordMsg', '영문 대소문자, 숫자, 특수문자 포함 8-20자로 입력해주세요.', 'error');
                    return false;
                }
                
                showValidationMessage('passwordMsg', '사용 가능한 비밀번호입니다.', 'success');
                return true;
            }

            // 비밀번호 확인 검증
            function validatePasswordConfirm(password, confirmPassword) {
                if (!confirmPassword) {
                    clearValidationMessage('confirmPasswordMsg');
                    return false;
                }
                
                if (password !== confirmPassword) {
                    showValidationMessage('confirmPasswordMsg', '비밀번호가 일치하지 않습니다.', 'error');
                    return false;
                }
                
                showValidationMessage('confirmPasswordMsg', '비밀번호가 일치합니다.', 'success');
                return true;
            }

            // 비밀번호 강도 체크
            function checkPasswordStrength(password) {
                const strengthBar = $('.strength-fill');
                const strengthText = $('.strength-text');
                
                let score = 0;
                let status = '';
                let color = '';
                
                if (password.length >= 8) score++;
                if (/[a-z]/.test(password)) score++;
                if (/[A-Z]/.test(password)) score++;
                if (/\d/.test(password)) score++;
                if (/[@$!%*?&]/.test(password)) score++;
                
                switch (score) {
                    case 0:
                    case 1:
                        status = '매우 약함';
                        color = '#ff4444';
                        break;
                    case 2:
                        status = '약함';
                        color = '#ff8800';
                        break;
                    case 3:
                        status = '보통';
                        color = '#ffaa00';
                        break;
                    case 4:
                        status = '강함';
                        color = '#88cc00';
                        break;
                    case 5:
                        status = '매우 강함';
                        color = '#00cc44';
                        break;
                }
                
                const percentage = (score / 5) * 100;
                strengthBar.css({
                    'width': percentage + '%',
                    'background-color': color
                });
                
                strengthText.text('비밀번호 강도: ' + status);
            }

            // 제출 버튼 활성화/비활성화
            function updateSubmitButton() {
                const submitBtn = $('#resetBtn');
                if (isPasswordValid && isConfirmValid) {
                    submitBtn.prop('disabled', false);
                } else {
                    submitBtn.prop('disabled', true);
                }
            }

            // 비밀번호 재설정 제출
            function submitPasswordReset() {
                const submitBtn = $('#resetBtn');
                submitBtn.prop('disabled', true).text('처리 중...');
                
                const formData = {
                    userId: $('#userId').val(),
                    newPassword: $('#newPassword').val(),
                    confirmPassword: $('#confirmPassword').val()
                };
                
                $.ajax({
                    url: '<c:url value="/api/reset-password"/>',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function(response) {
                        if (response.success) {
                            alert('비밀번호가 성공적으로 재설정되었습니다.');
                            window.location.href = '<c:url value="/login?message=password-reset-success"/>';
                        } else {
                            alert(response.message || '비밀번호 재설정에 실패했습니다.');
                        }
                    },
                    error: function() {
                        alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
                    },
                    complete: function() {
                        submitBtn.prop('disabled', false).text('비밀번호 재설정');
                    }
                });
            }

            // 유효성 메시지 표시
            function showValidationMessage(elementId, message, type) {
                $('#' + elementId).text(message).removeClass().addClass('validation-msg ' + type);
            }

            // 유효성 메시지 초기화
            function clearValidationMessage(elementId) {
                $('#' + elementId).text('').removeClass();
            }

            // 초기 버튼 상태 설정
            updateSubmitButton();
        });
    </script>
</body>
</html>