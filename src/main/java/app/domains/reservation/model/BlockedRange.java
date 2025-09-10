package app.domains.reservation.model;

import lombok.*;
import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlockedRange {
    private Date startAt;
    private Date endAt;
}
