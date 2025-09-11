package app.domains.dashboard.service;

import java.util.Map;

public interface DashBoardService {
    /**
     * 상단 자원 현황 카드 - 자원들에 대한 상태별 집계
     * 자원 상태 : 사용 가능 AVAILABLE, 사용중 USING, 점검 필요 MAINTENANCE_REQUIRED, 점검중 MAINTAINING
     * SELECT COUNT
     */
    Map<String, Long> getAssetMetrics();
    
    /**
     * 자원 종류별 현황 차트 데이터
     * 카테고리별 총 자산 수와 점검 필요 자산 수
     */
    Map<String, Object> getAssetCategoryData();
    
    /**
     * 자원 분포 현황 차트 데이터
     * 카테고리별 총 자산 수 (파이 차트용)
     */
    Map<String, Object> getAssetDistributionData();
}
