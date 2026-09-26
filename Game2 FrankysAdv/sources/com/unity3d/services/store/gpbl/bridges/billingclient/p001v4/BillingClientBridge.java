package com.unity3d.services.store.gpbl.bridges.billingclient.p001v4;

import android.content.Context;
import com.unity3d.services.store.gpbl.bridges.billingclient.common.BillingClientBridgeCommon;
import com.unity3d.services.store.gpbl.bridges.billingclient.common.BillingClientBuilderBridgeCommon;
import com.unity3d.services.store.gpbl.proxies.PurchasesResponseListenerProxy;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/* renamed from: com.unity3d.services.store.gpbl.bridges.billingclient.v4.BillingClientBridge */
public class BillingClientBridge extends BillingClientBridgeCommon {
    private static final String queryPurchasesAsyncMethodName = "queryPurchasesAsync";

    public BillingClientBridge(Object obj) throws ClassNotFoundException {
        super(obj, new HashMap<String, Class<?>[]>() {
            {
                put(BillingClientBridge.queryPurchasesAsyncMethodName, new Class[]{String.class, PurchasesResponseListenerProxy.getProxyListenerClass()});
            }
        });
    }

    public void queryPurchasesAsync(String str, PurchasesResponseListenerProxy purchasesResponseListenerProxy) throws ClassNotFoundException {
        callVoidMethod(queryPurchasesAsyncMethodName, this._billingClientInternalInstance, str, purchasesResponseListenerProxy.getProxyInstance());
    }

    public static boolean isAvailable() {
        try {
            getClassForBridge().getMethod(queryPurchasesAsyncMethodName, new Class[]{String.class, PurchasesResponseListenerProxy.getProxyListenerClass()});
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException unused) {
            return false;
        }
    }

    public static BuilderBridge newBuilder(Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        return new BuilderBridge(callNonVoidStaticMethod("newBuilder", context));
    }

    /* renamed from: com.unity3d.services.store.gpbl.bridges.billingclient.v4.BillingClientBridge$BuilderBridge */
    public static class BuilderBridge extends BillingClientBuilderBridgeCommon {
        public BuilderBridge(Object obj) throws ClassNotFoundException {
            super(obj);
        }

        public BillingClientBridgeCommon build() throws ClassNotFoundException {
            return new BillingClientBridge(callNonVoidMethod("build", this._billingClientBuilderInternalInstance, new Object[0]));
        }
    }
}
