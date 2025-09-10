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

            // 그룹핑된 자산 목록 조회 (중복 제거)
            List<Asset> assets = repository.findAssetsGrouped(params);

            return assets;
            
        } catch (Exception e) {
            throw new RuntimeException("자산 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public int getAssetCount(String q, String filter) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            // 검색어와 필터는 null이거나 빈 문자열이면 넣지 않음
            if (q != null && !q.trim().isEmpty()) {
                params.put("q", q.trim());
            }
            if (filter != null && !filter.trim().isEmpty()) {
                params.put("filter", filter.trim());
            }

            // 그룹핑된 자산의 총 개수 조회 (중복 제거)
            int count = repository.countAssetsGrouped(params);

            return count;
            
        } catch (Exception e) {
            throw new RuntimeException("자산 개수 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Asset> getAvailableAssets(String name, String category, String company) {
        try {
            // 파라미터 검증
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("자산 이름은 필수입니다.");
            }

            List<Asset> availableAssets = repository.getAvailableAssets(name, category, company);
            
            return availableAssets;
            
        } catch (Exception e) {
            throw new RuntimeException("사용 가능한 자산 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}