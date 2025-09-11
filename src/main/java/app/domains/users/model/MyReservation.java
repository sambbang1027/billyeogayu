package app.domains.users.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 내 예약 내역 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyReservation {
    
    // 예약 기본 정보
    private Long reservationId;
    private Long userId;
    private Long assetId;
    private Date startTime;
    private Date endTime;
    private String purpose;
    private String status;  // PENDING, APPROVED, REJECTED, CANCELLED, ACTIVE, COMPLETED
    private String address;
    private String addressState;
    private String addressCity;
    private Date createdAt;
    private Date updatedAt;
    
    // 자산 정보 (JOIN)
    private String assetName;
    private String assetCategory;
    private String assetCompany;
    private String assetImage;
    
    // 상태 표시용 메서드
    public String getStatusText() {
        switch (status) {
            case "PENDING": return "승인 대기";
            case "APPROVED": return "승인됨";
            case "REJECTED": return "거절됨";
            case "CANCELLED": return "취소됨";
            case "ACTIVE": return "사용 중";
            case "COMPLETED": return "완료됨";
            default: return status;
        }
    }
    
    public String getStatusClass() {
        return "status-" + (status != null ? status.toLowerCase() : "unknown");
    }
    
    // 취소 가능 여부 체크
    public boolean isCancellable() {
        return "PENDING".equals(status);
    }
    
    // 사용 가능 여부 체크 (승인됨 + 시작시간 전)
    public boolean isUsable() {
        return "APPROVED".equals(status) && startTime != null && startTime.after(new Date());
    }
    
    // 완전한 주소 반환
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressState != null && !addressState.trim().isEmpty()) {
            sb.append(addressState).append(" ");
        }
        if (addressCity != null && !addressCity.trim().isEmpty()) {
            sb.append(addressCity).append(" ");
        }
        if (address != null && !address.trim().isEmpty()) {
            sb.append(address);
        }
        return sb.toString().trim();
    }
}