// 작성자 : 황요한
package app.domains.users.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 내 사용 내역 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUsageHistory {
    
    // 사용 로그 기본 정보
    private Long usageLogId;
    private Long reservationId;
    private Long userId;
    private Long assetId;
    private Date startTime;
    private Date endTime;
    private Integer usageDuration; // 분 단위
    private String usageStatus;    // STARTED, COMPLETED, CANCELLED
    private Long processedBy;      // 처리자 ID
    private Date createdAt;
    private Date updatedAt;
    
    // 예약 정보 (JOIN)
    private String purpose;
    private String reservationStatus;
    
    // 자산 정보 (JOIN)
    private String assetName;
    private String assetCategory;
    private String assetCompany;
    private String assetImage;
    
    // 처리자 정보 (JOIN)
    private String processedByName;
    
    // 사용 시간 포맷팅
    public String getFormattedDuration() {
        if (usageDuration == null) return "-";
        
        int hours = usageDuration / 60;
        int minutes = usageDuration % 60;
        
        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        } else {
            return String.format("%d분", minutes);
        }
    }
    
    // 상태 텍스트
    public String getUsageStatusText() {
        switch (usageStatus != null ? usageStatus : "") {
            case "STARTED": return "사용 시작";
            case "COMPLETED": return "사용 완료";
            case "CANCELLED": return "사용 취소";
            default: return usageStatus;
        }
    }
    
    public String getUsageStatusClass() {
        return "usage-status-" + (usageStatus != null ? usageStatus.toLowerCase() : "unknown");
    }
    
    // 사용 완료 여부
    public boolean isCompleted() {
        return "COMPLETED".equals(usageStatus);
    }
    
    // 사용 기간 계산 (실제 사용 시간과 예약 시간 비교)
    public String getActualUsagePeriod() {
        if (startTime == null || endTime == null) return "-";
        
        long diffMs = endTime.getTime() - startTime.getTime();
        long diffMinutes = diffMs / (1000 * 60);
        
        int hours = (int) (diffMinutes / 60);
        int minutes = (int) (diffMinutes % 60);
        
        if (hours > 0) {
            return String.format("%d시간 %d분", hours, minutes);
        } else {
            return String.format("%d분", minutes);
        }
    }
}