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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceApply;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;
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

    // 점검 상세 모달 (점검중 + 점검완료) 
    @GetMapping("/detail/{requestId}")
    @ResponseBody
    public MaintDetail  getInspectionDetail(@PathVariable("requestId") int requestId) {
     
    	MaintDetail detail = maintenanceService.getMaintDetail(requestId);
    	log.info("상세 정보 ----------> "+detail);
  	
        return detail;
    }


    // 점검 완료 처리 
    @PostMapping("/complete")
    @ResponseBody
    public Map<String, Object> completeMaintenance(@RequestBody  MaintenanceComplete maintenanceComplete){
    	
    	maintenanceService.completeMaintenance(maintenanceComplete);
    	
    	Map<String, Object> response = new HashMap<>();
    	
    	response.put("code", "SUCCESS");
    	response.put("message", "점검 기록이 등록되었습니다");
    	
    	return response;
    }
	
   
    // 점검 기록  수정
    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> updateRecord(@RequestBody MaintenanceEdit maintenanceEdit ){
    
    	maintenanceService.updateRecord(maintenanceEdit);
    	
    	Map<String, Object> response = new HashMap<>();
    	
    	response.put("code", "SUCCESS");
    	response.put("message", "점검 기록이 수정되었습니다");
    	
    	return response;
    }
	
    @GetMapping("/part-list/{assetId}")
    @ResponseBody
    public List<Map<String, Object>> getPartList(@PathVariable("assetId") int assetId){
    	return maintenanceService.getPartList(assetId);
    }
    
    // 점검 신청
    @PostMapping("/apply")
    @ResponseBody
    public Map<String, Object> applyMaintenance(@RequestBody MaintenanceApply maintenanceApply){
    	maintenanceService.applyMaintenance(maintenanceApply);
    	
    	Map<String, Object> response = new HashMap<>();
    	
    	response.put("code", "SUCCESS");
    	response.put("message", "점검 신청이 등록되었습니다");
    	
    	return response;
    }

}
