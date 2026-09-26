package com.unity3d.ads;

import com.unity3d.ads.UnityAds;

public interface IUnityAdsShowListener {
    void onUnityAdsShowClick(String str);

    void onUnityAdsShowComplete(String str, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState);

    void onUnityAdsShowFailure(String str, UnityAds.UnityAdsShowError unityAdsShowError, String str2);

    void onUnityAdsShowStart(String str);
}
