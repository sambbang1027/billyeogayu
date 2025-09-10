	package app.domains.asset.service;

	import java.util.List;

import app.domains.asset.model.Asset;

	public interface AssetService {
	    /**
	     * 그룹핑된 자산 목록 조회 (페이징 포함)
	     */
	    List<Asset> getAssets(String q, String filter, int page, int pageSize);

	    /**
	     * 그룹핑된 자산 총 개수 조회
	     */
	    int getAssetCount(String q, String filter);

	    /**
	     * 특정 그룹의 사용 가능한 자산들 조회
	     */
	    List<Asset> getAvailableAssets(String name, String category, String company);
	}