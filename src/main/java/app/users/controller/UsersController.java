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
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            responseData.put("username", userDetails.getUsername());
            responseData.put("authorities", userDetails.getAuthorities());
            responseData.put("maxInactiveInterval", session.getMaxInactiveInterval());

            log.info("=== 로그인 성공 ===");
            log.info("세션 ID: {}", session.getId());

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
}