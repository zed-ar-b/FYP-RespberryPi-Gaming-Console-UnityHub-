package com.unity3d.services.ads.api;

import com.unity3d.services.ads.gmascar.GMAScarAdapterBridge;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import org.json.JSONArray;
import org.json.JSONException;

public class GMAScar {
    private static final GMAScarAdapterBridge gmaScarAdapterBridge = new GMAScarAdapterBridge();

    @WebViewExposed
    public static void initializeScar(WebViewCallback webViewCallback) {
        gmaScarAdapterBridge.initializeScar();
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getVersion(WebViewCallback webViewCallback) {
        gmaScarAdapterBridge.getVersion();
        webViewCallback.invoke(new Object[0]);
    }

    public static void isInitialized(WebViewCallback webViewCallback) {
        gmaScarAdapterBridge.isInitialized();
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getSCARSignals(JSONArray jSONArray, JSONArray jSONArray2, WebViewCallback webViewCallback) throws JSONException {
        gmaScarAdapterBridge.getSCARSignals(getPlacementList(jSONArray), getPlacementList(jSONArray2));
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void load(String str, String str2, Boolean bool, String str3, String str4, Integer num, WebViewCallback webViewCallback) {
        gmaScarAdapterBridge.load(bool.booleanValue(), str, str2, str4, str3, num.intValue());
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void show(String str, String str2, Boolean bool, WebViewCallback webViewCallback) {
        gmaScarAdapterBridge.show(str, str2, bool.booleanValue());
        webViewCallback.invoke(new Object[0]);
    }

    private static String[] getPlacementList(JSONArray jSONArray) throws JSONException {
        String[] strArr = new String[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            strArr[i] = jSONArray.getString(i);
        }
        return strArr;
    }
}
