package app.domains.maintenance.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domains.maintenance.dao.MaintenanceRepository;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;


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

}
