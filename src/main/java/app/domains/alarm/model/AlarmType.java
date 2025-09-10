package app.domains.alarm.model;

import lombok.Getter;

@Getter
public enum AlarmType {
    ASSET_REGULAR_MAINTENANCE("자산 정기점검 도래 알림"),
    PART_REGULAR_REPLACE("부품 정기점검 도래 알림"),
    RESERVATION_OVERDUE("예약 연체 알림"),
    ASSET_MAINTENANCE_LEAVED("자산 점검 방치 알림"),
    ;

    private final String description;

    public String toDescription() {
        return this.getDescription();
    }

    AlarmType(String description) {
        this.description = description;
    }
}