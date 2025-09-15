package app.domains.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

    /**
     * 루트 경로 - 메인 페이지로 리다이렉트
     */
    @GetMapping("/")
    public String rootPage() {
        log.info("=== 루트 페이지 요청 ===");
        log.info("/main으로 리다이렉트");
        return "redirect:/main";
    }

    /**
     * 메인 홈페이지 - /main 경로 처리
     */
    @GetMapping("/main")
    public String mainPage() {
        log.info("=== 메인 페이지 요청 ===");
        log.info("메인 페이지 로드 완료");
        return "layout/user/main";
    }
}