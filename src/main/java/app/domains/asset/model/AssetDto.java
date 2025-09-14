package app.domains.asset.model;

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
public class AssetDto {
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
    private String location;
    private String owner;

    // 변환된 날짜 문자열 Getter
    public String getExpectedMaintenanceDateFormatted() {
        return expectedMaintenanceDate != null
                ? expectedMaintenanceDate.toLocalDate().toString()  // yyyy-MM-dd 형식
                : "--";
    }

    public String getLastMaintenanceDateFormatted() {
        return lastMaintenanceDate != null
                ? lastMaintenanceDate.toLocalDate().toString()
                : "--";
    }
}
