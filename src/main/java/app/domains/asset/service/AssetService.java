package app.domains.asset.service;

import app.domains.asset.model.*;

import java.util.List;

public interface AssetService {
    List<AssetDto> findAssetsPaged(String assetStatus, String category, String company, String location,
                                   String field, String keywordLike, int startRow, int endRow);
    int countAssets(String assetStatus, String category, String company, String location, String field, String keywordLike);
    AssetFilterOptionsDto loadFilterOptions();
    Asset getAssetDetail(long assetId);
    List<Part> getPartsByAssetId(long assetId);
    void registerAsset(AssetPartsDto asset);
    void updateAsset(AssetPartsUpdateDto asset);
    void deleteAsset(long assetId, String deletedBy);
    void deletePart(long partId);
}
