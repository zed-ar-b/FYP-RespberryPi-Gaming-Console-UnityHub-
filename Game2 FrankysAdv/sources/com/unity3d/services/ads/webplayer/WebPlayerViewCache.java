package com.unity3d.services.ads.webplayer;

import java.util.HashMap;

public class WebPlayerViewCache {
    private static WebPlayerViewCache instance;
    private HashMap<String, WebPlayerView> _webPlayerMap = new HashMap<>();

    public static WebPlayerViewCache getInstance() {
        if (instance == null) {
            instance = new WebPlayerViewCache();
        }
        return instance;
    }

    public synchronized void addWebPlayer(String str, WebPlayerView webPlayerView) {
        this._webPlayerMap.put(str, webPlayerView);
    }

    public synchronized void removeWebPlayer(String str) {
        if (this._webPlayerMap.containsKey(str)) {
            this._webPlayerMap.remove(str);
        }
    }

    public synchronized WebPlayerView getWebPlayer(String str) {
        if (!this._webPlayerMap.containsKey(str)) {
            return null;
        }
        return this._webPlayerMap.get(str);
    }
}
