package com.unity3d.services.ads.operation.load;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.request.metrics.ISDKMetrics;
import com.unity3d.services.core.webview.bridge.IWebViewBridgeInvoker;

public class LoadModuleDecorator implements ILoadModule {
    private final ILoadModule _loadModule;

    public LoadModuleDecorator(ILoadModule iLoadModule) {
        this._loadModule = iLoadModule;
    }

    public void executeAdOperation(IWebViewBridgeInvoker iWebViewBridgeInvoker, LoadOperationState loadOperationState) {
        this._loadModule.executeAdOperation(iWebViewBridgeInvoker, loadOperationState);
    }

    public ISDKMetrics getMetricSender() {
        return this._loadModule.getMetricSender();
    }

    public void onUnityAdsAdLoaded(String str) {
        this._loadModule.onUnityAdsAdLoaded(str);
    }

    public void onUnityAdsFailedToLoad(String str, UnityAds.UnityAdsLoadError unityAdsLoadError, String str2) {
        this._loadModule.onUnityAdsFailedToLoad(str, unityAdsLoadError, str2);
    }

    public ILoadOperation get(String str) {
        return (ILoadOperation) this._loadModule.get(str);
    }

    public void set(ILoadOperation iLoadOperation) {
        this._loadModule.set(iLoadOperation);
    }

    public void remove(String str) {
        this._loadModule.remove(str);
    }
}
