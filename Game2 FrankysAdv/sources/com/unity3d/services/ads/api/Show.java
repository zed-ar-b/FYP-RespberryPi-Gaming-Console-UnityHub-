package com.unity3d.services.ads.api;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.operation.show.ShowModule;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;

public class Show {
    @WebViewExposed
    public static void sendShowFailedEvent(String str, String str2, String str3, String str4, WebViewCallback webViewCallback) {
        ShowModule.getInstance().onUnityAdsShowFailure(str2, UnityAds.UnityAdsShowError.valueOf(str3), str4);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void sendShowConsentEvent(String str, String str2, WebViewCallback webViewCallback) {
        ShowModule.getInstance().onUnityAdsShowConsent(str2);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void sendShowStartEvent(String str, String str2, WebViewCallback webViewCallback) {
        ShowModule.getInstance().onUnityAdsShowStart(str2);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void sendShowClickEvent(String str, String str2, WebViewCallback webViewCallback) {
        ShowModule.getInstance().onUnityAdsShowClick(str2);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void sendShowCompleteEvent(String str, String str2, String str3, WebViewCallback webViewCallback) {
        ShowModule.getInstance().onUnityAdsShowComplete(str2, UnityAds.UnityAdsShowCompletionState.valueOf(str3));
        webViewCallback.invoke(new Object[0]);
    }
}
