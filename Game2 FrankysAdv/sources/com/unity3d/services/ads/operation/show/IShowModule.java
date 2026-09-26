package com.unity3d.services.ads.operation.show;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.operation.IAdModule;

public interface IShowModule extends IAdModule<IShowOperation, ShowOperationState> {
    void onUnityAdsShowClick(String str);

    void onUnityAdsShowComplete(String str, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState);

    void onUnityAdsShowConsent(String str);

    void onUnityAdsShowFailure(String str, UnityAds.UnityAdsShowError unityAdsShowError, String str2);

    void onUnityAdsShowStart(String str);
}
