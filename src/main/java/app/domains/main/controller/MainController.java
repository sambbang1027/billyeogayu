package app.domains.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

    /**
     * 메인 홈페이지 - 루트 경로와 /main 모두 처리
     */
    @GetMapping("/")
    public String home(Model model) {
        log.info("=== 메인 페이지 요청 ===");
        
        try {
            // 임시로 기본값 설정 (Users 서비스가 정상화되면 복구)
            model.addAttribute("isLoggedIn", false);
            
            log.info("메인 페이지 로드 완료");
            
        } catch (Exception e) {
            log.error("메인 페이지 처리 중 오류 발생", e);
            model.addAttribute("isLoggedIn", false);
        }
        
        return "layout/user/main"; // home.jsp 파일을 찾도록 수정
    }
    
    /**
     * /main 경로도 처리
     */
    @GetMapping("/main")
    public String mainPage(Model model) {
        return home(model); // 같은 처리 로직 사용
    }
}