package app.domains.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.domains.users.model.Users;
import app.domains.users.service.UsersService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private UsersService usersService;

    /**
     * 루트 경로 - 로그인 상태에 따라 리다이렉트
     */
    @GetMapping("/")
    public String rootPage() {
        log.info("=== 루트 페이지 요청 ===");
        
        // 로그인 상태 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());
        
        if (isLoggedIn) {
            log.info("로그인된 사용자 - resource/list로 리다이렉트: {}", auth.getName());
            return "redirect:/resource/list";
        } else {
            log.info("비로그인 사용자 - /main으로 리다이렉트");
            return "redirect:/main";
        }
    }

    /**
     * 메인 홈페이지 - /main 경로 처리
     */
    @GetMapping("/main")
    public String mainPage(Model model) {
        log.info("=== 메인 페이지 요청 ===");
        
        try {
            // 로그인 상태 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());
            Users loginUser = null;
            
            if (isLoggedIn) {
                try {
                    String loginId = auth.getName();
                    loginUser = usersService.getUserByLoginId(loginId);
                    if (loginUser != null) {
                        log.info("로그인된 사용자: {} (ID: {})", loginUser.getName(), loginUser.getUserId());
                    }
                } catch (Exception e) {
                    log.warn("사용자 정보 조회 실패: {}", e.getMessage());
                    isLoggedIn = false;
                }
            }
            
            model.addAttribute("isLoggedIn", isLoggedIn);
            if (loginUser != null) {
                model.addAttribute("loginUser", loginUser);
            }
            
            log.info("메인 페이지 로드 완료 - 로그인 상태: {}", isLoggedIn);
            
        } catch (Exception e) {
            log.error("메인 페이지 처리 중 오류 발생", e);
            model.addAttribute("isLoggedIn", false);
        }
        
        return "layout/user/main";
    }
}