package app.domains.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

    @GetMapping("/list")
    public String maintenance(Model model) {
        model.addAttribute("pageTitle", "점검 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/maintenance/maintenancelist.jsp");
        model.addAttribute("activePage", "maintenance");
        return "layout/admin/main";
    }

 // 점검중 상세 모달
    @GetMapping("/maintenance/detail/{id}")
    public String getInspectionDetail(@PathVariable Long id, Model model) {
       // model.addAttribute("maintenance", maintenanceService.findById(id));
        return "maintenance/inspectionModal";
    }

    // 점검완료 수정 모달
    @GetMapping("/maintenance/edit/{id}")
    public String getInspectionEdit(@PathVariable Long id, Model model) {
     //   model.addAttribute("maintenance", maintenanceService.findById(id));
        return "maintenance/inspectionEditModal";
    }

}
