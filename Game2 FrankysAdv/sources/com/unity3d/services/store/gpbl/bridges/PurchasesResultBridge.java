package com.unity3d.services.store.gpbl.bridges;

import com.unity3d.services.core.reflection.GenericBridge;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchasesResultBridge extends GenericBridge {
    private static final String getBillingResultMethodName = "getBillingResult";
    private static final String getPurchasesListMethodName = "getPurchasesList";
    private final Object _purchasesResult;

    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.Purchase$PurchasesResult";
    }

    public PurchasesResultBridge(Object obj) {
        super(new HashMap<String, Class<?>[]>() {
            {
                put(PurchasesResultBridge.getBillingResultMethodName, new Class[0]);
                put(PurchasesResultBridge.getPurchasesListMethodName, new Class[0]);
            }
        });
        this._purchasesResult = obj;
    }

    public BillingResultBridge getBillingResult() {
        return new BillingResultBridge(callNonVoidMethod(getBillingResultMethodName, this._purchasesResult, new Object[0]));
    }

    public List<PurchaseBridge> getPurchasesList() {
        List<Object> list = (List) callNonVoidMethod(getPurchasesListMethodName, this._purchasesResult, new Object[0]);
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (Object purchaseBridge : list) {
                arrayList.add(new PurchaseBridge(purchaseBridge));
            }
        }
        return arrayList;
    }
}
