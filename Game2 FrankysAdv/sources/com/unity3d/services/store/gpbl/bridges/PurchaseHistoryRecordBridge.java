package com.unity3d.services.store.gpbl.bridges;

public class PurchaseHistoryRecordBridge extends CommonJsonResponseBridge {
    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.PurchaseHistoryRecord";
    }

    public PurchaseHistoryRecordBridge(Object obj) {
        super(obj);
    }
}
