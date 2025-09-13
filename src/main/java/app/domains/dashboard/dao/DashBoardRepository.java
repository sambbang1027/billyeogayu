package app.domains.dashboard.dao;

import app.domains.dashboard.dto.CategoryDataDto;
import app.domains.dashboard.dto.CategoryModelDto;
import org.apache.ibatis.annotations.Mapper;
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
}
