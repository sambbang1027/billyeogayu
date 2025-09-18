// 작성자 : 김민호
package app.domains.asset.dao;

import app.domains.asset.model.Asset;
import app.domains.asset.model.AssetDto;
import app.domains.asset.model.Part;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssetRepository {
    Asset findById(@Param("assetId") long assetId);
    List<Part> findByAssetId(@Param("assetId") Long assetId);
    int insertAsset(Asset asset);
    int insertParts(@Param("assetId") long assetId, @Param("parts") List<Part> parts);
    int updateAsset(Asset asset);
    int updateParts(@Param("assetId") long assetId, @Param("parts") List<Part> parts);
    int deleteAsset(@Param("assetId") long assetId, @Param("deletedBy") String deletedBy);
    int deletePart(@Param("partId") long partId);
    int countAll(@Param("assetStatus") String assetStatus,
                 @Param("category")     String category,
                 @Param("company")      String company,
                 @Param("location")    String location,
                 @Param("field")        String field,
                 @Param("keywordLike")  String keywordLike);

    List<AssetDto> findAllPaged(@Param("assetStatus") String assetStatus,
                                @Param("category")     String category,
                                @Param("company")      String company,
                                @Param("location")    String location,
                                @Param("field")        String field,
                                @Param("keywordLike")  String keywordLike,
                                @Param("startRow")     int startRow,
                                @Param("endRow")       int endRow);

    List<String> findCategories();
    List<String> findCompanies();
    List<String> findLocations();
}
