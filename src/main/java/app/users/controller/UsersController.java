package app.users.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.users.config.JwtTokenProvider;
import app.users.dao.UsersRepository;
import app.users.model.Users;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        String loginId = user.get("loginId");
        String password = user.get("password");

        // 사용자 조회
        Users member = userRepository.selectUserByLoginId(loginId);
        if (member == null) {
            throw new IllegalArgumentException("사용자가 없습니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // JWT 토큰 생성 및 반환
        return jwtTokenProvider.generateToken(member);
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
    

    /**
     * DB 연결 테스트 - 전체 사용자 조회
     */
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        System.out.println("=== DB 연결 테스트: 전체 사용자 조회 시작 ===");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 전체 사용자 수 조회
            int userCount = userRepository.getUserCount();
            System.out.println("총 사용자 수: " + userCount);
            
            // 전체 사용자 목록 조회 (role을 null로 전달하면 전체 조회)
            List<Users> users = userRepository.selectUsersByRole(null);
            System.out.println("조회된 사용자 목록:");
            
            for (Users user : users) {
                System.out.println("- ID: " + user.getUserId() + 
                                 ", 로그인ID: " + user.getLoginId() + 
                                 ", 이름: " + user.getName() +
                                 ", 역할: " + user.getRole());
            }
            
            result.put("status", "success");
            result.put("message", "DB 연결 성공");
            result.put("userCount", userCount);
            result.put("users", users);
            result.put("timestamp", new Date());
            
            System.out.println("=== DB 연결 테스트 완료 ===");
            
        } catch (Exception e) {
            System.err.println("DB 연결 실패: " + e.getMessage());
            e.printStackTrace();
            
            result.put("status", "error");
            result.put("message", "DB 연결 실패: " + e.getMessage());
        }
        
        return result;
    }
}