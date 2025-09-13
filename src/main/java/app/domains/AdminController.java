package app.domains;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.domains.maintenance.service.MaintenanceService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "forward:/dashboard";
    }

    @GetMapping("/asset")
    public String asset(Model model) {
        return "forward:/admin/asset/list";
    }

    @GetMapping("/maintenance")
    public String maintenance(Model model) {
    	return "forward:/maintenance/list";
    }
    
	@Autowired
    private MaintenanceService maintenanceService;
    
	@GetMapping("/reservation")
	public String reservation(Model model) {
	    System.out.println("=== AdminController.reservation() 호출됨 ===");
	    
	    try {
	        List<String> categoryList = maintenanceService.getAssetCategoryList();
	        System.out.println("categoryList: " + categoryList);
	        
	        model.addAttribute("categoryList", categoryList);
	        model.addAttribute("pageTitle", "예약 관리");
	        model.addAttribute("contentPage", "/WEB-INF/views/admin-reservation/reservationlist.jsp");
	        model.addAttribute("activePage", "reservation");
	        
	        System.out.println("=== layout/admin/main 반환 ===");
	        return "layout/admin/main";
	    } catch (Exception e) {
	        System.out.println("오류 발생: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}
}
