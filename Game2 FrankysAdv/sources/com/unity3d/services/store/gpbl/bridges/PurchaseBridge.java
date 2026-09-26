package com.unity3d.services.store.gpbl.bridges;

import com.unity3d.ads.metadata.InAppPurchaseMetaData;
import com.unity3d.services.core.log.DeviceLog;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseBridge extends CommonJsonResponseBridge {
    private static final String getSignatureMethodName = "getSignature";
    private final Object _purchase;

    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.Purchase";
    }

    public PurchaseBridge(Object obj) {
        super(obj, new HashMap<String, Class<?>[]>() {
            {
                put(PurchaseBridge.getSignatureMethodName, new Class[0]);
            }
        });
        this._purchase = obj;
    }

    public String getSignature() {
        return (String) callNonVoidMethod(getSignatureMethodName, this._purchase, new Object[0]);
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("purchaseData", getOriginalJson());
            jSONObject.put(InAppPurchaseMetaData.KEY_SIGNATURE, getSignature());
        } catch (JSONException e) {
            DeviceLog.warning("Could not build Purchase result Json: ", e.getMessage());
        }
        return jSONObject;
    }
}
