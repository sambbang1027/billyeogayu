package app.domains.alarm.model;

import lombok.Getter;

@Getter
public enum AlarmType {
    ASSET_REGULAR_MAINTENANCE("자산 정기점검일 도래"),
    PART_REGULAR_REPLACE("부품 정기교체일 도래"),
    RESERVATION_OVERDUE("예약 연체"),
    ASSET_MAINTENANCE_LEAVED("자산 점검 기한일 경과"),
    ;

    private final String description;

    public String toDescription() {
        return this.getDescription();
    }

    AlarmType(String description) {
        this.description = description;
    }
}