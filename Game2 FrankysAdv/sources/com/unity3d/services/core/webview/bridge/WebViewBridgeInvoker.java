package com.unity3d.services.core.webview.bridge;

import com.unity3d.services.core.webview.WebViewApp;
import java.lang.reflect.Method;

public class WebViewBridgeInvoker implements IWebViewBridgeInvoker {
    public boolean invokeMethod(String str, String str2, Method method, Object... objArr) {
        return WebViewApp.getCurrentApp().invokeMethod(str, str2, method, objArr);
    }
}
