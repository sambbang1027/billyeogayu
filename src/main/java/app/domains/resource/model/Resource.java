package app.domains.resource.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Asset 엔티티 클래스
 * 매핑 테이블: ASSET
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
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


    // 기존 보유대수 필드 (DB에는 없고 계산해서 설정) - 하위호환성 유지
    private int stock = 1;

    // 새로운 재고 관련 필드들 (선택적 사용)
    private int availableStock = 0; // STATUS가 'AVAILABLE'인 자산 수량
    private int rentedStock = 0; // STATUS가 'AVAILABLE'이 아닌 자산 수량

    /**
     * 편의 프로퍼티 (JSP에서 사용)
     * availableStock이 0보다 크고 삭제되지 않은 경우에만 임대가능
     */
    public boolean isRentable() {
        return isDeleted == 0 && availableStock > 0;
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