package app.users.controller;

import app.users.model.Users;
import app.users.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 로그인 요청 DTO
    public static class LoginRequest {
        public String loginId;
        public String password;
        
        // 기본 생성자
        public LoginRequest() {}
        
        // getter, setter
        public String getLoginId() { return loginId; }
        public void setLoginId(String loginId) { this.loginId = loginId; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 응답 DTO
    public static class ApiResponse {
        public boolean success;
        public String message;
        public Object data;
        public long timestamp;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        log.info("=== 회원가입 시작 ===");
        log.info("요청 데이터: loginId={}, email={}, name={}, role={}", 
                 user.getLoginId(), user.getEmail(), user.getName(), user.getRole());
        
        try {
            // 입력 데이터 검증 (역할 설정 포함)
            validateUserInput(user);
            
            // 회원가입 처리
            Users createdUser = usersService.createUser(user);
            
            log.info("=== 회원가입 성공 ===");
            log.info("생성된 사용자: userId={}, loginId={}, role={}", 
                     createdUser.getUserId(), createdUser.getLoginId(), createdUser.getRole());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "회원가입이 완료되었습니다.", createdUser));
            
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error("데이터베이스 제약조건 위배: {}", e.getMessage());
            
            String errorMessage = "회원가입 처리 중 오류가 발생했습니다.";
            if (e.getMessage().contains("CHK_USERS_ROLE")) {
                errorMessage = "유효하지 않은 사용자 역할입니다. 관리자에게 문의하세요.";
            } else if (e.getMessage().contains("UNIQUE")) {
                errorMessage = "이미 사용 중인 정보입니다. (아이디 또는 이메일 중복)";
            }
            
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, errorMessage, null));
            
        } catch (Exception e) {
            log.error("회원가입 처리 중 예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", null));
        }
    }

    /**
     * 로그인 ID 중복 확인 API
     */
    @PostMapping("/check-loginId")
    public ResponseEntity<Map<String, Object>> checkLoginId(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        boolean exists = usersService.isLoginIdExists(loginId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 ID입니다." : "사용 가능한 ID입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 중복 확인 API
     */
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = usersService.isEmailExists(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 API (세션 기반)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, 
                                 HttpServletRequest request) {
        try {
            log.info("=== 로그인 시도 ===");
            log.info("loginId: {}", loginRequest.getLoginId());

            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getLoginId(), 
                    loginRequest.getPassword()
                )
            );

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 SecurityContext 저장
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                               SecurityContextHolder.getContext());

            // 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 세션에 저장된 returnUrl 가져오기
            String returnUrl = (String) session.getAttribute("returnUrl");
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            responseData.put("username", userDetails.getUsername());
            responseData.put("authorities", userDetails.getAuthorities());
            responseData.put("maxInactiveInterval", session.getMaxInactiveInterval());
            responseData.put("returnUrl", returnUrl);
            

            log.info("=== 로그인 성공 ===");
            log.info("세션 ID: {}", session.getId());
            log.info("returnUrl: {}", returnUrl);

            return ResponseEntity.ok(new ApiResponse(true, "로그인 성공", responseData));

        } catch (BadCredentialsException e) {
            log.warn("로그인 실패: {} - 잘못된 인증정보", loginRequest.getLoginId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "아이디 또는 비밀번호가 잘못되었습니다.", null));
        } catch (Exception e) {
            log.error("로그인 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "로그인 처리 중 오류가 발생했습니다.", null));
        }
    }

    /**
     * 로그아웃 API
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                log.info("로그아웃: 세션 {} 무효화", session.getId());
                session.invalidate();
            }
            
            SecurityContextHolder.clearContext();
            
            return ResponseEntity.ok(new ApiResponse(true, "로그아웃 성공", null));
        } catch (Exception e) {
            log.error("로그아웃 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "로그아웃 처리 중 오류가 발생했습니다.", null));
        }
    }

    /**
     * 로그인 상태 확인 API
     */
    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        try {
            log.info("=== 인증 상태 확인 시작 ===");
            
            HttpSession session = request.getSession(false);
            log.info("세션 존재 여부: {}", session != null);
            if (session != null) {
                log.info("세션 ID: {}", session.getId());
                log.info("세션 유효 여부: {}", request.isRequestedSessionIdValid());
                
                Object storedContext = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                log.info("세션에서 SecurityContext 존재 여부: {}", storedContext != null);
                
                // 세션에서 SecurityContext를 수동으로 복원
                if (storedContext instanceof SecurityContext) {
                    SecurityContext sessionContext = (SecurityContext) storedContext;
                    SecurityContextHolder.setContext(sessionContext);
                    log.info("세션에서 SecurityContext 수동 복원 완료");
                }
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("SecurityContext에서 Authentication 존재 여부: {}", auth != null);
            if (auth != null) {
                log.info("인증 여부: {}", auth.isAuthenticated());
                log.info("사용자명: {}", auth.getName());
                log.info("권한: {}", auth.getAuthorities());
            }

            if (session == null || auth == null || !auth.isAuthenticated() || 
                "anonymousUser".equals(auth.getName())) {
                log.warn("인증 실패 - 세션: {}, 인증: {}, 인증여부: {}, 사용자명: {}", 
                         session != null, auth != null, 
                         auth != null ? auth.isAuthenticated() : false,
                         auth != null ? auth.getName() : "null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "인증되지 않았습니다.", null));
            }

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            responseData.put("username", auth.getName());
            responseData.put("authorities", auth.getAuthorities());
            responseData.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval());

            log.info("=== 인증 확인 성공 ===");
            return ResponseEntity.ok(new ApiResponse(true, "인증된 사용자입니다.", responseData));
        } catch (Exception e) {
            log.error("인증 확인 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "인증 확인 중 오류가 발생했습니다.", null));
        }
    }

    /**
     * 사용자 정보 조회 API
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        try {
            log.info("=== 사용자 정보 조회 시작 ===");
            
            // 먼저 세션에서 SecurityContext 복원 시도
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object storedContext = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContext sessionContext = (SecurityContext) storedContext;
                    SecurityContextHolder.setContext(sessionContext);
                    log.info("세션에서 SecurityContext 복원 완료");
                }
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("SecurityContext 인증 정보: {}", auth != null ? "존재" : "없음");
            
            if (auth != null) {
                log.info("인증 상태: {}", auth.isAuthenticated());
                log.info("인증된 사용자명: {}", auth.getName());
                log.info("사용자 권한: {}", auth.getAuthorities());
                log.info("Principal 타입: {}", auth.getPrincipal().getClass().getSimpleName());
            }
            
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                log.warn("인증 실패 - auth: {}, authenticated: {}, name: {}", 
                         auth != null, 
                         auth != null ? auth.isAuthenticated() : false,
                         auth != null ? auth.getName() : "null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "인증되지 않았습니다.", null));
            }

            String loginId = auth.getName();
            log.info("조회할 사용자 loginId: {}", loginId);

            Users user = usersService.getUserByLoginId(loginId);
            
            if (user != null) {
                log.info("조회된 사용자 정보: userId={}, loginId={}, name={}, role={}", 
                         user.getUserId(), user.getLoginId(), user.getName(), user.getRole());
            } else {
                log.warn("사용자 정보 없음 - loginId: {}", loginId);
            }

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "사용자를 찾을 수 없습니다.", null));
            }

            // 비밀번호 제거
            user.setPassword(null);
            
            log.info("=== 사용자 정보 조회 성공 ===");
            return ResponseEntity.ok(new ApiResponse(true, "사용자 정보 조회 성공", user));
            
        } catch (Exception e) {
            log.error("사용자 정보 조회 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "사용자 정보 조회 중 오류가 발생했습니다.", null));
        }
    }

    /**
     * 입력 데이터 검증
     */
    private void validateUserInput(Users user) {
        if (user.getLoginId() == null || user.getLoginId().trim().isEmpty()) {
            throw new IllegalArgumentException("로그인 ID는 필수입니다.");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다.");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        
        // 이메일 형식 검증
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    /**
     * 세션 만료 처리
     */
    @GetMapping("/session-expired")
    public void sessionExpired(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        ApiResponse apiResponse = new ApiResponse(false, "세션이 만료되었습니다.", null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
    



    // 그리고 handleAuthComplete 메서드 수정
    @PostMapping("/auth-complete")
    public ResponseEntity<ApiResponse> handleAuthComplete(@RequestBody Map<String, Object> request) {
        try {
            String purpose = (String) request.get("purpose");
            String authType = (String) request.get("authType");
            String userName = (String) request.get("userName");
            String birthDate = (String) request.get("birthDate");
            String email = (String) request.get("email");
            String phoneNumber = (String) request.get("phoneNumber");
            
            log.info("=== 인증 완료 후 처리 ===");
            log.info("목적: {}, 인증타입: {}, 이름: {}", purpose, authType, userName);
            log.info("받은 데이터 - 이름: [{}], 생년월일: [{}], 이메일: [{}]", userName, birthDate, email);
            
            // 입력값 검증
            if (purpose == null || authType == null || userName == null || birthDate == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "필수 정보가 누락되었습니다.", null));
            }
            
            // 회원가입인 경우 사용자 조회 생략
            if ("register".equals(purpose)) {
                Map<String, Object> result = new HashMap<>();
                result.put("redirectUrl", "/register/info");
                result.put("authType", authType);
                result.put("authKey", "email".equals(authType) ? email : phoneNumber);
                
                log.info("회원가입 인증 완료");
                return ResponseEntity.ok(new ApiResponse(true, "인증 완료 처리 성공", result));
            }
            
            // 아이디 찾기 또는 비밀번호 재설정인 경우 사용자 조회
            Users user = null;
            if ("email".equals(authType)) {
                if (email == null || email.trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "이메일 정보가 누락되었습니다.", null));
                }
                
                log.info("=== 이메일로 사용자 조회 시도 ===");
                log.info("검색할 이메일: [{}]", email);
                
                user = usersService.getUserByEmail(email);
                
                if (user != null) {
                    log.info("=== 이메일로 사용자 조회 성공 ===");
                    log.info("조회된 사용자 - ID: {}, 로그인ID: {}, 이름: [{}], 이메일: [{}]", 
                            user.getUserId(), user.getLoginId(), user.getName(), user.getEmail());
                    log.info("조회된 생년월일: [{}] (타입: {})", user.getBirth(), 
                            user.getBirth() != null ? user.getBirth().getClass().getSimpleName() : "null");
                } else {
                    log.warn("=== 이메일로 사용자 조회 실패 ===");
                    log.warn("해당 이메일로 가입된 사용자가 없음: [{}]", email);
                    return ResponseEntity.ok(new ApiResponse(false, "해당 이메일로 가입된 사용자가 없습니다.", null));
                }
                
            } else if ("phone".equals(authType)) {
                if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "휴대폰 번호 정보가 누락되었습니다.", null));
                }
                
                log.info("=== 휴대폰으로 사용자 조회 시도 ===");
                log.info("검색할 휴대폰: [{}]", phoneNumber);
                
                user = usersService.getUserByPhone(phoneNumber);
                
                if (user != null) {
                    log.info("=== 휴대폰으로 사용자 조회 성공 ===");
                    log.info("조회된 사용자 - ID: {}, 로그인ID: {}, 이름: [{}], 휴대폰: [{}]", 
                            user.getUserId(), user.getLoginId(), user.getName(), user.getPhoneNumber());
                    log.info("조회된 생년월일: [{}] (타입: {})", user.getBirth(), 
                            user.getBirth() != null ? user.getBirth().getClass().getSimpleName() : "null");
                } else {
                    log.warn("=== 휴대폰으로 사용자 조회 실패 ===");
                    log.warn("해당 휴대폰으로 가입된 사용자가 없음: [{}]", phoneNumber);
                    return ResponseEntity.ok(new ApiResponse(false, "해당 휴대폰으로 가입된 사용자가 없습니다.", null));
                }
            }
            
            // 사용자 정보 검증
            if (user == null) {
                log.error("사용자 조회 결과가 null입니다");
                return ResponseEntity.ok(new ApiResponse(false, "일치하는 사용자 정보를 찾을 수 없습니다.", null));
            }
            
            // 이름과 생년월일 검증 - 수정된 부분
            log.info("=== 사용자 정보 검증 시작 ===");
            log.info("요청 이름: [{}] (길이: {}, 바이트: {})", userName, userName.length(), userName.getBytes().length);
            log.info("DB 이름: [{}] (길이: {}, 바이트: {})", user.getName(), user.getName().length(), user.getName().getBytes().length);
            log.info("요청 생년월일: [{}] (타입: {}, 길이: {})", birthDate, birthDate.getClass().getSimpleName(), birthDate.length());
            log.info("DB 생년월일: [{}] (타입: {}, 길이: {})", user.getBirth(), 
                    user.getBirth() != null ? user.getBirth().getClass().getSimpleName() : "null",
                    user.getBirth() != null ? user.getBirth().toString().length() : 0);
            
            boolean nameMatch = user.getName().equals(userName);
            boolean birthMatch = compareBirthDate(user.getBirth(), birthDate);  // 수정된 부분
            
            log.info("이름 일치 여부: {}", nameMatch);
            log.info("생년월일 일치 여부: {}", birthMatch);
            
            if (!nameMatch || !birthMatch) {
                if (!nameMatch) {
                    log.warn("이름 불일치 - DB: [{}], 요청: [{}]", user.getName(), userName);
                }
                if (!birthMatch) {
                    log.warn("생년월일 불일치 - DB: [{}], 요청: [{}]", user.getBirth(), birthDate);
                }
                return ResponseEntity.ok(new ApiResponse(false, "입력하신 정보와 일치하는 사용자를 찾을 수 없습니다.", null));
            }
            
            // purpose에 따른 분기 처리
            Map<String, Object> result = new HashMap<>();
            result.put("authType", authType);
            result.put("authKey", "email".equals(authType) ? email : phoneNumber);
            
            switch (purpose) {
                case "find-id":
                    // 아이디 찾기 - 전체 아이디 반환 
                    result.put("foundId", user.getLoginId());
                    result.put("redirectUrl", "/find-id/result");
                    log.info("아이디 찾기 성공 - 아이디: {}", user.getLoginId());
                    break;
                    
                case "reset-password":
                    // 비밀번호 재설정 - 사용자 ID 반환
                    result.put("userId", user.getUserId());
                    result.put("redirectUrl", "/reset-password/form");
                    log.info("비밀번호 재설정 사용자 확인 성공 - userId: {}", user.getUserId());
                    break;
                    
                default:
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse(false, "잘못된 요청입니다.", null));
            }
            
            return ResponseEntity.ok(new ApiResponse(true, "인증 완료 처리 성공", result));
            
        } catch (Exception e) {
            log.error("인증 완료 처리 중 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "처리 중 오류가 발생했습니다.", null));
        }
    }
 

    /**
     * 비밀번호 재설정 실행 API
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String userIdStr = request.get("userId");
            String newPassword = request.get("newPassword");
            String confirmPassword = request.get("confirmPassword");
            
            log.info("=== 비밀번호 재설정 실행 ===");
            log.info("사용자 ID: {}", userIdStr);
            
            // 입력값 검증
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "사용자 정보가 없습니다.", null));
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "새 비밀번호를 입력해주세요.", null));
            }
            
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "비밀번호 확인이 일치하지 않습니다.", null));
            }
            
            // 비밀번호 강도 검증
            if (newPassword.length() < 8) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "비밀번호는 8자 이상이어야 합니다.", null));
            }
            
            // 비밀번호 복잡성 검증
            String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
            if (!newPassword.matches(passwordRegex)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.", null));
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // 사용자 조회
            Users user = usersService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "사용자 정보를 찾을 수 없습니다.", null));
            }
            
            // 비밀번호 재설정
            user.setPassword(newPassword);
            usersService.updateUser(user);
            
            log.info("비밀번호 재설정 성공 - userId: {}", userId);
            
            return ResponseEntity.ok(new ApiResponse(true, "비밀번호가 성공적으로 재설정되었습니다.", null));
            
        } catch (NumberFormatException e) {
            log.error("잘못된 사용자 ID 형식: {}", request.get("userId"));
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "잘못된 사용자 정보입니다.", null));
        } catch (Exception e) {
            log.error("비밀번호 재설정 실행 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "비밀번호 재설정 중 오류가 발생했습니다.", null));
        }
    }
    
    /**
     * 생년월일 비교 유틸리티 메서드
     */
    private boolean compareBirthDate(Object dbBirth, String requestBirth) {
        try {
            if (dbBirth instanceof Date) {
                Date dbBirthDate = (Date) dbBirth;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dbBirthString = sdf.format(dbBirthDate);
                
                log.info("생년월일 변환: {} -> {}", dbBirthDate, dbBirthString);
                return dbBirthString.equals(requestBirth);
            }
            return false;
        } catch (Exception e) {
            log.error("생년월일 비교 중 오류", e);
            return false;
        }
    }
    
}