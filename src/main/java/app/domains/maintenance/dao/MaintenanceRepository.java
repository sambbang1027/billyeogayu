package app.domains.maintenance.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;
import oracle.net.aso.m;

@Mapper
public interface MaintenanceRepository {
		
		// list 조회 + 필터링 + 검색  
		List<Maintenance> searchMaintList(MaintSearch maintSearch);
		int getSearchCount(MaintSearch search);

		
		// 필터링 항목들 
		List<String> getAssetCategoryList ();
		List<String> getCompanyList();
		
		
		// 점검 기록 상세 내용 출력 
		MaintDetail getMaintDetail (int requestId);
		
		// 점검 완료 처리 (기록등록 description + note)
		int completeMaintenance(MaintenanceComplete maintenanceComplete);
		
		// 기록 수정
		int updateRecord( MaintenanceEdit maintenanceEdit);
		
}
