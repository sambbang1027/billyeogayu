package app.domains.alarm.dto;

public record MaintenanceLeavedAlarm(Long maintenanceId, Long assetId, String assetCategory, String assetName) {
}