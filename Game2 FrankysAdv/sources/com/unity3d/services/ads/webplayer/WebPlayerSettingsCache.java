package com.unity3d.services.ads.webplayer;

import java.util.HashMap;
import org.json.JSONObject;

public class WebPlayerSettingsCache {
    private static WebPlayerSettingsCache instance;
    private HashMap<String, JSONObject> _webPlayerEventSettings = new HashMap<>();
    private HashMap<String, JSONObject> _webPlayerSettings = new HashMap<>();
    private HashMap<String, JSONObject> _webSettings = new HashMap<>();

    public static WebPlayerSettingsCache getInstance() {
        if (instance == null) {
            instance = new WebPlayerSettingsCache();
        }
        return instance;
    }

    public synchronized void addWebSettings(String str, JSONObject jSONObject) {
        this._webSettings.put(str, jSONObject);
    }

    public synchronized void removeWebSettings(String str) {
        if (this._webSettings.containsKey(str)) {
            this._webSettings.remove(str);
        }
    }

    public synchronized JSONObject getWebSettings(String str) {
        if (this._webSettings.containsKey(str)) {
            return this._webSettings.get(str);
        }
        return new JSONObject();
    }

    public synchronized void addWebPlayerSettings(String str, JSONObject jSONObject) {
        this._webPlayerSettings.put(str, jSONObject);
    }

    public synchronized void removeWebPlayerSettings(String str) {
        if (this._webPlayerSettings.containsKey(str)) {
            this._webPlayerSettings.remove(str);
        }
    }

    public synchronized JSONObject getWebPlayerSettings(String str) {
        if (this._webPlayerSettings.containsKey(str)) {
            return this._webPlayerSettings.get(str);
        }
        return new JSONObject();
    }

    public synchronized void addWebPlayerEventSettings(String str, JSONObject jSONObject) {
        this._webPlayerEventSettings.put(str, jSONObject);
    }

    public synchronized void removeWebPlayerEventSettings(String str) {
        if (this._webPlayerEventSettings.containsKey(str)) {
            this._webPlayerEventSettings.remove(str);
        }
    }

    public synchronized JSONObject getWebPlayerEventSettings(String str) {
        if (this._webPlayerEventSettings.containsKey(str)) {
            return this._webPlayerEventSettings.get(str);
        }
        return new JSONObject();
    }
}
