// 작성자 : 이해든, 황요한
package app.domains.reservation.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlockedRange {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
