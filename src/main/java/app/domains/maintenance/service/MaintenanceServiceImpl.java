// 작성자 : 서샘이, 황요한
package app.domains.maintenance.service;

import java.math.BigDecimal;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domains.maintenance.dao.MaintenanceRepository;
import app.domains.maintenance.model.MaintDetail;
import app.domains.maintenance.model.MaintPart;
import app.domains.maintenance.model.MaintSearch;
import app.domains.maintenance.model.Maintenance;
import app.domains.maintenance.model.MaintenanceApply;
import app.domains.maintenance.model.MaintenanceComplete;
import app.domains.maintenance.model.MaintenanceEdit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	private MaintenanceRepository maintenanceRepository;

	// 리스트 조회 
	@Override
	public List<Maintenance> searchMaintList(MaintSearch maintSearch){
		return maintenanceRepository.searchMaintList(maintSearch);
	}

	@Override
	public int getSearchCount(MaintSearch maintSearch) {
		return maintenanceRepository.getSearchCount(maintSearch);
	}

	
	// 필터링 항목 

	@Override
	public List<String> getAssetCategoryList(){
		return maintenanceRepository.getAssetCategoryList();
	}

	@Override
	public List<String>getCompanyList() {
		return maintenanceRepository.getCompanyList();
	}
	
	@Override
	public List<Map<String, Object>> getPartList(int assetId){
		return maintenanceRepository.getPartList(assetId);
	}

	// 상세 정보 
	@Override
	public  MaintDetail getMaintDetail(int requestId) {
		
		return maintenanceRepository.getMaintDetail(requestId);
	}

	// 점검 완료 처리 
	@Transactional
	@Override
	public void completeMaintenance(MaintenanceComplete maintenanceComplete) {
		try {
		
			List<Map<String, Object>> ids = maintenanceRepository.findByAssetId(maintenanceComplete.getRequestId());
	
				
			// 점검 상태 변경 
			int updateMaint = maintenanceRepository.completeMaintenance(maintenanceComplete);
			if(updateMaint < 1) {
				throw new IllegalStateException("해당 ID에 대한 점검 내역이 없습니다");
			}
			
			// 부품 상태 변경 
			for(Map<String, Object> part : ids) {
				int partId = ((BigDecimal)part.get("PART_ID")).intValue();
				int updatePart = maintenanceRepository.updatePartStatusAfterMaintenance(partId);
				
				if(updatePart < 1 ) {
					throw new IllegalStateException("해당 부품을 찾을 수 없습니다.");
				}
			}
			
			// 자산 상태 변경 
			int assetId = ((BigDecimal)ids.get(0).get("ASSET_ID")).intValue();
			int  updateAsset= maintenanceRepository.updateAssetStatusAfterMaintenance(assetId);
		
			if(updateAsset < 1) {
				throw new IllegalStateException("해당 자산을 찾을 수 없습니다.");
			}
			
		} catch (Exception e) {
			log.error("점검 완료 처리 중 오류 발생 "+ e);
			throw e;
		}
	}
	
	
	// 기록 수정 
	@Transactional
	@Override
	public void updateRecord(MaintenanceEdit maintenanceEdit) {
		try {
				int updateRow = maintenanceRepository.updateRecord(maintenanceEdit);
				
				if(updateRow < 1) {
					throw new IllegalStateException("해당 ID에 대한 점검 내역이 없습니다");
				}
		} catch (Exception e) {
			log.error("기록 수정 중 오류 발생 "+ e);
			throw e;
		}
	}
	
	
	
	// 점검 신청
	@Transactional
	@Override
	public void applyMaintenance(MaintenanceApply maintenanceApply) {
		try {
				int requestId = maintenanceRepository.getNextRequestId();
				maintenanceApply.setRequestId(requestId);
				
				for (MaintPart part : maintenanceApply.getParts()) {
				    Map<String, Object> param = new HashMap<>();
				    param.put("assetId", maintenanceApply.getAssetId());
				    param.put("adminId", maintenanceApply.getAdminId());
				    param.put("partId", part.getPartId());
				    param.put("partName", part.getPartName());
				    param.put("type", maintenanceApply.getType());
				    param.put("resolverName", maintenanceApply.getResolverName());
				    param.put("requestId", maintenanceApply.getRequestId());

					int insertRow =   maintenanceRepository.applyMaintenance(param);
					
					if(insertRow < 1) {
						throw new IllegalStateException("점검 신청에 실패하였습니다");
					}
					
				}

				
				int updateAsset = maintenanceRepository.updateAssetStatus(maintenanceApply.getAssetId());
				if(updateAsset < 1 ) {
					throw new IllegalStateException("자산의 상태 변경 실패 ");
				}
				
		} catch (Exception e) {
			log.error("점검 신청 중 오류 발생 "+ e);
			throw e;
			
		}
	}
	
	
	@Override
	public	List<Maintenance>findForExport(String keyword,String assetKind,  String company, String status){
		return maintenanceRepository.findForExport(keyword,assetKind,company,status);
	}

}
