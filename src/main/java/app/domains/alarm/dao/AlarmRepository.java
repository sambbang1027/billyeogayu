package app.domains.alarm.dao;

import app.domains.alarm.dto.AssetMaintenanceAlarm;
import app.domains.alarm.dto.PartReplaceAlarm;
import app.domains.alarm.dto.ReservationOverdueAlarm;
import app.domains.alarm.dto.MaintenanceLeavedAlarm;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.alarm.model.Alarm;

@Mapper
public interface AlarmRepository {

    // XML에서 정의된 메서드들
    Alarm findById(Long id);
    List<Alarm> findAll();
    void insert(Alarm alarm);
    
    // 읽음 처리 관련 메서드들
    void markAsRead(@Param("id") Long alarmId);
    int getUnreadCount();


    // 알람 스케줄링 관련 메서드들 (추가)
    /**
     * 정기점검 알람 대상 자산 조회
     * - 5일 후 정기점검 예정인 자산 중 AVAILABLE, USING 상태
     * - 당일 이미 알람이 생성되지 않은 자산
     */
    List<AssetMaintenanceAlarm> getAssetsForMaintenanceAlarm();

    /**
     * 자산 상태를 MAINTENANCE_REQUIRED로 업데이트
     */
    void updateAssetStatusToMaintenanceRequired(Long assetId);

    // 알람 스케줄링 관련 메서드들 (추가)
    /**
     * 교체 알람 대상 부품 조회
     * - 5일 후 교체 예정인 부품 중 AVAILABLE 상태
     * - 당일 이미 알람이 생성되지 않은 부품
     */
    List<PartReplaceAlarm> getPartsForReplaceAlarm();

    /**
     * 부품 상태를 UNAVAILABLE로 업데이트
     */
    void updatePartStatusToUnavailable(Long partId);

    /**
     * 방치 점검 대상 조회
     * - 30일 이상 진행 중인 점검 중 IN_PROGRESS 상태
     * - 아직 알람이 생성되지 않은 점검
     */
    List<MaintenanceLeavedAlarm> getMaintenancesForLeavedAlarm();

    /**
     * 연체 알람 대상 예약 조회
     * - 20일 이상 연체된 예약 중 APPROVED 상태
     * - 이미 알람이 생성되지 않은 예약
     */
    List<ReservationOverdueAlarm> getReservationsForOverdueAlarm();
}