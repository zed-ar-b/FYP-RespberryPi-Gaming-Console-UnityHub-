package com.unity3d.services.core.configuration;

import android.os.Build;
import android.webkit.JavascriptInterface;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.SdkProperties;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class EnvironmentCheck {
    public static boolean isEnvironmentOk() {
        return testProGuard() && testCacheDirectory();
    }

    public static boolean testProGuard() {
        try {
            Class<?> cls = Class.forName("com.unity3d.services.core.webview.bridge.WebViewBridgeInterface");
            Method method = cls.getMethod("handleInvocation", new Class[]{String.class});
            Method method2 = cls.getMethod("handleCallback", new Class[]{String.class, String.class, String.class});
            if (!hasJavascriptInterface(method) || !hasJavascriptInterface(method2)) {
                DeviceLog.error("Unity Ads ProGuard check fail: missing @JavascriptInterface annotations in Unity Ads web bridge");
                return false;
            }
            DeviceLog.debug("Unity Ads ProGuard check OK");
            return true;
        } catch (ClassNotFoundException e) {
            DeviceLog.exception("Unity Ads ProGuard check fail: Unity Ads web bridge class not found", e);
            return false;
        } catch (NoSuchMethodException e2) {
            DeviceLog.exception("Unity Ads ProGuard check fail: Unity Ads web bridge methods not found", e2);
            return false;
        } catch (Exception e3) {
            DeviceLog.exception("Unknown exception during Unity Ads ProGuard check: " + e3.getMessage(), e3);
            return true;
        }
    }

    public static boolean testCacheDirectory() {
        if (SdkProperties.getCacheDirectory() != null) {
            DeviceLog.debug("Unity Ads cache directory check OK");
            return true;
        }
        DeviceLog.error("Unity Ads cache directory check fail: no working cache directory available");
        return false;
    }

    private static boolean hasJavascriptInterface(Method method) {
        if (Build.VERSION.SDK_INT < 17) {
            return true;
        }
        Annotation[] annotations = method.getAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof JavascriptInterface) {
                    return true;
                }
            }
        }
        return false;
    }
}
