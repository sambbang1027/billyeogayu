package app.domains.maintenance.service;

import java.util.List;
import java.util.Map;

import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceApply;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;

public interface MaintenanceService {


	 // 점검 리스트 조회 + 필터링 + 검색
	 List<Maintenance> searchMaintList(MaintSearch maintSearch);
	 int getSearchCount(MaintSearch maintSearch);
	 
	 // 필터링 항목 불러오기 
	 List<String> getAssetCategoryList();
	 List<String>getCompanyList();
	 List<Map<String, Object>> getPartList(int assetId);
	 
	 
	 // 점검 기록 상세 
	 MaintDetail getMaintDetail(int requestId);
	// 점검 완료 처리 
	void completeMaintenance(MaintenanceComplete maintenanceComplete);
	
	// 기록 수정 
	void updateRecord (MaintenanceEdit maintenanceEdit);
	
	// 점검 신청 
	void applyMaintenance(MaintenanceApply maintenanceApply);


}
