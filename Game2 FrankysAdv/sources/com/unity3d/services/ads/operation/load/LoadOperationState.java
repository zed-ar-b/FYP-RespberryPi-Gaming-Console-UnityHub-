package com.unity3d.services.ads.operation.load;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsLoadOptions;
import com.unity3d.services.ads.operation.OperationState;
import com.unity3d.services.core.configuration.Configuration;

public class LoadOperationState extends OperationState {
    public IUnityAdsLoadListener listener;
    public UnityAdsLoadOptions loadOptions;

    public LoadOperationState(String str, IUnityAdsLoadListener iUnityAdsLoadListener, UnityAdsLoadOptions unityAdsLoadOptions, Configuration configuration) {
        super(str, configuration);
        this.listener = iUnityAdsLoadListener;
        this.loadOptions = unityAdsLoadOptions;
    }

    public void onUnityAdsFailedToLoad(UnityAds.UnityAdsLoadError unityAdsLoadError, String str) {
        IUnityAdsLoadListener iUnityAdsLoadListener = this.listener;
        if (iUnityAdsLoadListener != null) {
            iUnityAdsLoadListener.onUnityAdsFailedToLoad(this.placementId, unityAdsLoadError, str);
        }
    }

    public void onUnityAdsAdLoaded() {
        IUnityAdsLoadListener iUnityAdsLoadListener = this.listener;
        if (iUnityAdsLoadListener != null) {
            iUnityAdsLoadListener.onUnityAdsAdLoaded(this.placementId);
        }
    }
}
