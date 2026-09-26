package com.unity3d.services.core.webview.bridge;

import com.unity3d.services.core.webview.bridge.IWebViewSharedObject;

public interface IWebViewBridgeSharedObjectStore<T extends IWebViewSharedObject> {
    T get(String str);

    void remove(String str);

    void set(T t);
}
