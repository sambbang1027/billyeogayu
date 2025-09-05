package app.domains;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("contentPage", "/WEB-INF/views/dashboard.jsp");
        model.addAttribute("activePage", "dashboard");
        return "layout/admin/main";   // 공통 레이아웃
    }

    @GetMapping("/asset")
    public String asset(Model model) {
        return "forward:/asset/list";
    }

    @GetMapping("/maintenance")
    public String maintenance(Model model) {
        model.addAttribute("pageTitle", "점검 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/maintenance.jsp");
        model.addAttribute("activePage", "maintenance");
        return "layout/admin/main";
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        model.addAttribute("pageTitle", "예약 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/reservation.jsp");
        model.addAttribute("activePage", "reservation");
        return "layout/admin/main";
    }
}
