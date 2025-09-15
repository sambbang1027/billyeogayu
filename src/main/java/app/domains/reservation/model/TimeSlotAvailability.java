package app.domains.reservation.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TimeSlotAvailability {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int availableCount;  // 해당 시간대에 사용 가능한 자산 수
    private int totalCount;      // 총 자산 수
    
    /**
     * 완전히 사용 가능한지 체크
     */
    public boolean isFullyAvailable() {
        return availableCount == totalCount;
    }
    
    /**
     * 부분적으로 사용 가능한지 체크
     */
    public boolean isPartiallyAvailable() {
        return availableCount > 0 && availableCount < totalCount;
    }
    
    /**
     * 완전히 사용 불가능한지 체크
     */
    public boolean isUnavailable() {
        return availableCount == 0;
    }
}