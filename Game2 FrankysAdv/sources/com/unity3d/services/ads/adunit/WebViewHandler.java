package com.unity3d.services.ads.adunit;

import android.os.Bundle;
import android.view.View;
import com.unity3d.services.core.misc.ViewUtilities;
import com.unity3d.services.core.webview.WebViewApp;

public class WebViewHandler implements IAdUnitViewHandler {
    public boolean create(AdUnitActivity adUnitActivity) {
        return true;
    }

    public void onCreate(AdUnitActivity adUnitActivity, Bundle bundle) {
    }

    public void onPause(AdUnitActivity adUnitActivity) {
    }

    public void onResume(AdUnitActivity adUnitActivity) {
    }

    public void onStart(AdUnitActivity adUnitActivity) {
    }

    public void onStop(AdUnitActivity adUnitActivity) {
    }

    public boolean destroy() {
        if (WebViewApp.getCurrentApp() == null || WebViewApp.getCurrentApp().getWebView() == null) {
            return true;
        }
        ViewUtilities.removeViewFromParent(WebViewApp.getCurrentApp().getWebView());
        return true;
    }

    public View getView() {
        if (WebViewApp.getCurrentApp() != null) {
            return WebViewApp.getCurrentApp().getWebView();
        }
        return null;
    }

    public void onDestroy(AdUnitActivity adUnitActivity) {
        destroy();
    }
}
