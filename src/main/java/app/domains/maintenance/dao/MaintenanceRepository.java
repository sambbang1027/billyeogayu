package app.domains.maintenance.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;


@Mapper
public interface MaintenanceRepository {

		// list 조회 + 필터링 + 검색
		List<Maintenance> searchMaintList(MaintSearch maintSearch);
		int getSearchCount(MaintSearch search);


		// 필터링 항목들
		List<String> getAssetCategoryList ();
		List<String> getCompanyList();
		List<Map<String, Object>> getPartList(int assetId);
		
		
		// 점검 기록 상세 내용 출력 
		MaintDetail getMaintDetail (int requestId);
		
		// 점검 완료 처리 (기록등록 description + note)
		int completeMaintenance(MaintenanceComplete maintenanceComplete);
		// 점검 완료 시 -> 자산과 부품의 상태 변경 
		int updateAssetStatusAfterMaintenance(int assetId);
		int updatePartStatusAfterMaintenance(int partId);
		
		//id 조회 
		List<Map<String, Object>> findByAssetId(int requestId);
		// 기록 수정
		int updateRecord( MaintenanceEdit maintenanceEdit);
		
		//점검 신청
		int getNextRequestId();
		int applyMaintenance(Map<String, Object> param);
		int updateAssetStatus(int assetId);

		
}
