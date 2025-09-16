// 작성자 : 이원석
package app.domains.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDataDto {
    private String category;
    private Long totalCount;
    private Long maintenanceRequiredCount;
    
    public CategoryDataDto() {}

    public CategoryDataDto(String category, Long totalCount, Long maintenanceRequiredCount) {
        this.category = category;
        this.totalCount = totalCount;
        this.maintenanceRequiredCount = maintenanceRequiredCount;
    }
    
    // 검증 메소드
    public boolean isValid() {
        return category != null && totalCount != null && maintenanceRequiredCount != null;
    }
    
    // 안전한 getter (null 체크)
    public Long getTotalCount() {
        return totalCount != null ? totalCount : 0L;
    }
    
    public Long getMaintenanceRequiredCount() {
        return maintenanceRequiredCount != null ? maintenanceRequiredCount : 0L;
    }
}