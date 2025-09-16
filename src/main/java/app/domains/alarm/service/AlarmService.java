// 작성자 : 황요한, 이원석
package app.domains.alarm.service;

import app.domains.alarm.dto.AlarmResponse;
import java.util.List;

public interface AlarmService {
    List<AlarmResponse> findAll();

    void markAsRead(Long alarmId);

    int getUnreadAlarmCount();

    boolean createAssetMaintenanceAlarms();

    boolean createPartReplaceAlarms();

    boolean createReservationOverdueAlarms();

    boolean createMaintenanceLeavedAlarms();

}