package com.unity3d.services.core.api;

import com.unity3d.services.core.lifecycle.LifecycleError;
import com.unity3d.services.core.lifecycle.LifecycleListener;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class Lifecycle {
    private static LifecycleListener _listener;

    @WebViewExposed
    public static void register(JSONArray jSONArray, WebViewCallback webViewCallback) {
        if (ClientProperties.getApplication() == null) {
            webViewCallback.error(LifecycleError.APPLICATION_NULL, new Object[0]);
        } else if (getLifecycleListener() == null) {
            ArrayList arrayList = new ArrayList();
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    arrayList.add((String) jSONArray.get(i));
                    i++;
                } catch (JSONException unused) {
                    webViewCallback.error(LifecycleError.JSON_ERROR, new Object[0]);
                    return;
                }
            }
            setLifecycleListener(new LifecycleListener(arrayList));
            ClientProperties.getApplication().registerActivityLifecycleCallbacks(getLifecycleListener());
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(LifecycleError.LISTENER_NOT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void unregister(WebViewCallback webViewCallback) {
        if (ClientProperties.getApplication() != null) {
            if (getLifecycleListener() != null) {
                ClientProperties.getApplication().unregisterActivityLifecycleCallbacks(getLifecycleListener());
                setLifecycleListener((LifecycleListener) null);
            }
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(LifecycleError.APPLICATION_NULL, new Object[0]);
    }

    public static LifecycleListener getLifecycleListener() {
        return _listener;
    }

    public static void setLifecycleListener(LifecycleListener lifecycleListener) {
        _listener = lifecycleListener;
    }
}
