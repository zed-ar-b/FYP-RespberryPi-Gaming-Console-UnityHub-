package com.unity3d.services.core.webview.bridge;

import com.unity3d.services.core.webview.bridge.IWebViewSharedObject;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WebViewBridgeSharedObjectStore<T extends IWebViewSharedObject> implements IWebViewBridgeSharedObjectStore<T> {
    private ConcurrentHashMap<String, T> _sharedObjects = new ConcurrentHashMap<>();

    public T get(String str) {
        if (str == null) {
            return null;
        }
        return (IWebViewSharedObject) this._sharedObjects.get(str);
    }

    public void set(T t) {
        if (t != null) {
            this._sharedObjects.put(t.getId(), t);
        }
    }

    public void remove(T t) {
        if (t != null) {
            remove(t.getId());
        }
    }

    public void remove(String str) {
        this._sharedObjects.remove(str);
    }
}
