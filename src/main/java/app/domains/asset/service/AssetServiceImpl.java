package app.domains.asset.service;

import app.domains.asset.dao.AssetRepository;
import app.domains.asset.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService{
    private final AssetRepository assetRepository;

    @Override
    public List<AssetDto> findAssetsPaged(String assetStatus, String category, String company,
                                          String field, String keywordLike, int startRow, int endRow) {
        return assetRepository.findAllPaged(assetStatus, category, company, field, keywordLike, startRow, endRow)
                .stream()
                .map(Asset::toDto)
                .toList();
    }

    @Override
    public int countAssets(String assetStatus, String category, String company, String field, String keywordLike) {
        return assetRepository.countAll(assetStatus, category, company, field, keywordLike);
    }

    @Override
    public AssetFilterOptionsDto loadFilterOptions() {
        var categories = assetRepository.findCategories();
        var companies  = assetRepository.findCompanies();

        var locations = Collections.<String>emptyList();
        try {
            locations = assetRepository.findLocations();
        } catch (Exception e) {
        }

        return new AssetFilterOptionsDto(categories, companies, locations);
    }

    @Override
    public Asset getAssetDetail(long assetId) {
        return assetRepository.findById(assetId);
    }

    @Override
    public List<Part> getPartsByAssetId(long assetId) {
        return assetRepository.findByAssetId(assetId);
    }

    @Override
    @Transactional
    public void registerAsset(AssetPartsDto req) {
        if (req == null || req.getAsset() == null) {
            throw new IllegalArgumentException("asset is required");
        }

        Asset asset = req.getAsset();

        if (asset.getExpectedMaintenanceDate() == null
                && asset.getMaintenanceCycle() > 0) {
            asset.setExpectedMaintenanceDate(LocalDateTime.now()
                    .plusHours(asset.getMaintenanceCycle()));
        }

        assetRepository.insertAsset(asset);

        List<Part> rawParts = req.getParts();
        if (rawParts == null || rawParts.isEmpty()) return;

        List<Part> parts = new java.util.ArrayList<>();
        for (Part p : rawParts) {
            if (p == null) continue;

            boolean isBlank =
                    (p.getPartName() == null || p.getPartName().isBlank())
                            && p.getMaintenanceCycle() == null
                            && p.getUsageTime() == null
                            && p.getPartStatus() == null
                            && p.getLastMaintenanceDate() == null
                            && p.getExpectedMaintenanceDate() == null;

            if (isBlank) continue;

            p.setAssetId(asset.getAssetId());
            if (p.getPartStatus() == null) p.setPartStatus("AVAILABLE");

            if (p.getExpectedMaintenanceDate() == null
                    && p.getMaintenanceCycle() != null
                    && p.getMaintenanceCycle() > 0) {
                p.setExpectedMaintenanceDate(LocalDateTime.now()
                        .plusHours(p.getMaintenanceCycle()));
            }

            parts.add(p);
        }

        if (!parts.isEmpty()) {
            assetRepository.insertParts(asset.getAssetId(), parts);
        }
    }

    @Override
    @Transactional
    public void updateAsset(AssetPartsUpdateDto dto) {
        Asset asset = dto.getAsset();
        List<Part> parts = dto.getParts();

        if (dto.isAssetCycleChanged() || dto.isImageChanged()) {
            assetRepository.updateAsset(asset);
        }

        List<Part> existing = assetRepository.findByAssetId(asset.getAssetId());

        List<Part> toInsert = new ArrayList<>();
        List<Part> toUpdate = new ArrayList<>();

        for (Part p : parts) {
            if (p.getPartId() == null) {
                toInsert.add(p);
            } else {
                Part dbPart = existing.stream()
                        .filter(ep -> ep.getPartId().equals(p.getPartId()))
                        .findFirst()
                        .orElse(null);
                if (dbPart != null && !Objects.equals(dbPart.getMaintenanceCycle(), p.getMaintenanceCycle())) {
                    toUpdate.add(p);
                }
            }
        }

        if (!toInsert.isEmpty()) {
            assetRepository.insertParts(asset.getAssetId(), toInsert);
        }

        if (!toUpdate.isEmpty()) {
            assetRepository.updateParts(asset.getAssetId(), toUpdate);
        }
    }

    @Override
    public void deleteAsset(long assetId, String deletedBy) {
        assetRepository.deleteAsset(assetId, deletedBy);
    }

    @Override
    public void deletePart(long partId) {
        assetRepository.deletePart(partId);
    }
}
