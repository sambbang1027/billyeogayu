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
        
        // 🔍 MyBatis 파라미터 확인
        log.info("=== MyBatis 쿼리 파라미터 ===");
        log.info("userId: {}", params.get("userId"));
        log.info("status: {}", params.get("status"));
        log.info("startDate: {}", params.get("startDate"));
        log.info("endDate: {}", params.get("endDate"));
        log.info("category: {}", params.get("category"));
        
        List<MyReservation> result = myRepository.selectMyReservationsWithFilter(params);
        
        log.info("MyBatis 쿼리 실행 결과: {} 건", result != null ? result.size() : 0);
        
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public MyReservation getMyReservationDetail(Long reservationId, Long userId) {
        log.debug("예약 상세 정보 조회 - reservationId: {}, userId: {}", reservationId, userId);
        return myRepository.selectMyReservationDetail(reservationId, userId);
    }
    
    @Override
    public boolean cancelMyReservation(Long reservationId, Long userId) {
        log.debug("예약 취소 요청 - reservationId: {}, userId: {}", reservationId, userId);
        
        int updatedRows = myRepository.cancelMyReservation(reservationId, userId);
        boolean success = updatedRows > 0;
        
        if (success) {
            log.info("예약 취소 성공 - reservationId: {}, userId: {}", reservationId, userId);
        } else {
            log.warn("예약 취소 실패 - reservationId: {}, userId: {}", reservationId, userId);
        }
        
        return success;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyReservation> getMyUsageHistory(Long userId) {
        log.debug("내 사용 내역 조회 - userId: {}", userId);
        return myRepository.selectMyUsageHistory(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MyReservation> getMyUsageHistoryWithFilter(Long userId, Date startDate, 
                                                         Date endDate, String category, String usageStatus) {
        log.debug("내 사용 내역 조회 (필터링) - userId: {}, startDate: {}, endDate: {}, category: {}, usageStatus: {}", 
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
        
        // 디버깅용 로그 추가
        log.info("=== 사용 통계 원본 데이터 ===");
        log.info("totalUsageCount: {}", statistics.get("TOTALUSAGECOUNT"));
        log.info("completedCount: {}", statistics.get("COMPLETEDCOUNT"));
        log.info("totalUsageMinutes: {}", statistics.get("TOTALUSAGEMINUTES"));
        log.info("statistics 전체: {}", statistics);
        
        // 총 사용 시간을 시간 단위로 변환
        Long totalMinutes = null;
        Object totalMinutesObj = statistics.get("TOTALUSAGEMINUTES");
        
        if (totalMinutesObj instanceof Number) {
            totalMinutes = ((Number) totalMinutesObj).longValue();
        }
        
        log.info("변환된 totalMinutes: {}", totalMinutes);
        
        if (totalMinutes != null && totalMinutes > 0) {
            int totalHours = (int) (totalMinutes / 60);
            int remainingMinutes = (int) (totalMinutes % 60);
            statistics.put("totalUsageHours", totalHours);
            statistics.put("totalUsageMinutesRemaining", remainingMinutes);
            statistics.put("totalUsageFormatted", 
                          totalHours > 0 ? totalHours + "시간 " + remainingMinutes + "분" : remainingMinutes + "분");
        } else {
            statistics.put("totalUsageFormatted", "0분");
        }
        
        log.info("최종 totalUsageFormatted: {}", statistics.get("totalUsageFormatted"));
        
        return statistics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReservationSummary(Long userId) {
        log.debug("예약 현황 요약 조회 - userId: {}", userId);
        return myRepository.selectReservationCountByStatus(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary(Long userId) {
        log.debug("대시보드 요약 정보 조회 - userId: {}", userId);
        
        Map<String, Object> summary = new HashMap<>();
        
        // 예약 현황
        Map<String, Object> reservationSummary = getReservationSummary(userId);
        summary.putAll(reservationSummary);
        
        // 사용 통계
        Map<String, Object> usageStatistics = getMyUsageStatistics(userId);
        summary.putAll(usageStatistics);
        
        // 최근 사용한 농기계 (최대 5개)
        List<Map<String, Object>> recentAssets = getRecentUsedAssets(userId, 5);
        summary.put("recentUsedAssets", recentAssets);
        
        return summary;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentUsedAssets(Long userId, int limit) {
        log.debug("최근 사용한 농기계 목록 조회 - userId: {}, limit: {}", userId, limit);
        return myRepository.selectRecentUsedAssets(userId, limit);
    }
}