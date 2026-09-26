package com.unity3d.services.ads.webplayer;

import android.webkit.JavascriptInterface;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;

public class WebPlayerBridgeInterface {
    private final String viewId;

    public WebPlayerBridgeInterface(String str) {
        this.viewId = str;
    }

    @JavascriptInterface
    public void handleEvent(String str) {
        if (WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.WEBPLAYER_EVENT, str, this.viewId);
        }
    }
}
