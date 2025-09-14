package app.domains.dashboard.dao;

import app.domains.dashboard.dto.CategoryDataDto;
import app.domains.dashboard.dto.CategoryModelDto;
import app.domains.dashboard.dto.UsageDataDto;
import app.domains.dashboard.dto.InspectionDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DashBoardRepository {
    Long getTotalAssetCount();
    Long getAssetCountByStatus(String status);
    List<CategoryDataDto> getAssetCountByCategory();
    
    // 사용량 차트 필터 데이터
    List<String> getAssetCategories();
    List<CategoryModelDto> getAssetModelsByCategory();
    List<String> getReservationAddresses();
    
    /**
     * 예약 지역별 상위 7개 도시 조회 (예약 수 기준 내림차순)
     */
    List<String> getTopCitiesByReservationCount();
    
    // 사용량 차트 데이터
    List<UsageDataDto> getUsageDataByFilters(@Param("category") String category, 
                                            @Param("model") String model, 
                                            @Param("address") String address);
    
    // 점검 차트 데이터
    List<InspectionDataDto> getInspectionDataByFilters(@Param("category") String category, 
                                                      @Param("model") String model);
}
