package com.unity3d.services.core.api;

import com.unity3d.services.core.configuration.InitializeThread;
import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;

public class Sdk {
    @WebViewExposed
    public static void loadComplete(WebViewCallback webViewCallback) {
        DeviceLog.debug("Web Application loaded");
        boolean z = true;
        WebViewApp.getCurrentApp().setWebAppLoaded(true);
        Object[] objArr = new Object[18];
        objArr[0] = ClientProperties.getGameId();
        objArr[1] = Boolean.valueOf(SdkProperties.isTestMode());
        objArr[2] = ClientProperties.getAppName();
        objArr[3] = ClientProperties.getAppVersion();
        objArr[4] = Integer.valueOf(SdkProperties.getVersionCode());
        objArr[5] = SdkProperties.getVersionName();
        objArr[6] = Boolean.valueOf(ClientProperties.isAppDebuggable());
        objArr[7] = SdkProperties.getConfigUrl();
        objArr[8] = WebViewApp.getCurrentApp().getConfiguration().getWebViewUrl();
        objArr[9] = WebViewApp.getCurrentApp().getConfiguration().getWebViewHash();
        objArr[10] = WebViewApp.getCurrentApp().getConfiguration().getWebViewVersion();
        objArr[11] = Long.valueOf(SdkProperties.getInitializationTime());
        objArr[12] = Boolean.valueOf(SdkProperties.isReinitialized());
        objArr[13] = true;
        if (SdkProperties.getLatestConfiguration() == null) {
            z = false;
        }
        objArr[14] = Boolean.valueOf(z);
        objArr[15] = Long.valueOf(Device.getElapsedRealtime());
        objArr[16] = WebViewApp.getCurrentApp().getConfiguration().getStateId();
        objArr[17] = PrivacyConfigStorage.getInstance().getPrivacyConfig().getPrivacyStatus().toLowerCase();
        webViewCallback.invoke(objArr);
    }

    @WebViewExposed
    public static void initComplete(WebViewCallback webViewCallback) {
        DeviceLog.debug("Web Application initialized");
        SdkProperties.setInitialized(true);
        WebViewApp.getCurrentApp().setWebAppInitialized(true);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void initError(String str, Integer num, WebViewCallback webViewCallback) {
        WebViewApp.getCurrentApp().setWebAppFailureMessage(str);
        WebViewApp.getCurrentApp().setWebAppFailureCode(num.intValue());
        WebViewApp.getCurrentApp().setWebAppInitialized(false);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void setDebugMode(Boolean bool, WebViewCallback webViewCallback) {
        SdkProperties.setDebugMode(bool.booleanValue());
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getDebugMode(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(SdkProperties.getDebugMode()));
    }

    @WebViewExposed
    public static void logError(String str, WebViewCallback webViewCallback) {
        DeviceLog.error(str);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void logWarning(String str, WebViewCallback webViewCallback) {
        DeviceLog.warning(str);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void logInfo(String str, WebViewCallback webViewCallback) {
        DeviceLog.info(str);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void logDebug(String str, WebViewCallback webViewCallback) {
        DeviceLog.debug(str);
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void reinitialize(WebViewCallback webViewCallback) {
        SdkProperties.setReinitialized(true);
        InitializeThread.initialize(WebViewApp.getCurrentApp().getConfiguration());
    }

    @WebViewExposed
    public static void downloadLatestWebView(WebViewCallback webViewCallback) {
        DeviceLog.debug("Unity Ads init: WebView called download");
        webViewCallback.invoke(Integer.valueOf(InitializeThread.downloadLatestWebView().getValue()));
    }
}
