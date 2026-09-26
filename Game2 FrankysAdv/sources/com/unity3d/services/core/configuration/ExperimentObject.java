package com.unity3d.services.core.configuration;

import com.unity3d.services.core.log.DeviceLog;
import org.json.JSONObject;

public class ExperimentObject {
    private static final String APPLIED_KEY = "applied";
    private static final String VALUE_KEY = "value";
    private final JSONObject _experimentData;

    public ExperimentObject(JSONObject jSONObject) {
        this._experimentData = jSONObject == null ? new JSONObject() : jSONObject;
    }

    public boolean getBooleanValue() {
        return this._experimentData.optBoolean(VALUE_KEY);
    }

    public ExperimentAppliedRule getAppliedRule() {
        ExperimentAppliedRule experimentAppliedRule = ExperimentAppliedRule.NEXT;
        String optString = this._experimentData.optString(APPLIED_KEY);
        if (optString.isEmpty()) {
            return experimentAppliedRule;
        }
        try {
            return ExperimentAppliedRule.valueOf(optString.toUpperCase());
        } catch (IllegalArgumentException unused) {
            DeviceLog.warning("Invalid rule %s for experiment", optString);
            return experimentAppliedRule;
        }
    }
}
