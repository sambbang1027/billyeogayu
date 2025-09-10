package app.domains.main.controller;

import app.domains.users.model.Users;
import app.domains.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private UsersService usersService;

    /**
     * 메인 홈페이지
     */
    @GetMapping("/main")
    public String home(Model model, HttpServletRequest request, HttpSession session) {
        log.info("=== 메인 페이지 요청 ===");
        
        try {
            // 1) 세션에서 SecurityContext 수동 복원
            if (session != null) {
                Object storedContext = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                if (storedContext instanceof SecurityContext) {
                    SecurityContextHolder.setContext((SecurityContext) storedContext);
                    log.info("메인 페이지 - 세션에서 SecurityContext 복원 완료 - 세션ID: {}", session.getId());
                }
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                // 로그인된 사용자
                String loginId = auth.getName();
                Users user = usersService.getUserByLoginId(loginId);
                
                if (user != null) {
                    model.addAttribute("loginUser", user);
                    model.addAttribute("isLoggedIn", true);
                    log.info("로그인된 사용자 정보 설정: {} ({})", user.getName(), user.getLoginId());
                    
                    // 저장된 returnUrl이 있으면 해당 페이지로 리다이렉트
                    String returnUrl = (String) session.getAttribute("returnUrl");
                    if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                        session.removeAttribute("returnUrl"); // 사용 후 제거
                        log.info("저장된 returnUrl로 리다이렉트: {}", returnUrl);
                        return "redirect:" + returnUrl;
                    }
                } else {
                    log.warn("인증된 사용자지만 사용자 정보를 찾을 수 없음: {}", loginId);
                    model.addAttribute("isLoggedIn", false);
                }
            } else {
                // 비로그인 사용자
                model.addAttribute("isLoggedIn", false);
                log.info("비로그인 사용자의 메인 페이지 접근");
            }
            
        } catch (Exception e) {
            log.error("메인 페이지 처리 중 오류 발생", e);
            model.addAttribute("isLoggedIn", false);
        }
        
        return "user/main";
    }
}