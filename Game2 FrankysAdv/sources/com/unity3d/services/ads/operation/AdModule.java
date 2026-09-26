package com.unity3d.services.ads.operation;

import com.unity3d.services.core.request.metrics.ISDKMetrics;
import com.unity3d.services.core.webview.bridge.IWebViewSharedObject;
import com.unity3d.services.core.webview.bridge.WebViewBridgeSharedObjectStore;
import com.unity3d.services.core.webview.bridge.invocation.WebViewBridgeInvocationSingleThreadedExecutor;
import java.util.concurrent.ExecutorService;

public abstract class AdModule<T extends IWebViewSharedObject, T2> extends WebViewBridgeSharedObjectStore<T> implements IAdModule<T, T2> {
    protected ExecutorService _executorService = WebViewBridgeInvocationSingleThreadedExecutor.getInstance().getExecutorService();
    protected ISDKMetrics _sdkMetrics;

    protected AdModule(ISDKMetrics iSDKMetrics) {
        this._sdkMetrics = iSDKMetrics;
    }

    public ISDKMetrics getMetricSender() {
        return this._sdkMetrics;
    }
}
