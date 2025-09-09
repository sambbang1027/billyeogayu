package app.domains.maintenance.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domains.maintenance.dao.MaintenanceRepository;
import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;


@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	private MaintenanceRepository maintenanceRepository;
	
	
	
	@Override
	public List<Maintenance> searchMaintList(MaintSearch maintSearch){
		
		return maintenanceRepository.searchMaintList(maintSearch);
	}
	
	@Override
	public int getSearchCount(MaintSearch maintSearch) {
		return maintenanceRepository.getSearchCount(maintSearch);
	}
	
	@Override
	public List<String> getAssetCategoryList(){
		return maintenanceRepository.getAssetCategoryList();
	}
	
	@Override
	public List<String>getCompanyList() {
		return maintenanceRepository.getCompanyList();
	}

	@Override
	public  MaintDetail getMaintDetail(int requestId) {
		
		return maintenanceRepository.getMaintDetail(requestId);
	}

	@Transactional
	@Override
	public void completeMaintenance(MaintenanceComplete maintenanceComplete) {
		try {
			int updateRow = maintenanceRepository.completeMaintenance(maintenanceComplete);
			
			if(updateRow < 1) {
				throw new IllegalStateException("해당 ID에 대한 점검 내역이 없습니다");
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Transactional
	@Override
	public void updateRecord(MaintenanceEdit maintenanceEdit) {
		try {
				int updateRow = maintenanceRepository.updateRecord(maintenanceEdit);
				
				if(updateRow < 1) {
					throw new IllegalStateException("해당 ID에 대한 점검 내역이 없습니다");
				}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
