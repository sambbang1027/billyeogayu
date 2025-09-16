// 작성자 : 이원석
package app.domains.alarm.model;

import app.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Alarm extends BaseEntity {
    private Long id;            // alarm_id (PK)
    private Long assetId;       // asset_id (FK)
    private String type;        // TODO refactor: ENUM 매핑 방법
    private String description;
}
