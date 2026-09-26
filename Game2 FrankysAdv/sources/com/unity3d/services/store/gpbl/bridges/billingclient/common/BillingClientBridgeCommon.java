package com.unity3d.services.store.gpbl.bridges.billingclient.common;

import android.content.Context;
import com.unity3d.services.core.reflection.GenericBridge;
import com.unity3d.services.store.gpbl.BillingResultResponseCode;
import com.unity3d.services.store.gpbl.bridges.BillingResultBridge;
import com.unity3d.services.store.gpbl.bridges.SkuDetailsParamsBridge;
import com.unity3d.services.store.gpbl.bridges.billingclient.IBillingClient;
import com.unity3d.services.store.gpbl.proxies.BillingClientStateListenerProxy;
import com.unity3d.services.store.gpbl.proxies.PurchaseHistoryResponseListenerProxy;
import com.unity3d.services.store.gpbl.proxies.SkuDetailsResponseListenerProxy;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class BillingClientBridgeCommon extends GenericBridge implements IBillingClient {
    protected static final String endConnectionMethodName = "endConnection";
    protected static final String isFeatureSupportedMethodName = "isFeatureSupported";
    protected static final String isReadyMethodName = "isReady";
    protected static final String newBuilderMethodName = "newBuilder";
    protected static final String queryPurchaseHistoryAsyncMethodName = "queryPurchaseHistoryAsync";
    protected static final String querySkuDetailsAsyncMethodName = "querySkuDetailsAsync";
    protected static final String startConnectionMethodName = "startConnection";
    private static final Map<String, Class<?>[]> staticMethods = new HashMap<String, Class<?>[]>() {
        {
            put(BillingClientBridgeCommon.newBuilderMethodName, new Class[]{Context.class});
        }
    };
    protected final Object _billingClientInternalInstance;

    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.BillingClient";
    }

    public BillingClientBridgeCommon(Object obj, Map<String, Class<?>[]> map) throws ClassNotFoundException {
        super(appendFunctionAnParameters(map));
        this._billingClientInternalInstance = obj;
    }

    private static Map<String, Class<?>[]> appendFunctionAnParameters(Map<String, Class<?>[]> map) throws ClassNotFoundException {
        map.putAll(new HashMap<String, Class<?>[]>() {
            {
                put(BillingClientBridgeCommon.newBuilderMethodName, new Class[]{Context.class});
                put(BillingClientBridgeCommon.startConnectionMethodName, new Class[]{BillingClientStateListenerProxy.getProxyListenerClass()});
                put(BillingClientBridgeCommon.endConnectionMethodName, new Class[0]);
                put(BillingClientBridgeCommon.querySkuDetailsAsyncMethodName, new Class[]{SkuDetailsParamsBridge.getClassForBridge(), SkuDetailsResponseListenerProxy.getProxyListenerClass()});
                put(BillingClientBridgeCommon.queryPurchaseHistoryAsyncMethodName, new Class[]{String.class, PurchaseHistoryResponseListenerProxy.getProxyListenerClass()});
                put(BillingClientBridgeCommon.isFeatureSupportedMethodName, new Class[]{String.class});
                put(BillingClientBridgeCommon.isReadyMethodName, new Class[0]);
            }
        });
        return map;
    }

    protected static Class<?> getClassForBridge() throws ClassNotFoundException {
        return Class.forName("com.android.billingclient.api.BillingClient");
    }

    public void startConnection(BillingClientStateListenerProxy billingClientStateListenerProxy) throws ClassNotFoundException {
        callVoidMethod(startConnectionMethodName, this._billingClientInternalInstance, billingClientStateListenerProxy.getProxyInstance());
    }

    public void endConnection() {
        callVoidMethod(endConnectionMethodName, this._billingClientInternalInstance, new Object[0]);
    }

    public BillingResultResponseCode isFeatureSupported(String str) {
        return new BillingResultBridge(callNonVoidMethod(isFeatureSupportedMethodName, this._billingClientInternalInstance, str)).getResponseCode();
    }

    public boolean isReady() {
        return ((Boolean) callNonVoidMethod(isReadyMethodName, this._billingClientInternalInstance, new Object[0])).booleanValue();
    }

    public void querySkuDetailsAsync(SkuDetailsParamsBridge skuDetailsParamsBridge, SkuDetailsResponseListenerProxy skuDetailsResponseListenerProxy) throws ClassNotFoundException {
        callVoidMethod(querySkuDetailsAsyncMethodName, this._billingClientInternalInstance, skuDetailsParamsBridge.getInternalInstance(), skuDetailsResponseListenerProxy.getProxyInstance());
    }

    public void queryPurchaseHistoryAsync(String str, PurchaseHistoryResponseListenerProxy purchaseHistoryResponseListenerProxy) throws ClassNotFoundException {
        callVoidMethod(queryPurchaseHistoryAsyncMethodName, this._billingClientInternalInstance, str, purchaseHistoryResponseListenerProxy.getProxyInstance());
    }

    public static Object callNonVoidStaticMethod(String str, Object... objArr) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return getClassForBridge().getMethod(str, (Class[]) staticMethods.get(str)).invoke((Object) null, objArr);
    }
}
