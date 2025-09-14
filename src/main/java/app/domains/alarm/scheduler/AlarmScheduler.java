package app.domains.alarm.scheduler;

import app.domains.alarm.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@EnableAsync
@Slf4j
public class AlarmScheduler {
    @Autowired
    private AlarmService alarmService;

    public AlarmScheduler() {
        System.out.println("==================AlarmScheduler Bean created!");
    }
    
    /**
     * 1분마다 실행되는 알람 스케줄러
     */
    @Scheduled(fixedRate = 60000) // 1분 = 60,000ms
    public void runAlarmScheduler() {
        System.out.println("Running alarm scheduler===========================");
        log.info("[ALARM_SCHEDULER] Started at {}", java.time.LocalDateTime.now());
        
        try {
            // 4개 알람 체크를 병렬로 실행
            checkAssetMaintenance();
            checkPartReplace();
            checkReservationOverdue();
            checkMaintenanceLeaved();
            
            log.info("[ALARM_SCHEDULER] Completed at {}", java.time.LocalDateTime.now());
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] Error occurred during execution", e);
        }
    }
    
    @Async
    public void checkAssetMaintenance() {
        try {
            var targetAssets = alarmService.getAssetsForMaintenanceAlarm();
            if (!targetAssets.isEmpty()) {
                alarmService.createAssetMaintenanceAlarms(targetAssets);
                log.info("[알람 - 자산 정기점검] {} alarms created and asset status updated", targetAssets.size());
            } else {
                log.debug("[알람 - 자산 정기점검] No assets require maintenance alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] AssetMaintenance check failed", e);
        }
    }
    
    @Async
    public void checkPartReplace() {
        try {
            var targetParts = alarmService.getPartsForReplaceAlarm();
            if (!targetParts.isEmpty()) {
                alarmService.createPartReplaceAlarms(targetParts);
                log.info("[알람 - 부품 교체] {} alarms created and part status updated", targetParts.size());
            } else {
                log.debug("[알람 - 부품 교체] No parts require replace alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] PartReplace check failed", e);
        }
    }
    
    @Async
    public void checkReservationOverdue() {
        try {
            var targetReservations = alarmService.getReservationsForOverdueAlarm();
            if (!targetReservations.isEmpty()) {
                alarmService.createReservationOverdueAlarms(targetReservations);
                log.info("[알람 - 예약 연체] {} alarms created", targetReservations.size());
            } else {
                log.debug("[알람 - 예약 연체] No reservations require overdue alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] ReservationOverdue check failed", e);
        }
    }
    
    @Async
    public void checkMaintenanceLeaved() {
        try {
            var targetMaintenances = alarmService.getMaintenancesForLeavedAlarm();
            if (!targetMaintenances.isEmpty()) {
                alarmService.createMaintenanceLeavedAlarms(targetMaintenances);
                log.info("[알람 - 점검 방치] {} alarms created", targetMaintenances.size());
            } else {
                log.debug("[알람 - 점검 방치] No maintenances require leaved alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] MaintenanceLeaved check failed", e);
        }
    }
}