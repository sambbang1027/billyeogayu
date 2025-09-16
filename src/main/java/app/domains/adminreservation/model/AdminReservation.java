// 작성자 : 김민호
package app.domains.adminreservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReservation {
    private Long reservationId;
    private Long userId;
    private Long assetId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String status;
    private String address;
    private String addressState;
    private String addressCity;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Long usageDuration;
    private Long adminId;
    private String isRejected;
    private String rejectReason;
}
