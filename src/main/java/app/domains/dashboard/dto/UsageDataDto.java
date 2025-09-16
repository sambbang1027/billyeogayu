// 작성자 : 서샘이, 이원석
package app.domains.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsageDataDto {
    private int year;
    private long reservationCount;
}