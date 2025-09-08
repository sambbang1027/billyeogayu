package app.domains.list.dao;

import java.util.List;
import java.util.Map;

import app.domains.asset.model.Asset;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ListRepository {
    List<Asset> findAssets(Map<String, Object> params);
    int countAssets(Map<String, Object> params);
}
