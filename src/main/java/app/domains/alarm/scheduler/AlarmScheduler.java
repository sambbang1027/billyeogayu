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
        System.out.println("=========Checking asset maintenance...");
        try {
            boolean isAlarmGenerated = alarmService.createAssetMaintenanceAlarms();
            if (isAlarmGenerated) {
                log.info("[알람 - 자산 정기점검] alarms created and asset status updated");
            } else {
                log.debug("[알람 - 자산 정기점검] No assets require maintenance alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] AssetMaintenance check failed", e);
        }
    }
    
    @Async
    public void checkPartReplace() {
        System.out.println("========Checking part replace...");
        try {
            boolean isAlarmGenerated = alarmService.createPartReplaceAlarms();
            if (isAlarmGenerated) {
                log.info("[알람 - 부품 교체] alarms created and part status updated");
            } else {
                log.debug("[알람 - 부품 교체] No parts require replace alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] PartReplace check failed", e);
        }
    }
    
    @Async
    public void checkReservationOverdue() {
        System.out.println("========Checking reservation overdue...");
        try {
            boolean isAlarmGenerated = alarmService.createReservationOverdueAlarms();
            if (isAlarmGenerated) {
                log.info("[알람 - 예약 연체] alarms created");
            } else {
                log.debug("[알람 - 예약 연체] No reservations require overdue alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] ReservationOverdue check failed", e);
        }
    }
    
    @Async
    public void checkMaintenanceLeaved() {
        System.out.println("========Checking maintenance leaved...");
        try {
            boolean isAlarmGenerated = alarmService.createMaintenanceLeavedAlarms();
            if (isAlarmGenerated) {
                log.info("[알람 - 점검 방치] alarms created");
            } else {
                log.debug("[알람 - 점검 방치] No maintenances require leaved alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] MaintenanceLeaved check failed", e);
        }
    }
}