package app.domains.asset.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.domains.asset.model.Asset;

@Mapper
public interface AssetRepository {
    /**
     * 기존 메서드들
     */
    List<Asset> findAssets(Map<String, Object> params);
    int countAssets(Map<String, Object> params);

    /**
     * 그룹핑된 자산 목록 조회 (name, category, company 기준으로 그룹핑하여 실제 재고 정보 포함)
     */
    List<Asset> findAssetsGrouped(Map<String, Object> params);

    /**
     * 그룹핑된 자산의 총 개수 (중복 제거)
     */
    int countAssetsGrouped(Map<String, Object> params);

    /**
     * 특정 name, category, company 조합의 보유대수 계산
     */
    int countAssetStock(@Param("name") String name,
                       @Param("category") String category,
                       @Param("company") String company);

    /**
     * 특정 그룹의 사용 가능한 자산들 조회
     */
    List<Asset> getAvailableAssets(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("company") String company);
}