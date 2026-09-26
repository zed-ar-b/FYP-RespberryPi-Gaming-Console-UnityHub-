package com.unity3d.scar.adapter.v1950.signals;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SignalsStorage {
    private final Map<String, QueryInfoMetadata> _placementQueryInfoMap = new ConcurrentHashMap();

    public Map<String, QueryInfoMetadata> getPlacementQueryInfoMap() {
        return this._placementQueryInfoMap;
    }

    public QueryInfoMetadata getQueryInfoMetadata(String str) {
        return this._placementQueryInfoMap.get(str);
    }

    public void put(String str, QueryInfoMetadata queryInfoMetadata) {
        this._placementQueryInfoMap.put(str, queryInfoMetadata);
    }
}
