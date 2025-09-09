package app.domains.list.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domains.asset.model.Asset;
import app.domains.list.dao.ListRepository;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository repository;

    @Override
    public List<Asset> getAssets(String q, String filter, int page, int pageSize) {
        System.out.println("=== ListService.getAssets 호출 ===");
        System.out.println("파라미터: q=" + q + ", filter=" + filter + ", page=" + page + ", pageSize=" + pageSize);

        try {
            // 파라미터 검증
            if (page < 1) page = 1;
            if (pageSize < 1) pageSize = 12;

            Map<String, Object> params = new HashMap<>();
            
            // 검색어와 필터는 null이거나 빈 문자열이면 넣지 않음
            if (q != null && !q.trim().isEmpty()) {
                params.put("q", q.trim());
            }
            if (filter != null && !filter.trim().isEmpty()) {
                params.put("filter", filter.trim());
            }
            
            // 페이징 파라미터
            int offset = (page - 1) * pageSize;
            params.put("offset", offset);
            params.put("pageSize", pageSize);

            System.out.println("실제 전달되는 파라미터: " + params);

            // 그룹핑된 자산 목록 조회 (중복 제거)
            List<Asset> assets = repository.findAssetsGrouped(params);

            System.out.println("조회된 그룹핑된 자산 수: " + (assets != null ? assets.size() : 0));
            if (assets != null && !assets.isEmpty()) {
                for (int i = 0; i < Math.min(assets.size(), 5); i++) { // 최대 5개만 로그 출력
                    Asset asset = assets.get(i);
                    System.out.println("- " + asset.getName() + 
                                     " (카테고리: " + asset.getCategory() + 
                                     ", 회사: " + asset.getCompany() + 
                                     ", 총재고: " + asset.getStock() + 
                                     ", 사용가능: " + asset.getAvailableStock() + 
                                     ", 대여중: " + asset.getRentedStock() + 
                                     ", 임대가능: " + asset.isRentable() + ")");
                }
                if (assets.size() > 5) {
                    System.out.println("... 외 " + (assets.size() - 5) + "개 더");
                }
                if (assets != null) {
                    for (Asset asset : assets) {
                        if ("hy1313".equals(asset.getName())) {
                            System.out.println("=== hy1313 디버깅 ===");
                            System.out.println("이름: " + asset.getName());
                            System.out.println("총재고: " + asset.getStock());
                            System.out.println("사용가능재고: " + asset.getAvailableStock());
                            System.out.println("대여중재고: " + asset.getRentedStock());
                            System.out.println("isRentable(): " + asset.isRentable());
                            System.out.println("status: " + asset.getStatus());
                            System.out.println("isDeleted: " + asset.getIsDeleted());
                        }
                    }
                }
            }

            return assets;
            
        } catch (Exception e) {
            System.err.println("=== getAssets 오류 발생 ===");
            System.err.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("자산 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public int getAssetCount(String q, String filter) {
        System.out.println("=== ListService.getAssetCount 호출 ===");
        System.out.println("파라미터: q=" + q + ", filter=" + filter);

        try {
            Map<String, Object> params = new HashMap<>();
            
            // 검색어와 필터는 null이거나 빈 문자열이면 넣지 않음
            if (q != null && !q.trim().isEmpty()) {
                params.put("q", q.trim());
            }
            if (filter != null && !filter.trim().isEmpty()) {
                params.put("filter", filter.trim());
            }

            System.out.println("실제 전달되는 파라미터: " + params);

            // 그룹핑된 자산의 총 개수 조회 (중복 제거)
            int count = repository.countAssetsGrouped(params);
            System.out.println("그룹핑된 자산 총 개수: " + count);

            return count;
            
        } catch (Exception e) {
            System.err.println("=== getAssetCount 오류 발생 ===");
            System.err.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("자산 개수 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Asset> getAvailableAssets(String name, String category, String company) {
        System.out.println("=== ListService.getAvailableAssets 호출 ===");
        System.out.println("파라미터: name=" + name + ", category=" + category + ", company=" + company);

        try {
            // 파라미터 검증
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("자산 이름은 필수입니다.");
            }

            List<Asset> availableAssets = repository.getAvailableAssets(name, category, company);
            System.out.println("사용 가능한 자산 수: " + (availableAssets != null ? availableAssets.size() : 0));
            
            if (availableAssets != null && !availableAssets.isEmpty()) {
                System.out.println("사용 가능한 자산 목록:");
                for (Asset asset : availableAssets) {
                    System.out.println("  - ID: " + asset.getAssetId() + 
                                     ", 이름: " + asset.getName() + 
                                     ", 상태: " + asset.getStatus());
                }
            }
            
            return availableAssets;
            
        } catch (Exception e) {
            System.err.println("=== getAvailableAssets 오류 발생 ===");
            System.err.println("오류 메시지: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("사용 가능한 자산 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}