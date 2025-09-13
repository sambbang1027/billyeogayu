package app.domains.dashboard.controller;

import app.domains.dashboard.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/dashboard")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping("")
    public String dashboard(Model model) {
        // 자산 메트릭 데이터만 조회 (상단 카드용)
        Map<String, Long> metrics = dashBoardService.getAssetMetrics();
        model.addAttribute("metrics", metrics);

        model.addAttribute("pageTitle", "대시보드");
        model.addAttribute("contentPage", "/WEB-INF/views/dashboard/dashboard.jsp");
        model.addAttribute("activePage", "dashboard");
        return "layout/admin/main";   // 공통 레이아웃
    }

    @GetMapping("/categoryData")
    @ResponseBody
    public Map<String, Object> getCategoryData() {
        return dashBoardService.getAssetCategoryData();
    }

    @GetMapping("/distributionData")
    @ResponseBody
    public Map<String, Object> getDistributionData() {
        return dashBoardService.getAssetDistributionData();
    }
    
    @GetMapping("/usageFilters")
    @ResponseBody
    public Map<String, Object> getUsageFilters() {
        return dashBoardService.getUsageChartFilters();
    }
}
