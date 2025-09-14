package app.domains.users.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import app.domains.users.model.MyReservation;

/**
 * 내 정보 관련 Service 인터페이스
 */
public interface MyService {
    
    /**
     * 내 예약 목록 조회
     * @param userId 사용자 ID
     * @return 예약 목록
     */
    List<MyReservation> getMyReservations(Long userId);
    
    /**
     * 내 예약 목록 조회 (상태별 필터링)
     * @param userId 사용자 ID
     * @param status 예약 상태 (선택사항)
     * @return 예약 목록
     */
    List<MyReservation> getMyReservationsByStatus(Long userId, String status);
    
    /**
     * 내 예약 목록 조회 (기간별 + 기타 필터링)
     * @param userId 사용자 ID
     * @param status 예약 상태 (선택사항)
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @param category 농기계 카테고리 (선택사항)
     * @return 예약 목록
     */
    List<MyReservation> getMyReservationsWithFilter(Long userId, String status, 
                                                    Date startDate, Date endDate, String category);
    
    /**
     * 예약 상세 정보 조회 (본인 것만)
     * @param reservationId 예약 ID
     * @param userId 사용자 ID
     * @return 예약 상세 정보
     */
    MyReservation getMyReservationDetail(Long reservationId, Long userId);
    
    /**
     * 예약 취소 요청
     * @param reservationId 예약 ID
     * @param userId 사용자 ID
     * @return 취소 성공 여부
     */
    boolean cancelMyReservation(Long reservationId, Long userId);
    
    /**
     * 내 사용 내역 조회 (RESERVATION 테이블에서 사용이 시작되었거나 완료된 것들)
     * @param userId 사용자 ID
     * @return 사용 내역 목록
     */
    List<MyReservation> getMyUsageHistory(Long userId);
    
    /**
     * 내 사용 내역 조회 (필터링)
     * @param userId 사용자 ID
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @param category 농기계 카테고리 (선택사항)
     * @param usageStatus 사용 상태 (선택사항: COMPLETED, ACTIVE, OVERDUE)
     * @return 사용 내역 목록
     */
    List<MyReservation> getMyUsageHistoryWithFilter(Long userId, Date startDate, 
                                                     Date endDate, String category, String usageStatus);
    
    /**
     * 내 사용 통계 조회
     * @param userId 사용자 ID
     * @return 사용 통계 정보
     */
    Map<String, Object> getMyUsageStatistics(Long userId);
    
    /**
     * 예약 현황 요약 정보
     * @param userId 사용자 ID
     * @return 예약 현황 요약
     */
    Map<String, Object> getReservationSummary(Long userId);
    
    /**
     * 대시보드용 요약 정보
     * @param userId 사용자 ID
     * @return 대시보드 정보
     */
    Map<String, Object> getDashboardSummary(Long userId);
    
    /**
     * 최근 사용한 농기계 목록
     * @param userId 사용자 ID
     * @param limit 조회 건수
     * @return 최근 사용 농기계 목록
     */
    List<Map<String, Object>> getRecentUsedAssets(Long userId, int limit);
}