package app.domains.dashboard.service;

import app.domains.dashboard.dao.DashBoardRepository;
import app.domains.dashboard.dto.CategoryDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    private DashBoardRepository dashBoardRepository;

    @Override
    public Map<String, Long> getAssetMetrics() {
        Map<String, Long> metrics = new HashMap<>();
        
        // 전체 자산 수
        metrics.put("total", dashBoardRepository.getTotalAssetCount());
        
        // 상태별 자산 수
        // TODO refactor: ENUM
        metrics.put("available", dashBoardRepository.getAssetCountByStatus("AVAILABLE"));
        metrics.put("maintenanceRequired", dashBoardRepository.getAssetCountByStatus("MAINTENANCE_REQUIRED"));
        metrics.put("using", dashBoardRepository.getAssetCountByStatus("USING"));
        metrics.put("maintaining", dashBoardRepository.getAssetCountByStatus("MAINTAINING"));
        
        return metrics;
    }

    @Override
    public Map<String, Object> getAssetCategoryData() {
        List<CategoryDataDto> categoryDataList = dashBoardRepository.getAssetCountByCategory();

        // 데이터베이스에서 정렬된 순서 유지 (COUNT(*) DESC)
        List<String> labels = categoryDataList.stream()
            .map(this::getKoreanLabel)
            .collect(Collectors.toList());
        
        List<Long> totalCounts = categoryDataList.stream()
            .map(CategoryDataDto::getTotalCount)
            .collect(Collectors.toList());
        
        List<Long> maintenanceCounts = categoryDataList.stream()
            .map(CategoryDataDto::getMaintenanceRequiredCount)
            .collect(Collectors.toList());
        
        long maxCount = totalCounts.stream().mapToLong(Long::longValue).max().orElse(0L);
        
        return Map.of(
            "labels", labels,
            "totalCounts", totalCounts,
            "maintenanceCounts", maintenanceCounts,
            "maxCount", maxCount
        );
    }

    @Override
    public Map<String, Object> getAssetDistributionData() {
        List<CategoryDataDto> categoryDataList = dashBoardRepository.getAssetCountByCategory();

        // 데이터베이스에서 정렬된 순서 유지 (COUNT(*) DESC)
        List<String> labels = categoryDataList.stream()
            .map(this::getKoreanLabel)
            .collect(Collectors.toList());
        
        List<Long> data = categoryDataList.stream()
            .map(CategoryDataDto::getTotalCount)
            .collect(Collectors.toList());
        
        return Map.of(
            "labels", labels,
            "data", data
        );
    }

    /**
     * 카테고리 영문명을 한글명으로 변환
     */
    private String getKoreanLabel(CategoryDataDto dto) {
        Map<String, String> labelMap = Map.of(
            "TRACTOR", "트랙터",
            "COMBINE", "콤바인", 
            "RICE_PLANT", "이앙기",
            "TRANSPORT", "운반차",
            "LOADER", "로더"
        );
        return labelMap.get(dto.getCategory());
    }

    /**
     * DTO 리스트에서 카테고리별 카운트 데이터 추출
     */
    private List<Long> extractCountsFromDtoList(String[] categories, 
                                               List<CategoryDataDto> categoryDataList, 
                                               Function<CategoryDataDto, Long> extractor) {
        return Arrays.stream(categories)
            .mapToLong(category -> {
                return categoryDataList.stream()
                    .filter(dto -> category.equals(dto.getCategory()))
                    .findFirst()
                    .map(extractor)
                    .orElse(0L);
            })
            .boxed()
            .collect(Collectors.toList());
    }
}
