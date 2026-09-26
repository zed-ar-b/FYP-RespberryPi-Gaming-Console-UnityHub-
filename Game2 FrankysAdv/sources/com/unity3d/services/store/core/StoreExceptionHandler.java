package com.unity3d.services.store.core;

import com.unity3d.scar.adapter.common.WebViewAdsError;
import com.unity3d.services.ads.gmascar.handlers.WebViewErrorHandler;
import com.unity3d.services.store.StoreError;
import com.unity3d.services.store.StoreEvent;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONException;

public class StoreExceptionHandler {
    private final WebViewErrorHandler _webViewErrorHandler;

    public StoreExceptionHandler(WebViewErrorHandler webViewErrorHandler) {
        this._webViewErrorHandler = webViewErrorHandler;
    }

    public void handleStoreException(StoreEvent storeEvent, int i, Exception exc) {
        sendErrorToWebView(storeEvent, getStoreError(exc), i, exc);
    }

    private void sendErrorToWebView(StoreEvent storeEvent, StoreError storeError, int i, Exception exc) {
        this._webViewErrorHandler.handleError((WebViewAdsError) new StoreWebViewError(storeEvent, exc.getMessage(), Integer.valueOf(i), storeError, exc.getMessage()));
    }

    private StoreError getStoreError(Exception exc) {
        StoreError storeError = StoreError.UNKNOWN_ERROR;
        if (exc instanceof NoSuchMethodException) {
            return StoreError.NO_SUCH_METHOD;
        }
        if (exc instanceof IllegalAccessException) {
            return StoreError.ILLEGAL_ACCESS;
        }
        if (exc instanceof JSONException) {
            return StoreError.JSON_ERROR;
        }
        if (exc instanceof InvocationTargetException) {
            return StoreError.INVOCATION_TARGET;
        }
        return exc instanceof ClassNotFoundException ? StoreError.CLASS_NOT_FOUND : storeError;
    }
}
