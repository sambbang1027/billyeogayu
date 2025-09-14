package app.domains.alarm.service;

import app.domains.alarm.dto.AlarmResponseDto;
import java.util.List;

public interface AlarmService {
    List<AlarmResponseDto> findAll();

    void markAsRead(Long alarmId);

    int getUnreadAlarmCount();

    boolean createAssetMaintenanceAlarms();

    boolean createPartReplaceAlarms();

    boolean createReservationOverdueAlarms();

    boolean createMaintenanceLeavedAlarms();

}