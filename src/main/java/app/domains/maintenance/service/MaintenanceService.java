package app.domains.maintenance.service;

import java.util.List;

import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;

public interface MaintenanceService {


	 // 점검 리스트 조회 + 필터링 + 검색
	 List<Maintenance> searchMaintList(MaintSearch maintSearch);
	 int getSearchCount(MaintSearch maintSearch);

	 List<String> getAssetCategoryList();
	 List<String>getCompanyList();



}
