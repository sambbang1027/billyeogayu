package app.domains.alarm.service;

import app.domains.alarm.dto.AssetMaintenanceAlarm;
import app.domains.alarm.dto.PartReplaceAlarm;
import app.domains.alarm.dto.ReservationOverdueAlarm;
import app.domains.alarm.dto.MaintenanceLeavedAlarm;
import app.domains.alarm.model.AlarmType;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domains.alarm.dao.AlarmRepository;
import app.domains.alarm.dto.AlarmResponseDto;
import app.domains.alarm.model.Alarm;

@Service
@Slf4j
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    private AlarmRepository alarmRepository;

    @Override
    public List<AlarmResponseDto> findAll() {
        List<Alarm> alarms = alarmRepository.findAll();
        return alarms.stream()
                .map(AlarmResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long alarmId) {
        alarmRepository.markAsRead(alarmId);
    }

    @Override
    public int getUnreadAlarmCount() {
        return alarmRepository.getUnreadCount();
    }

    private static final String ASSET_MAINTENANCE_CONTENT = "%s %s-%d의 정기점검 주기가 5일 남았습니다.";
    private static final String PART_REPLACE_CONTENT = "부품 %s-%d의 정기교체 주기가 5일 남았습니다.";
    private static final String RESERVATION_OVERDUE_CONTENT = "예약 %d이(가) 반납 기한 20일 이상 연체되었습니다.";
    private static final String MAINTENANCE_LEAVED_CONTENT = "%s %s-%d의 점검 %d이 30일 이상 소요되고 있습니다.";

    /**
     * 자산 정기점검 알람 생성 및 상태 업데이트
     */
    @Override
    @Transactional
    public void createAssetMaintenanceAlarms(List<AssetMaintenanceAlarm> assets) {
        for (AssetMaintenanceAlarm asset : assets) {
            // 알람 생성
            String description = String.format("[%s]\n%s",
                    AlarmType.ASSET_REGULAR_MAINTENANCE.toDescription(),
                    String.format(ASSET_MAINTENANCE_CONTENT, asset.category(), asset.name(), asset.assetId()));

            Alarm alarm = Alarm.builder()
                    .assetId(asset.assetId())
                    .type(AlarmType.ASSET_REGULAR_MAINTENANCE.name())
                    .description(description)
                    .build();

            alarmRepository.insert(alarm);

            // 자산 상태 업데이트
            updateAssetStatus(asset.assetId());
        }
    }

    /**
     * 자산 상태를 MAINTENANCE_REQUIRED로 업데이트
     *
     */
    public void updateAssetStatus(Long assetId) {
        alarmRepository.updateAssetStatusToMaintenanceRequired(assetId);
    }

    /**
     * 정기점검 알람 대상 자산 조회
     */
    @Override
    public List<AssetMaintenanceAlarm> getAssetsForMaintenanceAlarm() {
        return alarmRepository.getAssetsForMaintenanceAlarm();
    }




    /**
     * 부품 교체 알람 생성 및 상태 업데이트
     */
    @Override
    @Transactional
    public void createPartReplaceAlarms(List<PartReplaceAlarm> parts) {
        for (PartReplaceAlarm part : parts) {
            // 알람 생성
            String description = String.format("[%s]\n%s",
                    AlarmType.PART_REGULAR_REPLACE.toDescription(),
                    String.format(PART_REPLACE_CONTENT, part.name(), part.partId()));

            Alarm alarm = Alarm.builder()
                    .assetId(part.assetId())
                    .type(AlarmType.PART_REGULAR_REPLACE.name())
                    .description(description)
                    .build();

            alarmRepository.insert(alarm);

            // 부품 상태 업데이트
            updatePartStatus(part.partId());
        }
    }

    /**
     * 부품 상태를 UNAVAILABLE로 업데이트
     */
    public void updatePartStatus(Long partId) {
        alarmRepository.updatePartStatusToUnavailable(partId);
    }

    /**
     * 예약 연체 알람 생성
     */
    @Override
    @Transactional
    public void createReservationOverdueAlarms(List<ReservationOverdueAlarm> reservations) {
        for (ReservationOverdueAlarm reservation : reservations) {
            // 알람 생성
            String description = String.format("[%s]\n%s",
                    AlarmType.RESERVATION_OVERDUE.toDescription(),
                    String.format(RESERVATION_OVERDUE_CONTENT, reservation.reservationId()));

            Alarm alarm = Alarm.builder()
                    .assetId(reservation.assetId())
                    .type(AlarmType.RESERVATION_OVERDUE.name())
                    .description(description)
                    .build();

            alarmRepository.insert(alarm);
        }
    }

    /**
     * 점검 방치 알람 생성
     */
    @Override
    @Transactional
    public void createMaintenanceLeavedAlarms(List<MaintenanceLeavedAlarm> maintenances) {
        for (MaintenanceLeavedAlarm maintenance : maintenances) {
            // 알람 생성
            String description = String.format("[%s]\n%s",
                    AlarmType.ASSET_MAINTENANCE_LEAVED.toDescription(),
                    String.format(MAINTENANCE_LEAVED_CONTENT,
                            maintenance.assetCategory(),
                            maintenance.assetName(),
                            maintenance.assetId(),
                            maintenance.maintenanceId()));

            Alarm alarm = Alarm.builder()
                    .assetId(maintenance.assetId())
                    .type(AlarmType.ASSET_MAINTENANCE_LEAVED.name())
                    .description(description)
                    .build();

            alarmRepository.insert(alarm);
        }
    }

    /**
     * 알람 대상 조회 메서드들
     */
    @Override
    public List<PartReplaceAlarm> getPartsForReplaceAlarm() {
        return alarmRepository.getPartsForReplaceAlarm();
    }

    @Override
    public List<ReservationOverdueAlarm> getReservationsForOverdueAlarm() {
        return alarmRepository.getReservationsForOverdueAlarm();
    }

    @Override
    public List<MaintenanceLeavedAlarm> getMaintenancesForLeavedAlarm() {
        return alarmRepository.getMaintenancesForLeavedAlarm();
    }
}