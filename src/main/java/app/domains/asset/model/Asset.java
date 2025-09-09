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

    private long assetId; // ASSET_ID (PK)
    private String name; // NAME
    private String category; // CATEGORY
    private String company; // COMPANY
    private String status; // STATUS (AVAILABLE / UNAVAILABLE 등)
    private String image; // IMAGE (이미지 경로)

    private long maintenanceCycle; // MAINTENANCE_CYCLE
    private long usageTime; // USAGE_TIME

    private LocalDateTime lastMaintenanceDate; // LAST_MAINTENANCE_DATE
    private LocalDateTime expectedMaintenanceDate; // EXPECTED_MAINTENANCE_DATE

    private LocalDateTime createdAt; // CREATED_AT
    private LocalDateTime updatedAt; // UPDATED_AT
    private LocalDateTime deletedAt; // DELETED_AT

    private String isDeleted; // IS_DELETED ('Y'/'N')

    // 기존 보유대수 필드 (DB에는 없고 계산해서 설정) - 하위호환성 유지
    private int stock = 1;
    
    // 새로운 재고 관련 필드들 (선택적 사용)
    private int availableStock = 0;  // 실제 임대 가능한 수량
    private int rentedStock = 0;     // 현재 대여중인 수량

    /** 편의 프로퍼티 (JSP에서 사용) */
    public boolean isRentable() {
        // availableStock이 설정되어 있으면 그것을 우선 사용
        if (availableStock >= 0) {  // availableStock이 설정된 경우
            return "AVAILABLE".equalsIgnoreCase(status) && "N".equalsIgnoreCase(isDeleted) && availableStock > 0;
        } else {  // availableStock이 설정되지 않은 경우 (기존 로직)
            return "AVAILABLE".equalsIgnoreCase(status) && "N".equalsIgnoreCase(isDeleted);
        }
    }
    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    // 새로운 재고 관련 메서드들
    public int getAvailableStock() {
        return this.availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getRentedStock() {
        return this.rentedStock;
    }

    public void setRentedStock(int rentedStock) {
        this.rentedStock = rentedStock;
    }
    
    // 전체 재고 수량 (기존 stock과 동일하지만 명확한 의미)
    public int getTotalStock() {
        return this.stock;
    }

    public void setTotalStock(int totalStock) {
        this.stock = totalStock;
    }
    
    // 재고 상태 체크 편의 메서드
    public boolean hasAvailableStock() {
        return availableStock > 0;
    }
    
    public boolean isOutOfStock() {
        return availableStock == 0;
    }
}