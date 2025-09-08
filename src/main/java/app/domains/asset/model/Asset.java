package app.domains.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Asset 엔티티 클래스
 * 매핑 테이블: ASSET
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    private long assetId;                  // ASSET_ID (PK)
    private String name;                   // NAME
    private String category;               // CATEGORY
    private String company;                // COMPANY
    private String status;                 // STATUS (AVAILABLE / UNAVAILABLE 등)
    private String image;                  // IMAGE (이미지 경로)

    private long maintenanceCycle;         // MAINTENANCE_CYCLE
    private long usageTime;                // USAGE_TIME

    private LocalDateTime lastMaintenanceDate;     // LAST_MAINTENANCE_DATE
    private LocalDateTime expectedMaintenanceDate; // EXPECTED_MAINTENANCE_DATE

    private LocalDateTime createdAt;       // CREATED_AT
    private LocalDateTime updatedAt;       // UPDATED_AT
    private LocalDateTime deletedAt;       // DELETED_AT

    private String isDeleted;              // IS_DELETED ('Y'/'N')


    /** 편의 프로퍼티 (JSP에서 사용) */
    public boolean isRentable() {
        return "AVAILABLE".equalsIgnoreCase(status) && "N".equalsIgnoreCase(isDeleted);
    }

    public int getStock() {
        // 보유 대수 대신 여기서는 항상 1대 (추후 확장 가능)
        return 1;
    }
}
