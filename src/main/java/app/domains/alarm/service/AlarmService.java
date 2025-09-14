package app.domains.alarm.service;

import app.domains.alarm.dto.AlarmResponseDto;
import app.domains.alarm.dto.AssetMaintenanceAlarm;
import app.domains.alarm.dto.PartReplaceAlarm;
import app.domains.alarm.dto.ReservationOverdueAlarm;
import app.domains.alarm.dto.MaintenanceLeavedAlarm;
import java.util.List;

public interface AlarmService {
    List<AlarmResponseDto> findAll();

    void markAsRead(Long alarmId);

    int getUnreadAlarmCount();

    void createAssetMaintenanceAlarms(List<AssetMaintenanceAlarm> assets);

    void createPartReplaceAlarms(List<PartReplaceAlarm> parts);

    void createReservationOverdueAlarms(List<ReservationOverdueAlarm> reservations);

    void createMaintenanceLeavedAlarms(List<MaintenanceLeavedAlarm> maintenances);

    // 알람 대상 조회 메서드들
    List<AssetMaintenanceAlarm> getAssetsForMaintenanceAlarm();

    List<PartReplaceAlarm> getPartsForReplaceAlarm();

    List<ReservationOverdueAlarm> getReservationsForOverdueAlarm();

    List<MaintenanceLeavedAlarm> getMaintenancesForLeavedAlarm();

}