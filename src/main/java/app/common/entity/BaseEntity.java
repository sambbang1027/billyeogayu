package app.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity {
    public static final String LOCAL_DATE_TIMME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String isDeleted = "N";
    
    /**
     * Soft delete 처리
     */
    public void softDelete() {
        this.isDeleted = "Y";
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 삭제 상태 확인
     */
    public boolean isDeleted() {
        return "Y".equals(this.isDeleted);
    }
    
    /**
     * 삭제 취소 (복구)
     */
    public void restore() {
        this.isDeleted = "N";
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    // BaseEntity의 LocalDateTime 필드들에 JSON 포맷 지정
    @JsonFormat(pattern = LOCAL_DATE_TIMME_FORMAT)
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @JsonFormat(pattern = LOCAL_DATE_TIMME_FORMAT)
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @JsonFormat(pattern = LOCAL_DATE_TIMME_FORMAT)
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }
}