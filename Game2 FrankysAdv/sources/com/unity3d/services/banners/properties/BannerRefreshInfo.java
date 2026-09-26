package com.unity3d.services.banners.properties;

import java.util.HashMap;

public class BannerRefreshInfo {
    private static BannerRefreshInfo instance;
    private HashMap<String, Integer> _refreshRateMap = new HashMap<>();

    public static BannerRefreshInfo getInstance() {
        if (instance == null) {
            instance = new BannerRefreshInfo();
        }
        return instance;
    }

    public synchronized void setRefreshRate(String str, Integer num) {
        this._refreshRateMap.put(str, num);
    }

    public synchronized Integer getRefreshRate(String str) {
        return this._refreshRateMap.get(str);
    }
}
