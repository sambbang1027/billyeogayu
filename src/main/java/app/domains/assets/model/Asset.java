package app.domains.assets.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    private long assetId;
    private String modelName;
    private String assetStatus;
    private String category;
    private String company;
    private long maintenanceCycle;
    private long usageTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMaintenanceDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedMaintenanceDate;
    private String imagePath;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;
    private String isDeleted;

    public AssetDto toDto() {
        return AssetDto.builder()
                .assetId(assetId)
                .modelName(modelName)
                .assetStatus(assetStatus)
                .category(category)
                .company(company)
                .maintenanceCycle(maintenanceCycle)
                .usageTime(usageTime)
                .lastMaintenanceDate(lastMaintenanceDate)
                .expectedMaintenanceDate(expectedMaintenanceDate)
                .imagePath(imagePath)
                .build();
    }
}
