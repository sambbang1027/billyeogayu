package app.domains.maintenance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.service.MaintenanceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

	@Autowired
	private MaintenanceService maintenanceService;
	
    @GetMapping("/list")
    public String maintenance( Model model) {
    	
    	List<String> categoryList = maintenanceService.getAssetCategoryList();
    	List<String> companyList = maintenanceService.getCompanyList();
    	
    	model.addAttribute("categories", categoryList);
    	model.addAttribute("companies", companyList);
    	
        model.addAttribute("pageTitle", "점검 관리");
        model.addAttribute("contentPage", "/WEB-INF/views/maintenance/maintenancelist.jsp");
        model.addAttribute("activePage", "maintenance");
        return "layout/admin/main";
    }
    
    @ResponseBody
	@GetMapping("/search")
	public  Map<String, Object> searchMaintenance (@ModelAttribute MaintSearch maintSearch, 
						@RequestParam(name="page", defaultValue = "1") int page) {
		
		int pageSize = 10; // 한 페이지에 보여줄 행 수
		maintSearch.setStartRow((page -1) * pageSize+1);
		maintSearch.setEndRow(page * pageSize);
		
		List<Maintenance> list = maintenanceService.searchMaintList(maintSearch);
		int totalCount = maintenanceService.getSearchCount(maintSearch);
		int totalPage = (int)Math.ceil((double)totalCount/pageSize);
		
		Map<String, Object>  response =   new HashMap<>();
		response.put("list", list);
		response.put("currentPage", page);
		response.put("totalPage", totalPage);
		response.put("totalCount", totalCount);
		
		return response;
	}
    
    
    // 필터 항목들 조회 
    @GetMapping("/filter")
    @ResponseBody
    public Map<String  , Object> getFilterList(){
    	
    	List<String> categoryList = maintenanceService.getAssetCategoryList();
    	List<String> companyList = maintenanceService.getCompanyList();
    	Map<String, Object> response = new HashMap<>();
    	response.put("categoryList", categoryList);
    	response.put("companyList", companyList);
    	
    	log.info("자산 종류 -----" + categoryList);
    	log.info("회사  리스트 -------> "+ companyList);
    	
    	return response;
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
