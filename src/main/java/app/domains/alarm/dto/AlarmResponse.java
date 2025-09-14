package app.domains.alarm.dto;

import java.time.format.DateTimeFormatter;

import app.domains.alarm.model.Alarm;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AlarmResponse {
    private Long id;
    private Long assetId;
    private String type;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String isDeleted;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static AlarmResponse from(Alarm alarm) {
        if (alarm == null) {
            return null;
        }

        return AlarmResponse.builder()
                .id(alarm.getId())
                .assetId(alarm.getAssetId())
                .type(alarm.getType())
                .description(alarm.getDescription())
                .createdAt(alarm.getCreatedAt() != null ? alarm.getCreatedAt().format(DATE_TIME_FORMATTER) : null)
                .updatedAt(alarm.getUpdatedAt() != null ? alarm.getUpdatedAt().format(DATE_TIME_FORMATTER) : null)
                .deletedAt(alarm.getDeletedAt() != null ? alarm.getDeletedAt().format(DATE_TIME_FORMATTER) : null)
                .isDeleted(alarm.getIsDeleted())
                .build();
    }
}