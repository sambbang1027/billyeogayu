<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 회원정보 입력</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/register-info/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- 다음 주소 API -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
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
                    <div class="menu-item active">
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
                            <div class="menu-text">비밀번호 재설정</div>
                        </a>
                    </div>
                </div>
            </div>

            <!-- 메인 컨텐츠 -->
            <div class="main-content">
                <div class="page-title">회원가입</div>
                
                <div class="register-container">
                    <div class="register-title">회원정보 입력</div>
                    
                    <!-- 진행 단계 표시 -->
                    <div class="progress-steps">
                        <div class="step completed">
                            <div class="step-number">1</div>
                            <div class="step-text">본인 확인</div>
                        </div>
                        <div class="step-line completed"></div>
                        <div class="step active">
                            <div class="step-number">2</div>
                            <div class="step-text">정보 입력</div>
                        </div>
                        <div class="step-line"></div>
                        <div class="step">
                            <div class="step-number">3</div>
                            <div class="step-text">가입 완료</div>
                        </div>
                    </div>

                    <!-- 회원가입 폼 -->
                    <form id="registerForm" class="register-form">
                        <!-- 인증된 정보 섹션 -->
                        <div class="form-section">
                            <h3 class="section-title">인증된 정보</h3>
                            <div class="verified-info">
                                <div class="info-row">
                                    <label>이름</label>
                                    <input type="text" id="verifiedName" readonly>
                                </div>
                                <div class="info-row">
                                    <label>생년월일</label>
                                    <input type="text" id="verifiedBirth" readonly>
                                </div>
                                <div class="info-row" id="verifiedEmailRow" style="display: none;">
                                    <label>이메일</label>
                                    <input type="text" id="verifiedEmail" readonly>
                                </div>
                                <div class="info-row" id="verifiedPhoneRow" style="display: none;">
                                    <label>휴대폰 번호</label>
                                    <input type="text" id="verifiedPhone" readonly>
                                </div>
                            </div>
                        </div>

                        <!-- 추가 정보 입력 섹션 -->
                        <div class="form-section">
                            <h3 class="section-title">추가 정보 입력</h3>
                            
                            <!-- 아이디 -->
                            <div class="form-group">
                                <label for="loginId" class="required">아이디</label>
                                <div class="input-with-button">
                                    <input type="text" id="loginId" name="loginId" placeholder="영문, 숫자 조합 6-20자" required>
                                    <button type="button" class="check-btn" id="checkIdBtn">중복확인</button>
                                </div>
                                <div class="validation-msg" id="loginIdMsg"></div>
                            </div>

                            <!-- 비밀번호 -->
                            <div class="form-group">
                                <label for="password" class="required">비밀번호</label>
                                <input type="password" id="password" name="password" placeholder="영문 대소문자, 숫자, 특수문자 포함 8-20자" required>
                                <div class="validation-msg" id="passwordMsg"></div>
                                <div class="password-strength" id="passwordStrength">
                                    <div class="strength-bar">
                                        <div class="strength-fill"></div>
                                    </div>
                                    <div class="strength-text">비밀번호 강도</div>
                                </div>
                            </div>

                            <!-- 비밀번호 확인 -->
                            <div class="form-group">
                                <label for="passwordConfirm" class="required">비밀번호 확인</label>
                                <input type="password" id="passwordConfirm" name="passwordConfirm" placeholder="비밀번호를 다시 입력하세요" required>
                                <div class="validation-msg" id="passwordConfirmMsg"></div>
                            </div>

                            <!-- 이메일 (휴대폰 인증인 경우) -->
                            <div class="form-group" id="emailGroup" style="display: none;">
                                <label for="email" class="required">이메일</label>
                                <input type="email" id="email" name="email" placeholder="이메일을 입력하세요">
                                <div class="validation-msg" id="emailMsg"></div>
                            </div>

                            <!-- 휴대폰 번호 (이메일 인증인 경우) -->
                            <div class="form-group" id="phoneGroup" style="display: none;">
                                <label for="phoneNumber" class="required">휴대폰 번호</label>
                                <input type="tel" id="phoneNumber" name="phoneNumber" placeholder="01012345678" maxlength="11">
                                <div class="validation-msg" id="phoneMsg"></div>
                            </div>

                            <!-- 주소 -->
                            <div class="form-group">
                                <label for="address" class="required">주소</label>
                                <div class="address-container">
                                    <div class="input-with-button">
                                        <input type="text" id="postcode" name="postcode" placeholder="우편번호" readonly required>
                                        <button type="button" class="check-btn" id="addressSearchBtn">주소검색</button>
                                    </div>
                                    <input type="text" id="roadAddress" name="roadAddress" placeholder="도로명 주소" readonly required>
                                    <input type="text" id="detailAddress" name="detailAddress" placeholder="상세 주소 (선택)" class="detail-address">
                                </div>
                                <div class="validation-msg" id="addressMsg"></div>
                            </div>
                        </div>

                        <!-- 약관 동의 섹션 -->
                        <div class="form-section">
                            <h3 class="section-title">이용약관 동의</h3>
                            
                            <div class="agreement-container">
                                <div class="agreement-all">
                                    <label class="checkbox-label">
                                        <input type="checkbox" id="agreeAll">
                                        
                                        <span class="checkbox-text">전체 동의</span>
                                    </label>
                                </div>

                                <div class="agreement-list">
                                    <div class="agreement-item">
                                        <label class="checkbox-label">
                                            <input type="checkbox" class="required-agree" id="agreeTerms" required>
                                            
                                            <span class="checkbox-text">[필수] 이용약관 동의</span>
                                        </label>
                                        <button type="button" class="view-btn" data-modal="terms">보기</button>
                                    </div>

                                    <div class="agreement-item">
                                        <label class="checkbox-label">
                                            <input type="checkbox" class="required-agree" id="agreePrivacy" required>
                                            <span class="checkbox-text">[필수] 개인정보 수집·이용 동의</span>
                                        </label>
                                        <button type="button" class="view-btn" data-modal="privacy">보기</button>
                                    </div>

                                    <div class="agreement-item">
                                        <label class="checkbox-label">
                                            <input type="checkbox" id="agreeMarketing">
                                            
                                            <span class="checkbox-text">[선택] 마케팅 정보 수신 동의</span>
                                        </label>
                                        <button type="button" class="view-btn" data-modal="marketing">보기</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 가입하기 버튼 -->
                        <div class="form-actions">
                            <button type="button" class="btn-secondary" id="backBtn">이전</button>
                            <button type="submit" class="btn-primary" id="submitBtn">가입하기</button>
                        </div>
                    </form>
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

    <!-- 약관 모달 -->
    <div id="termsModal" class="terms-modal">
        <div class="terms-modal-content">
            <div class="terms-modal-header">
                <h3 id="termsModalTitle">약관</h3>
                <span class="terms-close">&times;</span>
            </div>
            <div class="terms-modal-body" id="termsModalBody">
                <!-- 약관 내용이 여기에 로드됩니다 -->
            </div>
            <div class="terms-modal-footer">
                <button type="button" class="btn-primary" id="agreeModalBtn">동의</button>
                <button type="button" class="btn-secondary" id="closeModalBtn">닫기</button>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        $(document).ready(function() {
            let registrationData = {};
            let isIdChecked = false;
            let currentModal = '';

            // 페이지 로드 시 인증 정보 불러오기
            loadVerificationData();

            // localStorage에서 인증 정보 불러오기
            function loadVerificationData() {
                const data = localStorage.getItem('registrationData');
                if (!data) {
                    alert('인증 정보가 없습니다. 본인 확인부터 다시 진행해주세요.');
                    window.location.href = '<c:url value="/verification"/>';
                    return;
                }

                try {
                    registrationData = JSON.parse(data);
                    
                    // 인증된 정보 표시
                    $('#verifiedName').val(registrationData.userName);
                    $('#verifiedBirth').val(formatBirthDate(registrationData.birthDate));
                    
                    if (registrationData.email) {
                        // 이메일 인증인 경우
                        $('#verifiedEmail').val(registrationData.email);
                        $('#verifiedEmailRow').show();
                        $('#phoneGroup').show(); // 휴대폰 번호 입력 필요
                    } else if (registrationData.phoneNumber) {
                        // 휴대폰 인증인 경우
                        $('#verifiedPhone').val(formatPhoneNumber(registrationData.phoneNumber));
                        $('#verifiedPhoneRow').show();
                        $('#emailGroup').show(); // 이메일 입력 필요
                    }
                } catch (e) {
                    alert('인증 정보 형식이 올바르지 않습니다. 다시 인증해주세요.');
                    window.location.href = '<c:url value="/verification"/>';
                }
            }

            // 아이디 중복 확인
            $('#checkIdBtn').on('click', function() {
                const loginId = $('#loginId').val().trim();
                
                if (!loginId) {
                    showValidationMessage('loginIdMsg', '아이디를 입력해주세요.', 'error');
                    return;
                }

                if (!validateLoginId(loginId)) {
                    return;
                }

                checkIdDuplication(loginId);
            });

            // 비밀번호 강도 체크
            $('#password').on('input', function() {
                const password = $(this).val();
                checkPasswordStrength(password);
                validatePassword(password);
            });

            // 비밀번호 확인 체크
            $('#passwordConfirm').on('input', function() {
                const password = $('#password').val();
                const passwordConfirm = $(this).val();
                validatePasswordConfirm(password, passwordConfirm);
            });

            // 주소 검색
            $('#addressSearchBtn').on('click', function() {
                new daum.Postcode({
                    oncomplete: function(data) {
                        $('#postcode').val(data.zonecode);
                        $('#roadAddress').val(data.roadAddress);
                        $('#detailAddress').focus();
                        clearValidationMessage('addressMsg');
                    }
                }).open();
            });

            // 전체 동의 체크박스
            $('#agreeAll').on('change', function() {
                const isChecked = $(this).is(':checked');
                $('.agreement-list input[type="checkbox"]').prop('checked', isChecked);
            });

            // 개별 동의 체크박스
            $('.agreement-list input[type="checkbox"]').on('change', function() {
                const totalCount = $('.agreement-list input[type="checkbox"]').length;
                const checkedCount = $('.agreement-list input[type="checkbox"]:checked').length;
                $('#agreeAll').prop('checked', totalCount === checkedCount);
            });

            // 약관 보기 버튼
            $('.view-btn').on('click', function() {
                const modalType = $(this).data('modal');
                showTermsModal(modalType);
            });

            // 약관 모달 닫기
            $('.terms-close, #closeModalBtn').on('click', function() {
                closeTermsModal();
            });

            // 약관 동의 버튼
            $('#agreeModalBtn').on('click', function() {
                if (currentModal) {
                    $('#agree' + currentModal.charAt(0).toUpperCase() + currentModal.slice(1)).prop('checked', true);
                    
                    // 전체 동의 상태 업데이트
                    const totalCount = $('.agreement-list input[type="checkbox"]').length;
                    const checkedCount = $('.agreement-list input[type="checkbox"]:checked').length;
                    $('#agreeAll').prop('checked', totalCount === checkedCount);
                }
                closeTermsModal();
            });

            // 이전 버튼
            $('#backBtn').on('click', function() {
                if (confirm('이전 단계로 돌아가시겠습니까? 입력한 정보는 저장되지 않습니다.')) {
                    window.location.href = '<c:url value="/verificarion"/>';
                }
            });

            // 폼 제출
            $('#registerForm').on('submit', function(e) {
			    e.preventDefault();
			    console.log('폼 제출 이벤트 발생');
			    
			    if (validateForm()) {
			        console.log('유효성 검사 통과');
			        submitRegistration();
			    } else {
			        console.log('유효성 검사 실패');
			    }
			});


            // 실시간 유효성 검사
            $('#loginId').on('input', function() {
                isIdChecked = false;
                const loginId = $(this).val().trim();
                if (loginId) {
                    validateLoginId(loginId);
                }
            });

            $('#email').on('input', function() {
                const email = $(this).val().trim();
                if (email) {
                    validateEmail(email);
                }
            });

            $('#phoneNumber').on('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                const phoneNumber = $(this).val().trim();
                if (phoneNumber) {
                    validatePhoneNumber(phoneNumber);
                }
            });

            // 유효성 검사 함수들
            function validateLoginId(loginId) {
                const regex = /^[a-zA-Z0-9]{6,20}$/;
                
                if (!regex.test(loginId)) {
                    showValidationMessage('loginIdMsg', '영문 대소문자, 숫자 조합 6-20자로 입력해주세요.', 'error');
                    return false;
                }
                
                clearValidationMessage('loginIdMsg');
                return true;
            }

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
                
                clearValidationMessage('passwordMsg');
                return true;
            }

            function validatePasswordConfirm(password, passwordConfirm) {
                if (!passwordConfirm) {
                    clearValidationMessage('passwordConfirmMsg');
                    return false;
                }
                
                if (password !== passwordConfirm) {
                    showValidationMessage('passwordConfirmMsg', '비밀번호가 일치하지 않습니다.', 'error');
                    return false;
                }
                
                showValidationMessage('passwordConfirmMsg', '비밀번호가 일치합니다.', 'success');
                return true;
            }

            function validateEmail(email) {
                const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                
                if (!regex.test(email)) {
                    showValidationMessage('emailMsg', '올바른 이메일 형식이 아닙니다.', 'error');
                    return false;
                }
                
                clearValidationMessage('emailMsg');
                return true;
            }

            function validatePhoneNumber(phoneNumber) {
                const regex = /^01[0-9]\d{7,8}$/;
                
                if (!regex.test(phoneNumber)) {
                    showValidationMessage('phoneMsg', '올바른 휴대폰 번호 형식이 아닙니다.', 'error');
                    return false;
                }
                
                clearValidationMessage('phoneMsg');
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

            // 아이디 중복 확인 API
            function checkIdDuplication(loginId) {
                $('#checkIdBtn').prop('disabled', true).text('확인 중...');
                
                $.ajax({
                    url: '<c:url value="/api/check-loginId"/>',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({ loginId: loginId }),
                    success: function(response) {
                        if (response.exists) {
                            showValidationMessage('loginIdMsg', '이미 사용 중인 아이디입니다.', 'error');
                            isIdChecked = false;
                        } else {
                            showValidationMessage('loginIdMsg', '사용 가능한 아이디입니다.', 'success');
                            isIdChecked = true;
                        }
                    },
                    error: function() {
                        showValidationMessage('loginIdMsg', '아이디 중복 확인에 실패했습니다.', 'error');
                        isIdChecked = false;
                    },
                    complete: function() {
                        $('#checkIdBtn').prop('disabled', false).text('중복확인');
                    }
                });
            }

            // 폼 전체 유효성 검사
			function validateForm() {
			    console.log('=== 유효성 검사 시작 ===');
			    let isValid = true;
			    let firstErrorField = null;
			    
			    // 모든 오류 스타일 초기화
			    $('.form-group').removeClass('error');
			    
			    // 아이디 중복 확인 여부
			    console.log('1. 아이디 중복 확인 상태:', isIdChecked);
			    if (!isIdChecked) {
			        console.log('❌ 아이디 중복 확인 실패');
			        showValidationMessage('loginIdMsg', '아이디 중복 확인을 해주세요.', 'error');
			        $('#loginId').closest('.form-group').addClass('error');
			        if (!firstErrorField) firstErrorField = '#loginId';
			        isValid = false;
			    } else {
			        console.log('✅ 아이디 중복 확인 통과');
			    }
			    
			    // 비밀번호 검사
			    const password = $('#password').val();
			    console.log('2. 비밀번호 값:', password);
			    if (!validatePassword(password)) {
			        console.log('❌ 비밀번호 검증 실패');
			        $('#password').closest('.form-group').addClass('error');
			        if (!firstErrorField) firstErrorField = '#password';
			        isValid = false;
			    } else {
			        console.log('✅ 비밀번호 검증 통과');
			    }
			    
			    // 비밀번호 확인 검사
			    const passwordConfirm = $('#passwordConfirm').val();
			    console.log('3. 비밀번호 확인 값:', passwordConfirm);
			    if (!validatePasswordConfirm(password, passwordConfirm)) {
			        console.log('❌ 비밀번호 확인 검증 실패');
			        $('#passwordConfirm').closest('.form-group').addClass('error');
			        if (!firstErrorField) firstErrorField = '#passwordConfirm';
			        isValid = false;
			    } else {
			        console.log('✅ 비밀번호 확인 검증 통과');
			    }
			    
			    // 이메일 검사 (휴대폰 인증인 경우)
			    if ($('#emailGroup').is(':visible')) {
			        const email = $('#email').val().trim();
			        console.log('4. 이메일 값:', email, '(이메일 그룹 표시됨)');
			        if (!email || !validateEmail(email)) {
			            console.log('❌ 이메일 검증 실패');
			            $('#email').closest('.form-group').addClass('error');
			            if (!firstErrorField) firstErrorField = '#email';
			            isValid = false;
			        } else {
			            console.log('✅ 이메일 검증 통과');
			        }
			    } else {
			        console.log('4. 이메일 그룹 숨겨짐 - 검사 건너뜀');
			    }
			    
			    // 휴대폰 검사 (이메일 인증인 경우)
			    if ($('#phoneGroup').is(':visible')) {
			        const phoneNumber = $('#phoneNumber').val().trim();
			        console.log('5. 휴대폰 값:', phoneNumber, '(휴대폰 그룹 표시됨)');
			        if (!phoneNumber || !validatePhoneNumber(phoneNumber)) {
			            console.log('❌ 휴대폰 검증 실패');
			            $('#phoneNumber').closest('.form-group').addClass('error');
			            if (!firstErrorField) firstErrorField = '#phoneNumber';
			            isValid = false;
			        } else {
			            console.log('✅ 휴대폰 검증 통과');
			        }
			    } else {
			        console.log('5. 휴대폰 그룹 숨겨짐 - 검사 건너뜀');
			    }
			    
			    // 주소 검사
			    const postcode = $('#postcode').val();
			    const roadAddress = $('#roadAddress').val();
			    console.log('6. 주소 - 우편번호:', postcode, ', 도로명주소:', roadAddress);
			    if (!postcode || !roadAddress) {
			        console.log('❌ 주소 검증 실패');
			        showValidationMessage('addressMsg', '주소를 입력해주세요.', 'error');
			        $('#postcode').closest('.form-group').addClass('error');
			        if (!firstErrorField) firstErrorField = '#postcode';
			        isValid = false;
			    } else {
			        console.log('✅ 주소 검증 통과');
			    }
			    
			    // 필수 약관 동의 검사
			    const agreeTerms = $('#agreeTerms').is(':checked');
			    const agreePrivacy = $('#agreePrivacy').is(':checked');
			    console.log('7. 약관 동의 - 이용약관:', agreeTerms, ', 개인정보:', agreePrivacy);
			    if (!agreeTerms || !agreePrivacy) {
			        console.log('❌ 약관 동의 검증 실패');
			        alert('필수 약관에 동의해주세요.');
			        $('.agreement-container').addClass('error');
			        if (!firstErrorField) firstErrorField = '.agreement-container';
			        isValid = false;
			    } else {
			        console.log('✅ 약관 동의 검증 통과');
			    }
			    
			    // 첫 번째 오류 필드로 스크롤
			    if (!isValid && firstErrorField) {
			        scrollToErrorField(firstErrorField);
			    }
			    
			    console.log('=== 최종 유효성 검사 결과:', isValid, '===');
			    return isValid;
			}
			
			// 오류 필드로 스크롤하는 함수
			function scrollToErrorField(fieldSelector) {
			    const $field = $(fieldSelector);
			    if ($field.length) {
			        // 부드러운 스크롤
			        $('html, body').animate({
			            scrollTop: $field.offset().top - 100 // 상단에서 100px 여유
			        }, 500, function() {
			            // 스크롤 완료 후 필드에 포커스
			            $field.focus();
			        });
			    }
			}
            // 회원가입 제출
			function submitRegistration() {
			    const formData = {
			            loginId: $('#loginId').val().trim(),
			            password: $('#password').val(),
			            name: registrationData.userName,
			            birth: formatBirthForServer(registrationData.birthDate), 
			            address: $('#roadAddress').val() + ($("#detailAddress").val() ? ' ' + $("#detailAddress").val() : ''),
			            role: 'COMMON'
			        };
			
			   
			    // 이메일/휴대폰 정보 추가
			    if (registrationData.email) {
			        formData.email = registrationData.email;
			        formData.phoneNumber = $('#phoneNumber').val().trim();
			    } else {
			        formData.email = $('#email').val().trim();
			        formData.phoneNumber = registrationData.phoneNumber;
			    }

			
			    $('#submitBtn').prop('disabled', true).text('가입 중...');
			
			    $.ajax({
			        url: '<c:url value="/api/register"/>',
			        method: 'POST',
			        contentType: 'application/json',
			        data: JSON.stringify(formData),
			        success: function(response) {
			            if (response.success) {
			                // localStorage 정리
			                localStorage.removeItem('registrationData');
			
			                alert('회원가입이 완료되었습니다!');
			                window.location.href = '<c:url value="/login?success=true"/>';
			            } else {
			                alert(response.message || '회원가입에 실패했습니다.');
			            }
			        },
			        error: function(xhr, status, error) {
			            console.error('회원가입 오류:', error);
			            let errorMessage = '회원가입 처리 중 오류가 발생했습니다. 다시 시도해주세요.';
			            
			            if (xhr.responseJSON && xhr.responseJSON.message) {
			                errorMessage = xhr.responseJSON.message;
			            }
			            
			            alert(errorMessage);
			        },
			        complete: function() {
			        	$('#submitBtn').on('click', function(e) {
			        	    e.preventDefault();
			        	    console.log('가입하기 버튼 클릭');
			        	    
			        	    if (validateForm()) {
			        	        console.log('유효성 검사 통과');
			        	        submitRegistration();
			        	    } else {
			        	        console.log('유효성 검사 실패');
			        	    }
			        	});
			        }
			    });
			}

            // 약관 모달 표시
            function showTermsModal(type) {
                currentModal = type;
                let title = '';
                let content = '';
                
                switch (type) {
                    case 'terms':
                        title = '이용약관';
                        content = getTermsContent();
                        break;
                    case 'privacy':
                        title = '개인정보 수집·이용 동의';
                        content = getPrivacyContent();
                        break;
                    case 'marketing':
                        title = '마케팅 정보 수신 동의';
                        content = getMarketingContent();
                        break;
                }
                
                $('#termsModalTitle').text(title);
                $('#termsModalBody').html(content);
                $('#termsModal').show();
            }

            function closeTermsModal() {
                $('#termsModal').hide();
                currentModal = '';
            }

            // 유틸리티 함수들
            function showValidationMessage(elementId, message, type) {
                const element = $('#' + elementId);
                element.text(message);
                element.removeClass('success error').addClass(type);
            }

            function clearValidationMessage(elementId) {
                $('#' + elementId).text('').removeClass('success error');
            }

            function formatBirthDate(birthDate) {
                if (birthDate.length === 8) {
                    return birthDate.substring(0, 4) + '-' + 
                           birthDate.substring(4, 6) + '-' + 
                           birthDate.substring(6, 8);
                }
                return birthDate;
            }

            function formatPhoneNumber(phoneNumber) {
                if (phoneNumber.length === 11) {
                    return phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
                } else if (phoneNumber.length === 10) {
                    return phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
                }
                return phoneNumber;
            }
            
         	// 날짜 형식 변환 함수 추가
            function formatBirthForServer(birthDate) {
                if (birthDate && birthDate.length === 8) {
                    // "19980411" -> "1998-04-11"
                    return birthDate.substring(0, 4) + '-' + 
                           birthDate.substring(4, 6) + '-' + 
                           birthDate.substring(6, 8);
                }
                return birthDate;
            }

            // 약관 내용 함수들
            function getTermsContent() {
                return `
                    <div class="terms-content">
                        <h4>제1조 (목적)</h4>
                        <p>이 약관은 빌려가유(이하 "회사")가 제공하는 서비스의 이용과 관련하여 회사와 이용자 간의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.</p>
                        
                        <h4>제2조 (정의)</h4>
                        <p>1. "서비스"란 회사가 제공하는 물품 대여 플랫폼 서비스를 의미합니다.</p>
                        <p>2. "이용자"란 회사의 서비스에 접속하여 이 약관에 따라 회사가 제공하는 서비스를 받는 회원 및 비회원을 말합니다.</p>
                        
                        <h4>제3조 (약관의 효력 및 변경)</h4>
                        <p>1. 이 약관은 서비스 화면에 게시하거나 기타의 방법으로 이용자에게 공지함으로써 효력을 발생합니다.</p>
                        <p>2. 회사는 관련 법률에 위배되지 않는 범위에서 이 약관을 개정할 수 있습니다.</p>
                        
                        <h4>제4조 (서비스의 제공 및 변경)</h4>
                        <p>1. 회사는 다음과 같은 업무를 수행합니다.</p>
                        <p>- 물품 대여 중개 서비스</p>
                        <p>- 기타 회사가 정하는 업무</p>
                        
                        <h4>제5조 (서비스 이용계약의 성립)</h4>
                        <p>1. 이용계약은 이용자가 약관의 내용에 대하여 동의를 하고 회원가입신청을 하면 회사가 이를 승낙함으로써 성립합니다.</p>
                        
                        <p class="terms-notice">※ 전체 약관은 서비스 이용 중 언제든지 확인하실 수 있습니다.</p>
                    </div>
                `;
            }

            function getPrivacyContent() {
                return `
                    <div class="terms-content">
                        <h4>1. 개인정보 수집 목적</h4>
                        <p>회사는 다음의 목적을 위하여 개인정보를 처리합니다.</p>
                        <ul>
                            <li>회원 가입 및 관리</li>
                            <li>서비스 제공</li>
                            <li>민원처리</li>
                            <li>법정 의무 이행</li>
                        </ul>
                        
                        <h4>2. 수집하는 개인정보의 항목</h4>
                        <p><strong>필수항목:</strong> 이름, 생년월일, 이메일, 휴대폰번호, 주소</p>
                        <p><strong>선택항목:</strong> 마케팅 수신 동의</p>
                        
                        <h4>3. 개인정보의 처리 및 보유기간</h4>
                        <p>개인정보는 수집 목적 달성 시까지 보유하며, 관련 법령에 따라 일정 기간 보관할 수 있습니다.</p>
                        
                        <h4>4. 개인정보 제3자 제공</h4>
                        <p>회사는 원칙적으로 이용자의 개인정보를 외부에 제공하지 않습니다. 다만, 법령에 의한 경우는 예외로 합니다.</p>
                        
                        <h4>5. 개인정보 처리의 위탁</h4>
                        <p>회사는 서비스 개선을 위해 개인정보 처리업무를 외부에 위탁할 수 있습니다.</p>
                        
                        <h4>6. 정보주체의 권리</h4>
                        <p>이용자는 언제든지 개인정보 처리정지, 정정·삭제, 처리현황 통지를 요구할 수 있습니다.</p>
                        
                        <p class="terms-notice">※ 개인정보 처리방침 전문은 서비스 내에서 확인하실 수 있습니다.</p>
                    </div>
                `;
            }

            function getMarketingContent() {
                return `
                    <div class="terms-content">
                        <h4>마케팅 정보 수신 동의</h4>
                        
                        <h4>1. 수집 목적</h4>
                        <p>신규 서비스 안내, 이벤트 정보, 할인 혜택 등 마케팅 정보 제공</p>
                        
                        <h4>2. 수집 항목</h4>
                        <p>이메일 주소, 휴대폰 번호</p>
                        
                        <h4>3. 수신 방법</h4>
                        <ul>
                            <li>이메일</li>
                            <li>SMS/MMS</li>
                            <li>앱 푸시 알림</li>
                        </ul>
                        
                        <h4>4. 보유 및 이용기간</h4>
                        <p>동의철회 시 또는 회원탈퇴 시까지</p>
                        
                        <h4>5. 동의 거부권</h4>
                        <p>마케팅 정보 수신에 동의하지 않으셔도 서비스 이용에는 제한이 없습니다.</p>
                        
                        <h4>6. 수신 거부</h4>
                        <p>언제든지 마이페이지에서 수신을 거부하실 수 있습니다.</p>
                        
                        <p class="terms-notice">※ 동의는 선택사항이며, 거부하셔도 서비스 이용에 불이익은 없습니다.</p>
                    </div>
                `;
            }
        });
    </script>
</body>
</html>