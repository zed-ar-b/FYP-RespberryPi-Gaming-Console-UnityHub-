package com.unity3d.services.ads.adunit;

import android.os.ConditionVariable;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.request.metrics.AdOperationError;
import com.unity3d.services.core.request.metrics.AdOperationMetric;
import com.unity3d.services.core.request.metrics.SDKMetrics;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.bridge.CallbackStatus;
import java.lang.reflect.Method;
import org.json.JSONObject;

public class AdUnitOpen {
    private static Configuration _configuration;
    private static ConditionVariable _waitShowStatus;

    public static synchronized boolean open(String str, JSONObject jSONObject) throws NoSuchMethodException {
        boolean block;
        Class<AdUnitOpen> cls = AdUnitOpen.class;
        synchronized (cls) {
            Method method = cls.getMethod("showCallback", new Class[]{CallbackStatus.class});
            _waitShowStatus = new ConditionVariable();
            if (_configuration == null) {
                _configuration = new Configuration();
            }
            WebViewApp.getCurrentApp().invokeMethod("webview", "show", method, str, jSONObject);
            block = _waitShowStatus.block((long) _configuration.getShowTimeout());
            _waitShowStatus = null;
            if (!block) {
                SDKMetrics.getInstance().sendMetric(AdOperationMetric.newAdShowFailure(AdOperationError.timeout, Long.valueOf((long) _configuration.getShowTimeout())));
            }
        }
        return block;
    }

    public static void showCallback(CallbackStatus callbackStatus) {
        if (_waitShowStatus != null && callbackStatus.equals(CallbackStatus.OK)) {
            _waitShowStatus.open();
        }
    }

    public static void setConfiguration(Configuration configuration) {
        _configuration = configuration;
    }
}
