package com.unity3d.services.store.gpbl.bridges.billingclient.common;

import com.unity3d.services.core.reflection.GenericBridge;
import com.unity3d.services.store.gpbl.bridges.billingclient.IBillingClientBuilderBridge;
import com.unity3d.services.store.gpbl.proxies.PurchaseUpdatedListenerProxy;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class BillingClientBuilderBridgeCommon extends GenericBridge implements IBillingClientBuilderBridge {
    protected static final String buildMethodName = "build";
    private static final String enablePendingPurchasesMethodName = "enablePendingPurchases";
    private static final String setListenerMethodName = "setListener";
    protected Object _billingClientBuilderInternalInstance;

    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.BillingClient$Builder";
    }

    public BillingClientBuilderBridgeCommon(Object obj) throws ClassNotFoundException {
        super(new HashMap<String, Class<?>[]>() {
            {
                put(BillingClientBuilderBridgeCommon.setListenerMethodName, new Class[]{PurchaseUpdatedListenerProxy.getProxyListenerClass()});
                put(BillingClientBuilderBridgeCommon.enablePendingPurchasesMethodName, new Class[0]);
                put(BillingClientBuilderBridgeCommon.buildMethodName, new Class[0]);
            }
        });
        this._billingClientBuilderInternalInstance = obj;
    }

    public IBillingClientBuilderBridge setListener(PurchaseUpdatedListenerProxy purchaseUpdatedListenerProxy) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this._billingClientBuilderInternalInstance = callNonVoidMethod(setListenerMethodName, this._billingClientBuilderInternalInstance, purchaseUpdatedListenerProxy.getProxyInstance());
        return this;
    }

    public IBillingClientBuilderBridge enablePendingPurchases() {
        this._billingClientBuilderInternalInstance = callNonVoidMethod(enablePendingPurchasesMethodName, this._billingClientBuilderInternalInstance, new Object[0]);
        return this;
    }
}
