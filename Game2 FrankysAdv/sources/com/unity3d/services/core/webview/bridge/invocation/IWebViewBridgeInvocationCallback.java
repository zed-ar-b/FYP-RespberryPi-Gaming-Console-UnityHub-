package com.unity3d.services.core.webview.bridge.invocation;

import com.unity3d.services.core.webview.bridge.CallbackStatus;

public interface IWebViewBridgeInvocationCallback {
    void onFailure(String str, CallbackStatus callbackStatus);

    void onSuccess();

    void onTimeout();
}
