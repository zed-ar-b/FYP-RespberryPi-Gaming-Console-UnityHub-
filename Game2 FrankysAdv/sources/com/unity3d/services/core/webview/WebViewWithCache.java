package com.unity3d.services.core.webview;

import android.content.Context;
import android.webkit.WebSettings;

public class WebViewWithCache extends WebView {
    public WebViewWithCache(Context context, boolean z) {
        super(context, z);
        WebSettings settings = getSettings();
        settings.setCacheMode(-1);
        settings.setDomStorageEnabled(true);
        if (z) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
    }
}
