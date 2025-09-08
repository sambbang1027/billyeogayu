package app.domains.list.service;

import java.util.List;

import app.domains.asset.model.Asset;

public interface ListService {
    List<Asset> getAssets(String q, String filter, int page, int pageSize);
    int getAssetCount(String q, String filter);
}
