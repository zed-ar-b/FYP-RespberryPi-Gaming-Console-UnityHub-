package com.unity3d.services.analytics.core.api;

import com.unity3d.services.analytics.interfaces.AnalyticsError;
import com.unity3d.services.analytics.interfaces.IAnalytics;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;

public class Analytics {
    public static IAnalytics analyticsInterface;

    public static void setAnalyticsInterface(IAnalytics iAnalytics) {
        analyticsInterface = iAnalytics;
    }

    @WebViewExposed
    public static void addExtras(final String str, WebViewCallback webViewCallback) {
        if (analyticsInterface != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    Analytics.analyticsInterface.onAddExtras(str);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(AnalyticsError.API_NOT_FOUND, str);
    }
}
