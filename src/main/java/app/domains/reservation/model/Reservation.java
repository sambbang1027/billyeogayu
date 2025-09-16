// 작성자 : 이해든, 황요한
package app.domains.reservation.model;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {
    private Long reservationId;
    private Long assetId;
    private Long userId;
    private LocalDateTime startAt;      // -> START_TIME
    private LocalDateTime endAt;        // -> END_TIME
    private String purpose;

    // 화면에서 받는 원본 입력들
    private String useZipcode; // 폼에서 받음
    private String useAddr1;   // 시/도 + 시/군/구 + 나머지(보통 도로명+번지)
    private String useAddr2;   // 상세

    // DB 컬럼에 들어갈 값들
    private String address;       // ADDRESS         (예: "(06236) 서울 강남구 테헤란로 123 상세 101호")
    private String addressState;  // ADDRESS_STATE   (예: "서울특별시" 또는 "경기도")
    private String addressCity;   // ADDRESS_CITY    (예: "강남구" 혹은 "성남시 분당구")

    private String status;     // 'PENDING'
    private Date createdAt;    // DB DEFAULT
}