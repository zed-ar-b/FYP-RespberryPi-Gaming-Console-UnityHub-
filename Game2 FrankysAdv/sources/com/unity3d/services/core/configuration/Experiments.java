package com.unity3d.services.core.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

public class Experiments extends ExperimentsBase {
    private static final Set<String> NEXT_SESSION_FLAGS = new HashSet(Arrays.asList(new String[]{"tsi", "tsi_upii", "tsi_p", "tsi_nt", "tsi_prr", "tsi_prw"}));
    private final JSONObject _experimentData;

    public Experiments() {
        this((JSONObject) null);
    }

    public Experiments(JSONObject jSONObject) {
        if (jSONObject == null) {
            this._experimentData = new JSONObject();
        } else {
            this._experimentData = jSONObject;
        }
    }

    public boolean isTwoStageInitializationEnabled() {
        return this._experimentData.optBoolean("tsi", true);
    }

    public boolean isForwardExperimentsToWebViewEnabled() {
        return this._experimentData.optBoolean("fff", false);
    }

    public boolean isNativeTokenEnabled() {
        return this._experimentData.optBoolean("tsi_nt", true);
    }

    public boolean isUpdatePiiFields() {
        return this._experimentData.optBoolean("tsi_upii", false);
    }

    public boolean isPrivacyRequestEnabled() {
        return this._experimentData.optBoolean("tsi_prr", true);
    }

    public boolean shouldNativeTokenAwaitPrivacy() {
        return this._experimentData.optBoolean("tsi_prw", false);
    }

    public boolean isNativeWebViewCacheEnabled() {
        return this._experimentData.optBoolean("nwc", false);
    }

    public boolean isWebAssetAdCaching() {
        return this._experimentData.optBoolean("wac", false);
    }

    public boolean isWebGestureNotRequired() {
        return this._experimentData.optBoolean("wgr", false);
    }

    public boolean isNewLifecycleTimer() {
        return this._experimentData.optBoolean("nlt", false);
    }

    public JSONObject getExperimentsAsJson() {
        return this._experimentData;
    }

    public Map<String, String> getExperimentTags() {
        HashMap hashMap = new HashMap();
        Iterator<String> keys = this._experimentData.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            hashMap.put(next, String.valueOf(this._experimentData.opt(next)));
        }
        return hashMap;
    }

    public JSONObject getNextSessionExperiments() {
        if (this._experimentData == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator<String> keys = this._experimentData.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            if (NEXT_SESSION_FLAGS.contains(next)) {
                hashMap.put(next, String.valueOf(this._experimentData.optBoolean(next)));
            }
        }
        return new JSONObject(hashMap);
    }

    public JSONObject getCurrentSessionExperiments() {
        if (this._experimentData == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator<String> keys = this._experimentData.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            if (!NEXT_SESSION_FLAGS.contains(next)) {
                hashMap.put(next, String.valueOf(this._experimentData.optBoolean(next)));
            }
        }
        return new JSONObject(hashMap);
    }
}
