package com.unity3d.services.store.listeners;

import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import com.unity3d.services.store.StoreEvent;
import com.unity3d.services.store.gpbl.BillingResultResponseCode;
import com.unity3d.services.store.gpbl.bridges.BillingResultBridge;
import com.unity3d.services.store.gpbl.bridges.PurchaseBridge;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class PurchasesResponseListener implements IPurchasesResponseListener {
    private final StoreEvent _errorEvent;
    private final Integer _operationId;
    private final StoreEvent _successEvent;

    public PurchasesResponseListener(StoreEvent storeEvent, StoreEvent storeEvent2) {
        this((Integer) null, storeEvent, storeEvent2);
    }

    public PurchasesResponseListener(Integer num, StoreEvent storeEvent, StoreEvent storeEvent2) {
        this._operationId = num;
        this._successEvent = storeEvent;
        this._errorEvent = storeEvent2;
    }

    public void onBillingResponse(BillingResultBridge billingResultBridge, List<PurchaseBridge> list) {
        ArrayList arrayList = new ArrayList();
        Integer num = this._operationId;
        if (num != null) {
            arrayList.add(num);
        }
        if (billingResultBridge.getResponseCode() == BillingResultResponseCode.OK) {
            JSONArray jSONArray = new JSONArray();
            if (list != null) {
                for (PurchaseBridge json : list) {
                    jSONArray.put(json.toJson());
                }
            }
            arrayList.add(jSONArray);
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.STORE, this._successEvent, arrayList.toArray());
            return;
        }
        arrayList.add(billingResultBridge.getResponseCode());
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.STORE, this._errorEvent, arrayList.toArray());
    }
}
