package com.unity3d.services.ads.webplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.misc.ViewUtilities;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebPlayerView extends WebView {
    private Map<String, String> _erroredSettings;
    /* access modifiers changed from: private */
    public Method _evaluateJavascript = null;
    private JSONObject _eventSettings;
    private Runnable _unsubscribeLayoutChange = null;
    /* access modifiers changed from: private */
    public String viewId;

    public WebPlayerView(Context context, String str, JSONObject jSONObject, JSONObject jSONObject2) {
        super(context);
        this.viewId = str;
        WebSettings settings = getSettings();
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                this._evaluateJavascript = WebView.class.getMethod("evaluateJavascript", new Class[]{String.class, ValueCallback.class});
            } catch (NoSuchMethodException e) {
                DeviceLog.exception("Method evaluateJavascript not found", e);
                this._evaluateJavascript = null;
            }
        }
        settings.setCacheMode(2);
        settings.setDatabaseEnabled(false);
        settings.setDomStorageEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.OFF);
        settings.setRenderPriority(WebSettings.RenderPriority.NORMAL);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setInitialScale(0);
        setBackgroundColor(0);
        ViewUtilities.setBackground(this, new ColorDrawable(0));
        setBackgroundResource(0);
        setSettings(jSONObject, jSONObject2);
        setWebViewClient(new WebPlayerClient());
        setWebChromeClient(new WebPlayerChromeClient());
        setDownloadListener(new WebPlayerDownloadListener());
        addJavascriptInterface(new WebPlayerBridgeInterface(str), "webplayerbridge");
        WebPlayerViewCache.getInstance().addWebPlayer(str, this);
        subscribeOnLayoutChange();
    }

    private void subscribeOnLayoutChange() {
        Runnable runnable = this._unsubscribeLayoutChange;
        if (runnable != null) {
            runnable.run();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            final C02571 r0 = new View.OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    WebPlayerView.this.onLayoutChange();
                }
            };
            addOnLayoutChangeListener(r0);
            this._unsubscribeLayoutChange = new Runnable() {
                public void run() {
                    WebPlayerView.this.removeOnLayoutChangeListener(r0);
                }
            };
        }
    }

    public void destroy() {
        super.destroy();
        WebPlayerViewCache.getInstance().removeWebPlayer(this.viewId);
        Runnable runnable = this._unsubscribeLayoutChange;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setEventSettings(JSONObject jSONObject) {
        this._eventSettings = jSONObject;
    }

    public void setSettings(JSONObject jSONObject, JSONObject jSONObject2) {
        Map<String, String> map = this._erroredSettings;
        if (map != null) {
            map.clear();
        }
        setTargetSettings(getSettings(), jSONObject);
        setTargetSettings(this, jSONObject2);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        onLayoutChange();
    }

    public void onLayoutChange() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        WebPlayerEventBridge.sendFrameUpdate(this.viewId, iArr[0], iArr[1], getWidth(), getHeight(), Build.VERSION.SDK_INT >= 11 ? getAlpha() : 1.0f);
    }

    public Map<String, String> getErroredSettings() {
        return this._erroredSettings;
    }

    private Object setTargetSettings(Object obj, JSONObject jSONObject) {
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray(next);
                    obj.getClass().getMethod(next, getTypes(jSONArray)).invoke(obj, getValues(jSONArray));
                } catch (Exception e) {
                    addErroredSetting(next, e.getMessage());
                    DeviceLog.exception("Setting errored", e);
                }
            }
        }
        return obj;
    }

    public void invokeJavascript(String str) {
        Utilities.runOnUiThread(new JavaScriptInvocation(str, this));
    }

    public void sendEvent(JSONArray jSONArray) {
        invokeJavascript("javascript:window.nativebridge.receiveEvent(" + jSONArray.toString() + ")");
    }

    private class JavaScriptInvocation implements Runnable {
        private String _jsString = null;
        private WebView _webView = null;

        public JavaScriptInvocation(String str, WebView webView) {
            this._jsString = str;
            this._webView = webView;
        }

        public void run() {
            if (this._jsString != null) {
                try {
                    if (Build.VERSION.SDK_INT >= 19) {
                        WebPlayerView.this._evaluateJavascript.invoke(this._webView, new Object[]{this._jsString, null});
                        return;
                    }
                    WebPlayerView.this.loadUrl(this._jsString);
                } catch (Exception e) {
                    DeviceLog.exception("Error while processing JavaScriptString", e);
                }
            } else {
                DeviceLog.error("Could not process JavaScript, the string is NULL");
            }
        }
    }

    private Class<?>[] getTypes(JSONArray jSONArray) throws JSONException, ClassNotFoundException {
        if (jSONArray == null) {
            return null;
        }
        Class<?>[] clsArr = new Class[jSONArray.length()];
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                if (jSONArray.get(i) instanceof JSONObject) {
                    Class<?> cls = Class.forName(((JSONObject) jSONArray.get(i)).getString("className"));
                    if (cls != null) {
                        clsArr[i] = cls;
                    }
                } else {
                    clsArr[i] = getPrimitiveClass(jSONArray.get(i).getClass());
                }
            }
        }
        return clsArr;
    }

    public Class<?> getPrimitiveClass(Class<?> cls) {
        String name = cls.getName();
        if (name.equals("java.lang.Byte")) {
            return Byte.TYPE;
        }
        if (name.equals("java.lang.Short")) {
            return Short.TYPE;
        }
        if (name.equals("java.lang.Integer")) {
            return Integer.TYPE;
        }
        if (name.equals("java.lang.Long")) {
            return Long.TYPE;
        }
        if (name.equals("java.lang.Character")) {
            return Character.TYPE;
        }
        if (name.equals("java.lang.Float")) {
            return Float.TYPE;
        }
        if (name.equals("java.lang.Double")) {
            return Double.TYPE;
        }
        if (name.equals("java.lang.Boolean")) {
            return Boolean.TYPE;
        }
        return name.equals("java.lang.Void") ? Void.TYPE : cls;
    }

    private Object[] getValues(JSONArray jSONArray) throws JSONException, ClassNotFoundException, NoSuchMethodException {
        Class<?> cls;
        if (jSONArray == null) {
            return null;
        }
        Object[] objArr = new Object[jSONArray.length()];
        Object[] objArr2 = new Object[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            if (jSONArray.get(i) instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                Object obj = jSONObject.get("value");
                String string = jSONObject.getString("type");
                String string2 = jSONObject.has("className") ? jSONObject.getString("className") : null;
                if (!(string2 == null || !string.equals("Enum") || (cls = Class.forName(string2)) == null)) {
                    objArr2[i] = Enum.valueOf(cls, (String) obj);
                }
            } else {
                objArr2[i] = jSONArray.get(i);
            }
        }
        if (jSONArray != null) {
            System.arraycopy(objArr2, 0, objArr, 0, jSONArray.length());
        }
        return objArr;
    }

    private void addErroredSetting(String str, String str2) {
        if (this._erroredSettings == null) {
            this._erroredSettings = new HashMap();
        }
        this._erroredSettings.put(str, str2);
    }

    /* access modifiers changed from: private */
    public boolean shouldCallSuper(String str) {
        try {
            if (this._eventSettings == null || !this._eventSettings.has(str) || !this._eventSettings.getJSONObject(str).has("callSuper")) {
                return true;
            }
            return this._eventSettings.getJSONObject(str).getBoolean("callSuper");
        } catch (Exception e) {
            DeviceLog.exception("Error getting super call status", e);
            return true;
        }
    }

    /* access modifiers changed from: private */
    public boolean shouldSendEvent(String str) {
        try {
            if (this._eventSettings == null || !this._eventSettings.has(str) || !this._eventSettings.getJSONObject(str).has("sendEvent")) {
                return false;
            }
            return this._eventSettings.getJSONObject(str).getBoolean("sendEvent");
        } catch (Exception e) {
            DeviceLog.exception("Error getting send event status", e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public <T> T getReturnValue(String str, Class<T> cls, T t) {
        try {
            if (this._eventSettings != null && this._eventSettings.has(str) && this._eventSettings.getJSONObject(str).has("returnValue")) {
                return cls.cast(this._eventSettings.getJSONObject(str).get("returnValue"));
            }
        } catch (Exception e) {
            DeviceLog.exception("Error getting default return value", e);
        }
        return t;
    }

    /* access modifiers changed from: private */
    public boolean hasReturnValue(String str) {
        try {
            if (this._eventSettings == null || !this._eventSettings.has(str) || !this._eventSettings.getJSONObject(str).has("returnValue")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            DeviceLog.exception("Error getting default return value", e);
            return false;
        }
    }

    private class WebPlayerClient extends WebViewClient {
        private WebPlayerClient() {
        }

        public boolean onRenderProcessGone(final WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    ViewUtilities.removeViewFromParent(webView);
                    webView.destroy();
                }
            });
            String access$400 = WebPlayerView.this.viewId;
            String url = WebPlayerView.this.getUrl();
            WebPlayerEventBridge.error(access$400, url, "UnityAds Sdk WebPlayer onRenderProcessGone : " + renderProcessGoneDetail.toString());
            DeviceLog.error("UnityAds Sdk WebPlayer onRenderProcessGone : " + renderProcessGoneDetail.toString());
            return true;
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (WebPlayerView.this.shouldCallSuper("onPageStarted")) {
                super.onPageStarted(webView, str, bitmap);
            }
            if (WebPlayerView.this.shouldSendEvent("onPageStarted")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.PAGE_STARTED, str, WebPlayerView.this.viewId);
            }
        }

        public void onPageFinished(WebView webView, String str) {
            if (WebPlayerView.this.shouldCallSuper("onPageFinished")) {
                super.onPageFinished(webView, str);
            }
            if (WebPlayerView.this.shouldSendEvent("onPageFinished")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.PAGE_FINISHED, str, WebPlayerView.this.viewId);
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedError")) {
                super.onReceivedError(webView, i, str, str2);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedError")) {
                WebPlayerEventBridge.error(WebPlayerView.this.viewId, str2, str);
            }
        }

        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedError")) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedError")) {
                String str = "";
                String charSequence = (webResourceError == null || webResourceError.getDescription() == null) ? str : webResourceError.getDescription().toString();
                if (!(webResourceRequest == null || webResourceRequest.getUrl() == null)) {
                    str = webResourceRequest.getUrl().toString();
                }
                WebPlayerEventBridge.error(WebPlayerView.this.viewId, str, charSequence);
            }
        }

        public void onLoadResource(WebView webView, String str) {
            if (WebPlayerView.this.shouldCallSuper("onLoadResource")) {
                super.onLoadResource(webView, str);
            }
            if (WebPlayerView.this.shouldSendEvent("onLoadResource")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.LOAD_RESOUCE, str, WebPlayerView.this.viewId);
            }
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            DeviceLog.error("Received SSL error for '%s': %s", sslError.getUrl(), sslError.toString());
            if (WebPlayerView.this.shouldSendEvent("onReceivedSslError")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.SSL_ERROR, sslError != null ? sslError.getUrl() : "", WebPlayerView.this.viewId);
            }
        }

        public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
            int i;
            String str;
            if (WebPlayerView.this.shouldCallSuper("onReceivedClientCertRequest")) {
                super.onReceivedClientCertRequest(webView, clientCertRequest);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedClientCertRequest")) {
                if (clientCertRequest != null) {
                    str = clientCertRequest.getHost();
                    i = clientCertRequest.getPort();
                } else {
                    str = "";
                    i = -1;
                }
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.CLIENT_CERT_REQUEST, str, Integer.valueOf(i), WebPlayerView.this.viewId);
            }
        }

        public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedHttpAuthRequest")) {
                super.onReceivedHttpAuthRequest(webView, httpAuthHandler, str, str2);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedHttpAuthRequest")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.HTTP_AUTH_REQUEST, str, str2, WebPlayerView.this.viewId);
            }
        }

        public void onScaleChanged(WebView webView, float f, float f2) {
            if (WebPlayerView.this.shouldCallSuper("onScaleChanged")) {
                super.onScaleChanged(webView, f, f2);
            }
            if (WebPlayerView.this.shouldSendEvent("onScaleChanged")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.SCALE_CHANGED, Float.valueOf(f), Float.valueOf(f2), WebPlayerView.this.viewId);
            }
        }

        public void onReceivedLoginRequest(WebView webView, String str, String str2, String str3) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedLoginRequest")) {
                super.onReceivedLoginRequest(webView, str, str2, str3);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedLoginRequest")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.LOGIN_REQUEST, str, str2, str3, WebPlayerView.this.viewId);
            }
        }

        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedHttpError")) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedHttpError")) {
                String str = "";
                String uri = (webResourceRequest == null || webResourceRequest.getUrl() == null) ? str : webResourceRequest.getUrl().toString();
                int i = -1;
                if (webResourceResponse != null) {
                    i = webResourceResponse.getStatusCode();
                    str = webResourceResponse.getReasonPhrase();
                }
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.HTTP_ERROR, uri, str, Integer.valueOf(i), WebPlayerView.this.viewId);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean shouldOverrideUrlLoading(android.webkit.WebView r9, android.webkit.WebResourceRequest r10) {
            /*
                r8 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "shouldOverrideUrlLoading"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r9 = super.shouldOverrideUrlLoading(r9, r10)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r9)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r9 = r9.shouldSendEvent(r3)
                r2 = 1
                if (r9 == 0) goto L_0x0047
                com.unity3d.services.core.webview.WebViewApp r9 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r4 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r5 = com.unity3d.services.ads.webplayer.WebPlayerEvent.SHOULD_OVERRIDE_URL_LOADING
                r6 = 3
                java.lang.Object[] r6 = new java.lang.Object[r6]
                android.net.Uri r7 = r10.getUrl()
                java.lang.String r7 = r7.toString()
                r6[r0] = r7
                java.lang.String r10 = r10.getMethod()
                r6[r2] = r10
                r10 = 2
                com.unity3d.services.ads.webplayer.WebPlayerView r0 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r0 = r0.viewId
                r6[r10] = r0
                r9.sendEvent(r4, r5, r6)
            L_0x0047:
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r9 = r9.hasReturnValue(r3)
                if (r9 == 0) goto L_0x005e
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r10 = java.lang.Boolean.class
                java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
                java.lang.Object r9 = r9.getReturnValue(r3, r10, r0)
                r1 = r9
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x005e:
                boolean r9 = r1.booleanValue()
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerClient.shouldOverrideUrlLoading(android.webkit.WebView, android.webkit.WebResourceRequest):boolean");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean shouldOverrideUrlLoading(android.webkit.WebView r8, java.lang.String r9) {
            /*
                r7 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "shouldOverrideUrlLoading"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r8 = super.shouldOverrideUrlLoading(r8, r9)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r8)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.shouldSendEvent(r3)
                r2 = 1
                if (r8 == 0) goto L_0x0038
                com.unity3d.services.core.webview.WebViewApp r8 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r4 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r5 = com.unity3d.services.ads.webplayer.WebPlayerEvent.SHOULD_OVERRIDE_URL_LOADING
                r6 = 2
                java.lang.Object[] r6 = new java.lang.Object[r6]
                r6[r0] = r9
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r9 = r9.viewId
                r6[r2] = r9
                r8.sendEvent(r4, r5, r6)
            L_0x0038:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.hasReturnValue(r3)
                if (r8 == 0) goto L_0x004f
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r9 = java.lang.Boolean.class
                java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
                java.lang.Object r8 = r8.getReturnValue(r3, r9, r0)
                r1 = r8
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x004f:
                boolean r8 = r1.booleanValue()
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerClient.shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String):boolean");
        }

        public void onPageCommitVisible(WebView webView, String str) {
            if (WebPlayerView.this.shouldCallSuper("onPageCommitVisible")) {
                super.onPageCommitVisible(webView, str);
            }
            if (WebPlayerView.this.shouldSendEvent("onPageCommitVisible")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.PAGE_COMMIT_VISIBLE, str, WebPlayerView.this.viewId);
            }
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            WebResourceResponse shouldInterceptRequest = WebPlayerView.this.shouldCallSuper("shouldInterceptRequest") ? super.shouldInterceptRequest(webView, webResourceRequest) : null;
            if (WebPlayerView.this.shouldSendEvent("shouldInterceptRequest")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.SHOULD_INTERCEPT_REQUEST, webResourceRequest.getUrl().toString(), WebPlayerView.this.viewId);
            }
            return shouldInterceptRequest;
        }

        public void onFormResubmission(WebView webView, Message message, Message message2) {
            if (WebPlayerView.this.shouldCallSuper("onFormResubmission")) {
                super.onFormResubmission(webView, message, message2);
            }
            if (WebPlayerView.this.shouldSendEvent("onFormResubmission")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.FORM_RESUBMISSION, WebPlayerView.this.viewId);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean shouldOverrideKeyEvent(android.webkit.WebView r9, android.view.KeyEvent r10) {
            /*
                r8 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "shouldOverrideKeyEvent"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r9 = super.shouldOverrideKeyEvent(r9, r10)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r9)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r9 = r9.shouldSendEvent(r3)
                r2 = 1
                if (r9 == 0) goto L_0x004b
                com.unity3d.services.core.webview.WebViewApp r9 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r4 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r5 = com.unity3d.services.ads.webplayer.WebPlayerEvent.SHOULD_OVERRIDE_KEY_EVENT
                r6 = 3
                java.lang.Object[] r6 = new java.lang.Object[r6]
                int r7 = r10.getKeyCode()
                java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
                r6[r0] = r7
                int r10 = r10.getAction()
                java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
                r6[r2] = r10
                r10 = 2
                com.unity3d.services.ads.webplayer.WebPlayerView r0 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r0 = r0.viewId
                r6[r10] = r0
                r9.sendEvent(r4, r5, r6)
            L_0x004b:
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r9 = r9.hasReturnValue(r3)
                if (r9 == 0) goto L_0x0062
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r10 = java.lang.Boolean.class
                java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
                java.lang.Object r9 = r9.getReturnValue(r3, r10, r0)
                r1 = r9
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x0062:
                boolean r9 = r1.booleanValue()
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerClient.shouldOverrideKeyEvent(android.webkit.WebView, android.view.KeyEvent):boolean");
        }

        public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
            if (WebPlayerView.this.shouldCallSuper("onUnhandledKeyEvent")) {
                super.onUnhandledKeyEvent(webView, keyEvent);
            }
            if (WebPlayerView.this.shouldSendEvent("onUnhandledKeyEvent")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.UNHANDLED_KEY_EVENT, Integer.valueOf(keyEvent.getKeyCode()), Integer.valueOf(keyEvent.getAction()), WebPlayerView.this.viewId);
            }
        }
    }

    private class WebPlayerChromeClient extends WebChromeClient {
        private WebPlayerChromeClient() {
        }

        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            if (WebPlayerView.this.shouldCallSuper("onGeolocationPermissionsShowPrompt")) {
                super.onGeolocationPermissionsShowPrompt(str, callback);
            }
            if (WebPlayerView.this.shouldSendEvent("onGeolocationPermissionsShowPrompt")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.GEOLOCATION_PERMISSIONS_SHOW, str, WebPlayerView.this.viewId);
            }
        }

        public void onPermissionRequest(PermissionRequest permissionRequest) {
            if (WebPlayerView.this.shouldCallSuper("onPermissionRequest")) {
                super.onPermissionRequest(permissionRequest);
            }
            if (WebPlayerView.this.shouldSendEvent("onPermissionRequest")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.PERMISSION_REQUEST, (permissionRequest == null || permissionRequest.getOrigin() == null) ? "" : permissionRequest.getOrigin().toString(), WebPlayerView.this.viewId);
            }
        }

        public void onProgressChanged(WebView webView, int i) {
            if (WebPlayerView.this.shouldCallSuper("onProgressChanged")) {
                super.onProgressChanged(webView, i);
            }
            if (WebPlayerView.this.shouldSendEvent("onProgressChanged")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.PROGRESS_CHANGED, Integer.valueOf(i), WebPlayerView.this.viewId);
            }
        }

        public void onReceivedTitle(WebView webView, String str) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedTitle")) {
                super.onReceivedTitle(webView, str);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedTitle")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.RECEIVED_TITLE, str, WebPlayerView.this.viewId);
            }
        }

        public void onReceivedIcon(WebView webView, Bitmap bitmap) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedIcon")) {
                super.onReceivedIcon(webView, bitmap);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedIcon")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.RECEIVED_ICON, WebPlayerView.this.viewId);
            }
        }

        public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
            if (WebPlayerView.this.shouldCallSuper("onReceivedTouchIconUrl")) {
                super.onReceivedTouchIconUrl(webView, str, z);
            }
            if (WebPlayerView.this.shouldSendEvent("onReceivedTouchIconUrl")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.RECEIVED_TOUCH_ICON_URL, str, Boolean.valueOf(z), WebPlayerView.this.viewId);
            }
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            if (WebPlayerView.this.shouldCallSuper("onShowCustomView")) {
                super.onShowCustomView(view, customViewCallback);
            }
            if (WebPlayerView.this.shouldSendEvent("onShowCustomView")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.SHOW_CUSTOM_VIEW, WebPlayerView.this.viewId);
            }
        }

        public void onHideCustomView() {
            if (WebPlayerView.this.shouldCallSuper("onHideCustomView")) {
                super.onHideCustomView();
            }
            if (WebPlayerView.this.shouldSendEvent("onHideCustomView")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.HIDE_CUSTOM_VIEW, WebPlayerView.this.viewId);
            }
        }

        public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
            Boolean valueOf = WebPlayerView.this.shouldCallSuper("onCreateWindow") ? Boolean.valueOf(super.onCreateWindow(webView, z, z2, message)) : false;
            if (WebPlayerView.this.shouldSendEvent("onCreateWindow")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.CREATE_WINDOW, Boolean.valueOf(z), Boolean.valueOf(z2), message, WebPlayerView.this.viewId);
            }
            if (WebPlayerView.this.hasReturnValue("onCreateWindow")) {
                valueOf = (Boolean) WebPlayerView.this.getReturnValue("onCreateWindow", Boolean.class, false);
            }
            return valueOf.booleanValue();
        }

        public void onRequestFocus(WebView webView) {
            if (WebPlayerView.this.shouldCallSuper("onRequestFocus")) {
                super.onRequestFocus(webView);
            }
            if (WebPlayerView.this.shouldSendEvent("onRequestFocus")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.REQUEST_FOCUS, WebPlayerView.this.viewId);
            }
        }

        public void onCloseWindow(WebView webView) {
            if (WebPlayerView.this.shouldCallSuper("onCloseWindow")) {
                super.onCloseWindow(webView);
            }
            if (WebPlayerView.this.shouldSendEvent("onCloseWindow")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.CLOSE_WINDOW, WebPlayerView.this.viewId);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onJsAlert(android.webkit.WebView r8, java.lang.String r9, java.lang.String r10, android.webkit.JsResult r11) {
            /*
                r7 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "onJsAlert"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r8 = super.onJsAlert(r8, r9, r10, r11)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r8)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.shouldSendEvent(r3)
                r2 = 1
                if (r8 == 0) goto L_0x003e
                com.unity3d.services.core.webview.WebViewApp r8 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r4 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r5 = com.unity3d.services.ads.webplayer.WebPlayerEvent.JS_ALERT
                r6 = 4
                java.lang.Object[] r6 = new java.lang.Object[r6]
                r6[r0] = r9
                r6[r2] = r10
                r9 = 2
                r6[r9] = r11
                r9 = 3
                com.unity3d.services.ads.webplayer.WebPlayerView r10 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r10 = r10.viewId
                r6[r9] = r10
                r8.sendEvent(r4, r5, r6)
            L_0x003e:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.hasReturnValue(r3)
                if (r8 == 0) goto L_0x0055
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r9 = java.lang.Boolean.class
                java.lang.Boolean r10 = java.lang.Boolean.valueOf(r2)
                java.lang.Object r8 = r8.getReturnValue(r3, r9, r10)
                r1 = r8
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x0055:
                boolean r8 = r1.booleanValue()
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerChromeClient.onJsAlert(android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.JsResult):boolean");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onJsConfirm(android.webkit.WebView r7, java.lang.String r8, java.lang.String r9, android.webkit.JsResult r10) {
            /*
                r6 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "onJsConfirm"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r7 = super.onJsConfirm(r7, r8, r9, r10)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r7)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r7 = r7.shouldSendEvent(r3)
                r10 = 1
                if (r7 == 0) goto L_0x003b
                com.unity3d.services.core.webview.WebViewApp r7 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r2 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r4 = com.unity3d.services.ads.webplayer.WebPlayerEvent.JS_CONFIRM
                r5 = 3
                java.lang.Object[] r5 = new java.lang.Object[r5]
                r5[r0] = r8
                r5[r10] = r9
                r8 = 2
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r9 = r9.viewId
                r5[r8] = r9
                r7.sendEvent(r2, r4, r5)
            L_0x003b:
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r7 = r7.hasReturnValue(r3)
                if (r7 == 0) goto L_0x0052
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r8 = java.lang.Boolean.class
                java.lang.Boolean r9 = java.lang.Boolean.valueOf(r10)
                java.lang.Object r7 = r7.getReturnValue(r3, r8, r9)
                r1 = r7
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x0052:
                boolean r7 = r1.booleanValue()
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerChromeClient.onJsConfirm(android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.JsResult):boolean");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onJsPrompt(android.webkit.WebView r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, android.webkit.JsPromptResult r11) {
            /*
                r6 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "onJsPrompt"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r7 = super.onJsPrompt(r7, r8, r9, r10, r11)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r7)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r7 = r7.shouldSendEvent(r3)
                r11 = 1
                if (r7 == 0) goto L_0x003e
                com.unity3d.services.core.webview.WebViewApp r7 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r2 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r4 = com.unity3d.services.ads.webplayer.WebPlayerEvent.JS_PROMPT
                r5 = 4
                java.lang.Object[] r5 = new java.lang.Object[r5]
                r5[r0] = r8
                r5[r11] = r9
                r8 = 2
                r5[r8] = r10
                r8 = 3
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r9 = r9.viewId
                r5[r8] = r9
                r7.sendEvent(r2, r4, r5)
            L_0x003e:
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r7 = r7.hasReturnValue(r3)
                if (r7 == 0) goto L_0x0055
                com.unity3d.services.ads.webplayer.WebPlayerView r7 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r8 = java.lang.Boolean.class
                java.lang.Boolean r9 = java.lang.Boolean.valueOf(r11)
                java.lang.Object r7 = r7.getReturnValue(r3, r8, r9)
                r1 = r7
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x0055:
                boolean r7 = r1.booleanValue()
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerChromeClient.onJsPrompt(android.webkit.WebView, java.lang.String, java.lang.String, java.lang.String, android.webkit.JsPromptResult):boolean");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v5, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onConsoleMessage(android.webkit.ConsoleMessage r9) {
            /*
                r8 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "onConsoleMessage"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r1 = super.onConsoleMessage(r9)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r2 = r2.shouldSendEvent(r3)
                r4 = 1
                if (r2 == 0) goto L_0x0041
                if (r9 == 0) goto L_0x0027
                java.lang.String r9 = r9.message()
                goto L_0x0029
            L_0x0027:
                java.lang.String r9 = ""
            L_0x0029:
                com.unity3d.services.core.webview.WebViewApp r2 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r5 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r6 = com.unity3d.services.ads.webplayer.WebPlayerEvent.CONSOLE_MESSAGE
                r7 = 2
                java.lang.Object[] r7 = new java.lang.Object[r7]
                r7[r0] = r9
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r9 = r9.viewId
                r7[r4] = r9
                r2.sendEvent(r5, r6, r7)
            L_0x0041:
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r9 = r9.hasReturnValue(r3)
                if (r9 == 0) goto L_0x0058
                com.unity3d.services.ads.webplayer.WebPlayerView r9 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r0 = java.lang.Boolean.class
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r4)
                java.lang.Object r9 = r9.getReturnValue(r3, r0, r1)
                r1 = r9
                java.lang.Boolean r1 = (java.lang.Boolean) r1
            L_0x0058:
                boolean r9 = r1.booleanValue()
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerChromeClient.onConsoleMessage(android.webkit.ConsoleMessage):boolean");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Boolean} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onShowFileChooser(android.webkit.WebView r8, android.webkit.ValueCallback<android.net.Uri[]> r9, android.webkit.WebChromeClient.FileChooserParams r10) {
            /*
                r7 = this;
                r0 = 0
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
                com.unity3d.services.ads.webplayer.WebPlayerView r2 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r3 = "onShowFileChooser"
                boolean r2 = r2.shouldCallSuper(r3)
                if (r2 == 0) goto L_0x0017
                boolean r8 = super.onShowFileChooser(r8, r9, r10)
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r8)
            L_0x0017:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.shouldSendEvent(r3)
                r10 = 1
                if (r8 == 0) goto L_0x0035
                com.unity3d.services.core.webview.WebViewApp r8 = com.unity3d.services.core.webview.WebViewApp.getCurrentApp()
                com.unity3d.services.core.webview.WebViewEventCategory r2 = com.unity3d.services.core.webview.WebViewEventCategory.WEBPLAYER
                com.unity3d.services.ads.webplayer.WebPlayerEvent r4 = com.unity3d.services.ads.webplayer.WebPlayerEvent.SHOW_FILE_CHOOSER
                java.lang.Object[] r5 = new java.lang.Object[r10]
                com.unity3d.services.ads.webplayer.WebPlayerView r6 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.String r6 = r6.viewId
                r5[r0] = r6
                r8.sendEvent(r2, r4, r5)
            L_0x0035:
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                boolean r8 = r8.hasReturnValue(r3)
                if (r8 == 0) goto L_0x0056
                com.unity3d.services.ads.webplayer.WebPlayerView r8 = com.unity3d.services.ads.webplayer.WebPlayerView.this
                java.lang.Class<java.lang.Boolean> r0 = java.lang.Boolean.class
                java.lang.Boolean r10 = java.lang.Boolean.valueOf(r10)
                java.lang.Object r8 = r8.getReturnValue(r3, r0, r10)
                r1 = r8
                java.lang.Boolean r1 = (java.lang.Boolean) r1
                boolean r8 = r1.booleanValue()
                if (r8 == 0) goto L_0x0056
                r8 = 0
                r9.onReceiveValue(r8)
            L_0x0056:
                boolean r8 = r1.booleanValue()
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.webplayer.WebPlayerView.WebPlayerChromeClient.onShowFileChooser(android.webkit.WebView, android.webkit.ValueCallback, android.webkit.WebChromeClient$FileChooserParams):boolean");
        }
    }

    private class WebPlayerDownloadListener implements DownloadListener {
        private WebPlayerDownloadListener() {
        }

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (WebPlayerView.this.shouldSendEvent("onDownloadStart")) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.WEBPLAYER, WebPlayerEvent.DOWNLOAD_START, str, str2, str3, str4, Long.valueOf(j), WebPlayerView.this.viewId);
            }
        }
    }
}
