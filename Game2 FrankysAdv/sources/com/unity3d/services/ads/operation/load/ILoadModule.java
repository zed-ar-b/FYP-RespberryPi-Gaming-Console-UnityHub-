package com.unity3d.services.ads.operation.load;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.operation.IAdModule;

public interface ILoadModule extends IAdModule<ILoadOperation, LoadOperationState> {
    void onUnityAdsAdLoaded(String str);

    void onUnityAdsFailedToLoad(String str, UnityAds.UnityAdsLoadError unityAdsLoadError, String str2);
}
