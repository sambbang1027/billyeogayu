// 작성자 : 이원석
package app.domains.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryModelDto {
    private String category;
    private String modelName;
    private Long modelCount;
    
    public CategoryModelDto() {}
    
    public CategoryModelDto(String category, String modelName) {
        this.category = category;
        this.modelName = modelName;
    }
    
    public CategoryModelDto(String category, String modelName, Long modelCount) {
        this.category = category;
        this.modelName = modelName;
        this.modelCount = modelCount;
    }
}