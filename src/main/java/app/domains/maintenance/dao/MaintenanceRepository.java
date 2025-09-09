package app.domains.maintenance.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;

@Mapper
public interface MaintenanceRepository {
		
		// list 조회 + 필터링 + 검색  
		List<Maintenance> searchMaintList(MaintSearch maintSearch);
		int getSearchCount(MaintSearch search);

		
		// 필터링 항목들 
		List<String> getAssetCategoryList ();
		List<String> getCompanyList();
		
		
		// 점검 완료 처리를 위한 상세 내용 출력 
		MaintDetail getMaintDetail (int requestId);
		
		
}
