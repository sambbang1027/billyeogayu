package app.domains.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/dashboard")
public class DashBoardController {

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("contentPage", "/WEB-INF/views/dashboard/dashboard.jsp");
        model.addAttribute("activePage", "dashboard");
        return "layout/admin/main";   // 공통 레이아웃
    }
}
