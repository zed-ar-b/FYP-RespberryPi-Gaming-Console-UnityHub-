package com.unity3d.services.store.gpbl.bridges;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.reflection.GenericBridge;
import com.unity3d.services.store.gpbl.IBillingResponse;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class CommonJsonResponseBridge extends GenericBridge implements IBillingResponse {
    private static final String getOriginalJsonMethodName = "getOriginalJson";
    private final Object _internalBridgeRef;

    protected CommonJsonResponseBridge(Object obj) {
        this(obj, new HashMap());
    }

    protected CommonJsonResponseBridge(Object obj, Map<String, Class<?>[]> map) {
        super(appendCommonResponseMethods(map));
        this._internalBridgeRef = obj;
    }

    private static Map<String, Class<?>[]> appendCommonResponseMethods(Map<String, Class<?>[]> map) {
        map.putAll(new HashMap<String, Class<?>[]>() {
            {
                put(CommonJsonResponseBridge.getOriginalJsonMethodName, new Class[0]);
            }
        });
        return map;
    }

    public JSONObject getOriginalJson() {
        try {
            return new JSONObject((String) callNonVoidMethod(getOriginalJsonMethodName, this._internalBridgeRef, new Object[0]));
        } catch (JSONException e) {
            DeviceLog.error("Couldn't parse BillingResponse JSON : %s", e.getMessage());
            return null;
        }
    }
}
