package app.domains.asset.model;

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
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime expectedMaintenanceDate;
    private String imagePath;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int isDeleted;
}
