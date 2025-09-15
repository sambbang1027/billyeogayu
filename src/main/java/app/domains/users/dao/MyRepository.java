package app.domains.users.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import app.domains.users.model.MyReservation;

/**
 * 내 정보 관련 Repository 인터페이스
 */
public interface MyRepository {
    
    /**
     * 내 예약 목록 조회
     * @param userId 사용자 ID
     * @return 예약 목록
     */
    List<MyReservation> selectMyReservations(@Param("userId") Long userId);
    
    /**
     * 내 예약 목록 조회 (필터링)
     * @param params userId, status, startDate, endDate, category 포함한 파라미터 맵
     * @return 예약 목록
     */
    List<MyReservation> selectMyReservationsWithFilter(Map<String, Object> params);
    
    /**
     * 예약 상세 정보 조회 (본인 것만)
     * @param reservationId 예약 ID
     * @param userId 사용자 ID
     * @return 예약 상세 정보
     */
    MyReservation selectMyReservationDetail(@Param("reservationId") Long reservationId, 
                                           @Param("userId") Long userId);
    
    /**
     * 예약 취소 요청
     * @param reservationId 예약 ID
     * @param userId 사용자 ID (권한 체크용)
     * @return 업데이트된 행 수
     */
    int cancelMyReservation(@Param("reservationId") Long reservationId, 
                           @Param("userId") Long userId);
    
    /**
     * 내 사용 내역 조회 (RESERVATION 테이블에서 사용이 시작되었거나 완료된 것들)
     * @param userId 사용자 ID
     * @return 사용 내역 목록
     */
    List<MyReservation> selectMyUsageHistory(@Param("userId") Long userId);
    
    /**
     * 내 사용 내역 조회 (필터링)
     * @param params userId, startDate, endDate, category, usageStatus 포함한 파라미터 맵
     * @return 사용 내역 목록
     */
    List<MyReservation> selectMyUsageHistoryWithFilter(Map<String, Object> params);
    
    /**
     * 내 사용 통계 조회
     * @param userId 사용자 ID
     * @return 사용 통계 정보
     */
    Map<String, Object> selectMyUsageStatistics(@Param("userId") Long userId);


    Map<String, Object> selectMyUsageStatisticsWithTime(Map<String, Object> params);
    
    /**
     * 예약 건수 조회 (상태별)
     * @param userId 사용자 ID
     * @return 상태별 예약 건수
     */
    Map<String, Object> selectReservationCountByStatus(@Param("userId") Long userId);
    
    /**
     * 최근 사용한 농기계 목록
     * @param userId 사용자 ID
     * @param limit 조회 건수
     * @return 최근 사용 농기계 목록
     */
    List<Map<String, Object>> selectRecentUsedAssets(@Param("userId") Long userId,@Param("limit") int limit);
}