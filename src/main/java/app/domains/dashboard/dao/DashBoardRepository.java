package app.domains.dashboard.dao;

import app.domains.dashboard.dto.CategoryDataDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DashBoardRepository {
    Long getTotalAssetCount();
    Long getAssetCountByStatus(String status);
    List<CategoryDataDto> getAssetCountByCategory();
}
