package com.unity3d.services.store.core.api;

import com.unity3d.services.ads.gmascar.handlers.WebViewErrorHandler;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import com.unity3d.services.store.StoreError;
import com.unity3d.services.store.StoreEvent;
import com.unity3d.services.store.StoreMonitor;
import com.unity3d.services.store.core.StoreExceptionHandler;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class Store {
    private static final StoreExceptionHandler storeExceptionHandler = new StoreExceptionHandler(new WebViewErrorHandler());

    @WebViewExposed
    public static void initialize(WebViewCallback webViewCallback) {
        try {
            StoreMonitor.initialize(storeExceptionHandler);
            webViewCallback.invoke(new Object[0]);
        } catch (Exception e) {
            webViewCallback.error(StoreError.UNKNOWN_ERROR, e.getMessage(), e.getClass().getName());
        }
    }

    @WebViewExposed
    public static void startPurchaseTracking(JSONArray jSONArray, WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            try {
                arrayList.add(jSONArray.getString(i));
                i++;
            } catch (JSONException e) {
                webViewCallback.error(StoreError.JSON_ERROR, e.getMessage());
                return;
            }
        }
        StoreMonitor.startPurchaseTracking(arrayList);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void stopPurchaseTracking(WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        StoreMonitor.stopPurchaseTracking();
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void isFeatureSupported(final Integer num, final String str, WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                StoreMonitor.isFeatureSupported(num.intValue(), str);
            }
        }).start();
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getPurchases(Integer num, String str, WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        StoreMonitor.getPurchases(num.intValue(), str);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getPurchaseHistory(Integer num, String str, Integer num2, WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        StoreMonitor.getPurchaseHistory(num.intValue(), str, num2.intValue());
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getSkuDetails(Integer num, String str, JSONArray jSONArray, WebViewCallback webViewCallback) {
        if (!StoreMonitor.isInitialized()) {
            webViewCallback.error(StoreError.NOT_INITIALIZED, new Object[0]);
            return;
        }
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.getString(i));
            }
            StoreMonitor.getSkuDetails(num.intValue(), str, arrayList);
        } catch (JSONException e) {
            storeExceptionHandler.handleStoreException(StoreEvent.SKU_DETAILS_LIST_REQUEST_ERROR, num.intValue(), e);
        }
        webViewCallback.invoke(new Object[0]);
    }
}
