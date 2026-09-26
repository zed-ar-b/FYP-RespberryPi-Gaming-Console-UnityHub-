package com.unity3d.services.ads.api;

import android.os.Build;
import com.unity3d.services.ads.webplayer.WebPlayerError;
import com.unity3d.services.ads.webplayer.WebPlayerEventBridge;
import com.unity3d.services.ads.webplayer.WebPlayerSettingsCache;
import com.unity3d.services.ads.webplayer.WebPlayerView;
import com.unity3d.services.ads.webplayer.WebPlayerViewCache;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebPlayer {
    @WebViewExposed
    public static void setUrl(final String str, String str2, WebViewCallback webViewCallback) {
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str2);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.loadUrl(str);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setData(final String str, final String str2, final String str3, String str4, WebViewCallback webViewCallback) {
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str4);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.loadData(str, str2, str3);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setDataWithUrl(String str, String str2, String str3, String str4, String str5, WebViewCallback webViewCallback) {
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str5);
        if (webPlayer != null) {
            final String str6 = str;
            final String str7 = str2;
            final String str8 = str3;
            final String str9 = str4;
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.loadDataWithBaseURL(str6, str7, str8, str9, (String) null);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setSettings(final JSONObject jSONObject, final JSONObject jSONObject2, String str, WebViewCallback webViewCallback) {
        WebPlayerSettingsCache.getInstance().addWebSettings(str, jSONObject);
        WebPlayerSettingsCache.getInstance().addWebPlayerSettings(str, jSONObject2);
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.setSettings(jSONObject, jSONObject2);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setEventSettings(final JSONObject jSONObject, String str, WebViewCallback webViewCallback) {
        WebPlayerSettingsCache.getInstance().addWebPlayerEventSettings(str, jSONObject);
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.setEventSettings(jSONObject);
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void clearSettings(String str, WebViewCallback webViewCallback) {
        WebPlayerSettingsCache instance = WebPlayerSettingsCache.getInstance();
        instance.removeWebSettings(str);
        instance.removeWebPlayerSettings(str);
        instance.removeWebPlayerEventSettings(str);
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    webPlayer.setSettings(new JSONObject(), new JSONObject());
                    webPlayer.setEventSettings(new JSONObject());
                }
            });
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void getErroredSettings(String str, WebViewCallback webViewCallback) {
        WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str);
        if (webPlayer != null) {
            Map<String, String> erroredSettings = webPlayer.getErroredSettings();
            JSONObject jSONObject = new JSONObject();
            try {
                for (Map.Entry next : erroredSettings.entrySet()) {
                    jSONObject.put((String) next.getKey(), next.getValue());
                }
            } catch (Exception e) {
                DeviceLog.exception("Error forming JSON object", e);
            }
            webViewCallback.invoke(jSONObject);
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void sendEvent(JSONArray jSONArray, String str, WebViewCallback webViewCallback) {
        WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str);
        if (webPlayer != null) {
            webPlayer.sendEvent(jSONArray);
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(WebPlayerError.WEBPLAYER_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void getFrame(final String str, final String str2, WebViewCallback webViewCallback) {
        webViewCallback.invoke(new Object[0]);
        final WebPlayerView webPlayer = WebPlayerViewCache.getInstance().getWebPlayer(str2);
        if (webPlayer != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    int[] iArr = new int[2];
                    webPlayer.getLocationOnScreen(iArr);
                    WebPlayerEventBridge.sendGetFrameResponse(str, str2, iArr[0], iArr[1], webPlayer.getWidth(), webPlayer.getHeight(), Build.VERSION.SDK_INT >= 11 ? webPlayer.getAlpha() : 1.0f);
                }
            });
        }
    }
}
