package app.users.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.users.config.JwtTokenProvider;
import app.users.dao.UsersRepository;
import app.users.model.Users;
import app.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api")
@Slf4j
public class UsersController {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Autowired
    private UsersService usersService;

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Users user) {
        log.info("=== 회원가입 시작 ===");
        log.info("요청 데이터: loginId={}, email={}, name={}", 
                 user.getLoginId(), user.getEmail(), user.getName());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 입력 데이터 검증
            validateUserInput(user);
            
            // 회원가입 처리
            usersService.registerUser(user);
            
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            
            log.info("=== 회원가입 성공 ===");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
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
     * 로그인 API
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        log.info("=== 로그인 시작 ===");
        log.info("요청 데이터: {}", user.toString());
        
        try {
            String loginId = user.get("loginId");
            String password = user.get("password");
            log.info("추출된 loginId: {}, password 길이: {}", loginId, password != null ? password.length() : "null");

            // 사용자 조회
            log.info("데이터베이스에서 사용자 조회 시작 - loginId: {}", loginId);
            Users member = userRepository.selectUserByLoginId(loginId);
            
            if (member == null) {
                log.warn("사용자를 찾을 수 없음 - loginId: {}", loginId);
                throw new IllegalArgumentException("사용자가 없습니다.");
            }
            
            log.info("사용자 조회 성공 - userId: {}, name: {}", member.getUserId(), member.getName());
            log.info("DB 저장된 password: {}", member.getPassword());

            // 비밀번호 확인
            log.info("비밀번호 검증 시작");
            boolean passwordMatches = passwordEncoder.matches(password, member.getPassword());
            log.info("비밀번호 매칭 결과: {}", passwordMatches);
            
            if (!passwordMatches) {
                log.warn("비밀번호 불일치 - loginId: {}", loginId);
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            // JWT 토큰 생성 및 반환
            log.info("JWT 토큰 생성 시작");
            String token = jwtTokenProvider.generateToken(member);
            log.info("JWT 토큰 생성 완료, 토큰 길이: {}", token != null ? token.length() : "null");
            
            log.info("=== 로그인 성공 ===");
            return token;
            
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            log.error("오류 타입: {}", e.getClass().getSimpleName());
            log.error("오류 메시지: {}", e.getMessage());
            throw e; // 다시 던져서 전역 예외 처리기에서 처리하도록
        }
    }
    /**
     * 토큰 테스트 API
     */
    @GetMapping("/test-jwt")
    public String testJwt(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUserId(token);
            return "인증된 사용자: " + userId;
        }
        
        return "인증 실패";
    }
    


    
}