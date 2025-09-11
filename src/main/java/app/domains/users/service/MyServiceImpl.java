package app.domains.users.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domains.users.dao.MyRepository;
import app.domains.users.model.MyReservation;
import app.domains.users.model.MyUsageHistory;
import lombok.extern.slf4j.Slf4j;

/**
 * 내 정보 관련 Service 구현체
 */
@Service
@Transactional
@Slf4j
public class MyServiceImpl implements MyService {
    
    @Autowired
    private MyRepository myRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<MyReservation> getMyReservations(Long userId) {
        log.debug("내 예약 목록 조회 - userId: {}", userId);
        return myRepository.selectMyReservations(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyReservation> getMyReservationsByStatus(Long userId, String status) {
        log.debug("내 예약 목록 조회 (상태별) - userId: {}, status: {}", userId, status);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        
        return myRepository.selectMyReservationsWithFilter(params);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyReservation> getMyReservationsWithFilter(Long userId, String status, 
                                                           Date startDate, Date endDate, String category) {
        log.debug("내 예약 목록 조회 (필터링) - userId: {}, status: {}, startDate: {}, endDate: {}, category: {}", 
                 userId, status, startDate, endDate, category);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", status);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("category", category);
        
        return myRepository.selectMyReservationsWithFilter(params);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MyReservation getMyReservationDetail(Long reservationId, Long userId) {
        log.debug("예약 상세 정보 조회 - reservationId: {}, userId: {}", reservationId, userId);
        return myRepository.selectMyReservationDetail(reservationId, userId);
    }
    
    @Override
    public boolean cancelMyReservation(Long reservationId, Long userId) {
        log.info("예약 취소 요청 - reservationId: {}, userId: {}", reservationId, userId);
        
        try {
            // 본인 예약이고 PENDING 상태인지 확인
            MyReservation reservation = getMyReservationDetail(reservationId, userId);
            if (reservation == null) {
                log.warn("예약을 찾을 수 없음 - reservationId: {}, userId: {}", reservationId, userId);
                return false;
            }
            
            if (!"PENDING".equals(reservation.getStatus())) {
                log.warn("취소할 수 없는 상태 - reservationId: {}, status: {}", reservationId, reservation.getStatus());
                return false;
            }
            
            int result = myRepository.cancelMyReservation(reservationId, userId);
            
            if (result > 0) {
                log.info("예약 취소 성공 - reservationId: {}, userId: {}", reservationId, userId);
                return true;
            } else {
                log.warn("예약 취소 실패 - reservationId: {}, userId: {}", reservationId, userId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("예약 취소 중 오류 발생 - reservationId: {}, userId: {}", reservationId, userId, e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyUsageHistory> getMyUsageHistory(Long userId) {
        log.debug("내 사용 이력 조회 - userId: {}", userId);
        return myRepository.selectMyUsageHistory(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyUsageHistory> getMyUsageHistoryWithFilter(Long userId, Date startDate, 
                                                            Date endDate, String category, String usageStatus) {
        log.debug("내 사용 이력 조회 (필터링) - userId: {}, startDate: {}, endDate: {}, category: {}, usageStatus: {}", 
                 userId, startDate, endDate, category, usageStatus);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("category", category);
        params.put("usageStatus", usageStatus);
        
        return myRepository.selectMyUsageHistoryWithFilter(params);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMyUsageStatistics(Long userId) {
        log.debug("내 사용 통계 조회 - userId: {}", userId);
        
        Map<String, Object> statistics = myRepository.selectMyUsageStatistics(userId);
        
        // 기본값 설정
        if (statistics == null) {
            statistics = new HashMap<>();
        }
        
        statistics.putIfAbsent("TOTAL_USAGE", 0);
        statistics.putIfAbsent("COMPLETED_USAGE", 0);
        statistics.putIfAbsent("TOTAL_DURATION", 0);
        statistics.putIfAbsent("UNIQUE_ASSETS", 0);
        
        // 완료율 계산
        int totalUsage = ((Number) statistics.get("TOTAL_USAGE")).intValue();
        int completedUsage = ((Number) statistics.get("COMPLETED_USAGE")).intValue();
        
        double completionRate = totalUsage > 0 ? (double) completedUsage / totalUsage * 100 : 0;
        statistics.put("COMPLETION_RATE", Math.round(completionRate * 100) / 100.0);
        
        // 총 사용 시간을 시간 단위로 변환
        int totalDurationMinutes = ((Number) statistics.get("TOTAL_DURATION")).intValue();
        double totalHours = totalDurationMinutes / 60.0;
        statistics.put("TOTAL_HOURS", Math.round(totalHours * 100) / 100.0);
        
        return statistics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReservationSummary(Long userId) {
        log.debug("예약 현황 요약 조회 - userId: {}", userId);
        
        Map<String, Object> summary = myRepository.selectReservationCountByStatus(userId);
        
        // 기본값 설정
        if (summary == null) {
            summary = new HashMap<>();
        }
        
        summary.putIfAbsent("TOTAL_RESERVATIONS", 0);
        summary.putIfAbsent("PENDING_COUNT", 0);
        summary.putIfAbsent("APPROVED_COUNT", 0);
        summary.putIfAbsent("REJECTED_COUNT", 0);
        summary.putIfAbsent("CANCELLED_COUNT", 0);
        summary.putIfAbsent("COMPLETED_COUNT", 0);
        
        return summary;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary(Long userId) {
        log.debug("대시보드 요약 정보 조회 - userId: {}", userId);
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // 예약 현황
        Map<String, Object> reservationSummary = getReservationSummary(userId);
        dashboard.put("reservationSummary", reservationSummary);
        
        // 사용 통계
        Map<String, Object> usageStatistics = getMyUsageStatistics(userId);
        dashboard.put("usageStatistics", usageStatistics);
        
        // 최근 사용한 농기계 (최대 5개)
        List<Map<String, Object>> recentAssets = getRecentUsedAssets(userId, 5);
        dashboard.put("recentAssets", recentAssets);
        
        // 최근 예약 목록 (최대 5개)
        List<MyReservation> recentReservations = getMyReservations(userId);
        if (recentReservations != null && recentReservations.size() > 5) {
            recentReservations = recentReservations.subList(0, 5);
        }
        dashboard.put("recentReservations", recentReservations);
        
        return dashboard;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentUsedAssets(Long userId, int limit) {
        log.debug("최근 사용한 농기계 목록 조회 - userId: {}, limit: {}", userId, limit);
        return myRepository.selectRecentUsedAssets(userId, limit);
    }
}