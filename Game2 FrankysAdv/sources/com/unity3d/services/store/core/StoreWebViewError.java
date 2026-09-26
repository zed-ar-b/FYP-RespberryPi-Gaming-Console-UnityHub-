package com.unity3d.services.store.core;

import com.unity3d.scar.adapter.common.WebViewAdsError;
import com.unity3d.services.core.webview.WebViewEventCategory;

public class StoreWebViewError extends WebViewAdsError {
    public StoreWebViewError(Enum<?> enumR, String str, Object... objArr) {
        super(enumR, str, objArr);
    }

    public String getDomain() {
        return WebViewEventCategory.STORE.name();
    }
}
