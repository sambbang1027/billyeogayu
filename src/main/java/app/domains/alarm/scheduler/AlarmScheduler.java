// 작성자 : 이원석
package app.domains.alarm.scheduler;

import app.domains.alarm.service.AlarmService;
import java.time.LocalDateTime;
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
    
    /**
     * 1분마다 실행되는 알람 스케줄러
     */
    @Scheduled(fixedRate = 60000) // 1분 = 60,000ms
    public void runAlarmScheduler() {
        log.info("[ALARM_SCHEDULER] Started at {}", LocalDateTime.now());
        
        try {
            // 4개 알람 체크를 병렬로 실행
            checkAssetMaintenance();
            checkPartReplace();
            checkReservationOverdue();
            checkMaintenanceLeaved();
            
            log.info("[ALARM_SCHEDULER] Completed at {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] Error occurred during execution", e);
        }
    }
    
    @Async
    public void checkAssetMaintenance() {
        try {
            boolean isAlarmGenerated = alarmService.createAssetMaintenanceAlarms();
            if (isAlarmGenerated) {
                log.info("[ALARM_SCHEDULER - 자산 정기점검 도래] alarms generated, status updated");
            } else {
                log.info("[ALARM_SCHEDULER - 자산 정기점검 도래] No assets requires alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] AssetMaintenance check failed", e);
        }
    }
    
    @Async
    public void checkPartReplace() {
        try {
            boolean isAlarmGenerated = alarmService.createPartReplaceAlarms();
            if (isAlarmGenerated) {
                log.info("[ALARM_SCHEDULER - 부품 교체 도래] alarm generated, status updated");
            } else {
                log.info("[ALARM_SCHEDULER - 부품 교체 도래] No data requires alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] PartReplace check failed", e);
        }
    }
    
    @Async
    public void checkReservationOverdue() {
        try {
            boolean isAlarmGenerated = alarmService.createReservationOverdueAlarms();
            if (isAlarmGenerated) {
                log.info("[ALARM_SCHEDULER - 예약 연체] alarms generated");
            } else {
                log.info("[ALARM_SCHEDULER - 예약 연체] No data requires alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] ReservationOverdue check failed", e);
        }
    }
    
    @Async
    public void checkMaintenanceLeaved() {
        try {
            boolean isAlarmGenerated = alarmService.createMaintenanceLeavedAlarms();
            if (isAlarmGenerated) {
                log.info("[ALARM_SCHEDULER - 점검 방치] alarms generated");
            } else {
                log.info("[ALARM_SCHEDULER - 점검 방치] No data requires alarm");
            }
        } catch (Exception e) {
            log.error("[ALARM_SCHEDULER] MaintenanceLeaved check failed", e);
        }
    }
}