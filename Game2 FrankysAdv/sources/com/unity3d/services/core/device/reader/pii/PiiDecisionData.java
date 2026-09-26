package com.unity3d.services.core.device.reader.pii;

import com.unity3d.services.core.device.reader.JsonStorageKeyNames;
import java.util.HashMap;
import java.util.Map;

public class PiiDecisionData {
    private final Map<String, Object> _attributes;
    private final DataSelectorResult _resultType;

    public PiiDecisionData(DataSelectorResult dataSelectorResult) {
        this(dataSelectorResult, new HashMap());
    }

    public PiiDecisionData(DataSelectorResult dataSelectorResult, Map<String, Object> map) {
        this._resultType = dataSelectorResult;
        this._attributes = map;
    }

    public void appendData(Map<String, Object> map) {
        Map<String, Object> map2 = this._attributes;
        if (map2 != null) {
            map2.putAll(map);
        }
    }

    public DataSelectorResult getResultType() {
        return this._resultType;
    }

    public Map<String, Object> getAttributes() {
        return this._attributes;
    }

    public Boolean getUserNonBehavioralFlag() {
        Map<String, Object> map = this._attributes;
        if (map != null) {
            Object obj = map.get(JsonStorageKeyNames.USER_NON_BEHAVIORAL_KEY);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
        }
        return null;
    }
}
