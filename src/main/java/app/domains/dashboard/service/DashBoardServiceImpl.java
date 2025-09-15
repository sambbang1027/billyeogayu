package app.domains.dashboard.service;

import app.domains.dashboard.dao.DashBoardRepository;
import app.domains.dashboard.dto.CategoryDataDto;
import app.domains.dashboard.dto.CategoryModelDto;
import app.domains.dashboard.dto.UsageDataDto;
import app.domains.dashboard.dto.InspectionDataDto;
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
            .map(CategoryDataDto::getCategory)
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
            .map(CategoryDataDto::getCategory)
            .collect(Collectors.toList());
        
        List<Long> data = categoryDataList.stream()
            .map(CategoryDataDto::getTotalCount)
            .collect(Collectors.toList());
        
        return Map.of(
            "labels", labels,
            "data", data
        );
    }

    @Override
    public Map<String, Object> getUsageChartFilters() {
        // 실제 데이터베이스에서 카테고리-모델 관계 조회
        List<CategoryModelDto> categoryModels = dashBoardRepository.getAssetModelsByCategory();
        
        // 카테고리별로 모델 그룹화 (중복 제거)
        Map<String, List<String>> categoryModelMap = categoryModels.stream()
            .collect(Collectors.groupingBy(
                CategoryModelDto::getCategory,
                Collectors.mapping(CategoryModelDto::getModelName, 
                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))
            ));
        
        // 각 카테고리별 모델 리스트 정렬
        categoryModelMap.values().forEach(Collections::sort);
        
        // 실제 데이터베이스에서 상위 7개 도시 조회
        List<String> addresses = dashBoardRepository.getTopCitiesByReservationCount();
        
        return Map.of(
            "categoryModels", categoryModelMap,
            "addresses", addresses
        );
    }

    @Override
    public Map<String, Object> getUsageChartData(String category, String model, String address) {
        List<UsageDataDto> usageDataList = dashBoardRepository.getUsageDataByFilters(category, model, address);
        
        // 2016~2025년 전체 범위에서 누락된 연도는 0으로 채우기
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        for (int year = 2016; year <= 2025; year++) {
            final int currentYear = year; // effectively final 변수로 만들기
            labels.add(String.valueOf(year));
            Optional<UsageDataDto> yearData = usageDataList.stream()
                .filter(dto -> dto.getYear() == currentYear)
                .findFirst();
            data.add(yearData.map(UsageDataDto::getReservationCount).orElse(0L));
        }
        
        return Map.of(
            "labels", labels,
            "data", data
        );
    }

    @Override
    public Map<String, Object> getInspectionChartFilters() {
        // 사용량 차트와 동일한 카테고리-모델 데이터 사용
        List<CategoryModelDto> categoryModels = dashBoardRepository.getAssetModelsByCategory();
        
        // 카테고리별로 모델 그룹화 (중복 제거)
        Map<String, List<String>> categoryModelMap = categoryModels.stream()
            .collect(Collectors.groupingBy(
                CategoryModelDto::getCategory,
                Collectors.mapping(CategoryModelDto::getModelName, 
                    Collectors.collectingAndThen(Collectors.toSet(), ArrayList::new))
            ));
        
        // 각 카테고리별 모델 리스트 정렬
        categoryModelMap.values().forEach(Collections::sort);
        
        return Map.of(
            "categoryModels", categoryModelMap
        );
    }

    @Override
    public Map<String, Object> getInspectionChartData(String category, String model) {
        List<InspectionDataDto> inspectionDataList = dashBoardRepository.getInspectionDataByFilters(category, model);
        
        // 2016~2025년 전체 범위에서 누락된 연도는 0으로 채우기
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        for (int year = 2016; year <= 2025; year++) {
            final int currentYear = year; // effectively final 변수로 만들기
            labels.add(String.valueOf(year));
            Optional<InspectionDataDto> yearData = inspectionDataList.stream()
                .filter(dto -> dto.getYear() == currentYear)
                .findFirst();
            data.add(yearData.map(InspectionDataDto::getInspectionCount).orElse(0L));
        }
        
        return Map.of(
            "labels", labels,
            "data", data
        );
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
