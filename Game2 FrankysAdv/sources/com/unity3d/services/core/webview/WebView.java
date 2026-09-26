package com.unity3d.services.core.webview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.misc.ViewUtilities;
import com.unity3d.services.core.webview.bridge.WebViewBridgeInterface;
import java.lang.reflect.Method;

public class WebView extends android.webkit.WebView {
    /* access modifiers changed from: private */
    public static Method _evaluateJavascript;

    public WebView(Context context, boolean z) {
        super(context);
        WebSettings settings = getSettings();
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                _evaluateJavascript = android.webkit.WebView.class.getMethod("evaluateJavascript", new Class[]{String.class, ValueCallback.class});
            } catch (NoSuchMethodException e) {
                DeviceLog.exception("Method evaluateJavascript not found", e);
                _evaluateJavascript = null;
            }
        }
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(2);
        settings.setDatabaseEnabled(false);
        if (Build.VERSION.SDK_INT >= 11) {
            settings.setDisplayZoomControls(false);
        }
        settings.setDomStorageEnabled(false);
        if (Build.VERSION.SDK_INT >= 11) {
            settings.setEnableSmoothTransition(false);
        }
        settings.setGeolocationEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setJavaScriptEnabled(true);
        settings.setLightTouchEnabled(false);
        settings.setLoadWithOverviewMode(false);
        settings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= 17) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(1);
        }
        settings.setNeedInitialFocus(true);
        settings.setPluginState(WebSettings.PluginState.OFF);
        settings.setRenderPriority(WebSettings.RenderPriority.NORMAL);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setSupportMultipleWindows(false);
        settings.setSupportZoom(false);
        settings.setUseWideViewPort(true);
        if (z) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setInitialScale(0);
        setBackgroundColor(0);
        ViewUtilities.setBackground(this, new ColorDrawable(0));
        setBackgroundResource(0);
        addJavascriptInterface(new WebViewBridgeInterface(), "webviewbridge");
    }

    public void invokeJavascript(String str) {
        Utilities.runOnUiThread(new JavaScriptInvocation(str, this));
    }

    public void loadUrl(String str) {
        DeviceLog.debug("Loading url: " + str);
        super.loadUrl(str);
    }

    private class JavaScriptInvocation implements Runnable {
        private String _jsString = null;
        private android.webkit.WebView _webView = null;

        public JavaScriptInvocation(String str, android.webkit.WebView webView) {
            this._jsString = str;
            this._webView = webView;
        }

        public void run() {
            if (this._jsString != null) {
                try {
                    if (Build.VERSION.SDK_INT >= 19) {
                        WebView._evaluateJavascript.invoke(this._webView, new Object[]{this._jsString, null});
                        return;
                    }
                    WebView.this.loadUrl(this._jsString);
                } catch (Exception e) {
                    DeviceLog.exception("Error while processing JavaScriptString", e);
                }
            } else {
                DeviceLog.error("Could not process JavaScript, the string is NULL");
            }
        }
    }
}
