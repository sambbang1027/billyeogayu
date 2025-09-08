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
        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("filter", filter);
        params.put("offset", (page - 1) * pageSize);
        params.put("pageSize", pageSize);
        return repository.findAssets(params);
    }

    @Override
    public int getAssetCount(String q, String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("filter", filter);
        return repository.countAssets(params);
    }
}
