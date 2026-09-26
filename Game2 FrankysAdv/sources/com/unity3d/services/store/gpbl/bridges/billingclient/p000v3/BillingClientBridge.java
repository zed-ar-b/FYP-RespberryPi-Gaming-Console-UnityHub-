package com.unity3d.services.store.gpbl.bridges.billingclient.p000v3;

import android.content.Context;
import com.unity3d.services.store.gpbl.bridges.PurchasesResultBridge;
import com.unity3d.services.store.gpbl.bridges.billingclient.IBillingClient;
import com.unity3d.services.store.gpbl.bridges.billingclient.common.BillingClientBridgeCommon;
import com.unity3d.services.store.gpbl.bridges.billingclient.common.BillingClientBuilderBridgeCommon;
import com.unity3d.services.store.gpbl.proxies.PurchasesResponseListenerProxy;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/* renamed from: com.unity3d.services.store.gpbl.bridges.billingclient.v3.BillingClientBridge */
public class BillingClientBridge extends BillingClientBridgeCommon {
    private static final String queryPurchasesMethodName = "queryPurchases";

    public BillingClientBridge(Object obj) throws ClassNotFoundException {
        super(obj, new HashMap<String, Class<?>[]>() {
            {
                put(BillingClientBridge.queryPurchasesMethodName, new Class[]{String.class});
            }
        });
    }

    public void queryPurchasesAsync(String str, PurchasesResponseListenerProxy purchasesResponseListenerProxy) {
        PurchasesResultBridge purchasesResultBridge = new PurchasesResultBridge(callNonVoidMethod(queryPurchasesMethodName, this._billingClientInternalInstance, str));
        purchasesResponseListenerProxy.getPurchasesResponseListener().onBillingResponse(purchasesResultBridge.getBillingResult(), purchasesResultBridge.getPurchasesList());
    }

    public static BuilderBridge newBuilder(Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        return new BuilderBridge(callNonVoidStaticMethod("newBuilder", context));
    }

    /* renamed from: com.unity3d.services.store.gpbl.bridges.billingclient.v3.BillingClientBridge$BuilderBridge */
    public static class BuilderBridge extends BillingClientBuilderBridgeCommon {
        public BuilderBridge(Object obj) throws ClassNotFoundException {
            super(obj);
        }

        public IBillingClient build() throws ClassNotFoundException {
            return new BillingClientBridge(callNonVoidMethod("build", this._billingClientBuilderInternalInstance, new Object[0]));
        }
    }
}
