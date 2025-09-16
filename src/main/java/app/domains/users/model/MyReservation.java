// 작성자 : 황요한
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
    private String status; 
    private String address;
    private String addressState;
    private String addressCity;
    private Date createdAt;
    
    // 사용 관련 정보 (새로 추가)
    private Date completedAt;
    private Integer usageDuration;  // 분 단위
    private Long adminId;
    private Date returnedAt;
    private String isRejected;  
    private String rejectReason;
    
    // 자산 정보 (JOIN)
    private String assetName;
    private String assetCategory;
    private String assetCompany;
    private String assetImage;
    
    // 관리자 정보 (JOIN)
    private String adminName;
    
    // 상태 표시용 메서드

    public String getStatusText() {
        // 사용자 취소인 경우 (새로 추가)
        if ("Y".equals(isRejected) && "사용자 취소".equals(rejectReason)) {
            return "취소됨";
        }
        
        // 관리자 거절인 경우
        if ("Y".equals(isRejected)) {
            return "거절됨";
        }
        
        switch (status) {
            case "PENDING": return "승인 대기";
            case "APPROVED": 
                if (returnedAt != null) {
                    return "반납 완료";
                } else if (completedAt != null) {
                    return "사용 완료";
                } else if (isCurrentlyInUse()) {
                    return "사용 중";
                } else {
                    return "승인됨";
                }
            case "REJECTED": return "거절됨";
            case "CANCELLED": return "취소됨";
            case "COMPLETED": return "사용 완료";  // 추가된 케이스
            default: return status;
        }
    }
    
    public String getStatusClass() {
        // 사용자 취소인 경우
        if ("Y".equals(isRejected) && "사용자 취소".equals(rejectReason)) {
            return "status-cancelled";
        }
        
        // 관리자 거절인 경우
        if ("Y".equals(isRejected)) {
            return "status-rejected";
        }
        
        if ("APPROVED".equals(status)) {
            if (returnedAt != null) {
                return "status-completed";
            } else if (completedAt != null) {
                return "status-used";
            } else if (isCurrentlyInUse()) {
                return "status-active";
            } else {
                return "status-approved";
            }
        }
        
        // CANCELLED 상태 처리 추가
        if ("CANCELLED".equals(status)) {
            return "status-cancelled";
        }
        
        // COMPLETED 상태 처리 추가
        if ("COMPLETED".equals(status)) {
            return "status-completed";
        }
        
        return "status-" + (status != null ? status.toLowerCase() : "unknown");
    }
    
    // 취소 가능 여부 체크
    public boolean isCancellable() {
        return "PENDING".equals(status) && !"Y".equals(isRejected);
    }
    
    // 현재 사용 중인지 체크
    public boolean isCurrentlyInUse() {
        if (!"APPROVED".equals(status) || startTime == null || endTime == null) {
            return false;
        }
        
        Date now = new Date();
        return now.after(startTime) && now.before(endTime) && completedAt == null;
    }
    
    // 사용 완료 여부
    public boolean isCompleted() {
        return completedAt != null || returnedAt != null;
    }
    
    // 사용 내역으로 간주할지 여부 (사용이 시작되었거나 완료된 경우)
    public boolean isUsageHistory() {
        return "APPROVED".equals(status) && 
               (completedAt != null || returnedAt != null || isCurrentlyInUse());
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
    
    // 사용 기간 포맷팅
    public String getFormattedUsageDuration() {
        if (usageDuration == null || usageDuration == 0) {
            return "-";
        }
        
        int hours = usageDuration / 60;
        int minutes = usageDuration % 60;
        
        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        } else {
            return String.format("%d분", minutes);
        }
    }
    
    // 실제 사용 시간 계산 (완료 시각과 시작 시각의 차이)
    public String getActualUsageTime() {
        if (startTime == null || completedAt == null) {
            return "-";
        }
        
        long diffMs = completedAt.getTime() - startTime.getTime();
        long diffMinutes = diffMs / (1000 * 60);
        
        if (diffMinutes < 0) return "-";
        
        int hours = (int) (diffMinutes / 60);
        int minutes = (int) (diffMinutes % 60);
        
        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        } else {
            return String.format("%d분", minutes);
        }
    }
    
    public boolean isOverdue() {
        if (!"APPROVED".equals(status) || endTime == null || completedAt != null) {
            return false;
        }
        
        Date now = new Date();
        return now.after(endTime);
    }
}