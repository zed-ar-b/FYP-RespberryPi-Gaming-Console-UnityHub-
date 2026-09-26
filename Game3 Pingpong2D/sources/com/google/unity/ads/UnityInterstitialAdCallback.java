package com.google.unity.ads;

import com.google.android.gms.ads.LoadAdError;

public interface UnityInterstitialAdCallback extends UnityPaidEventListener, UnityFullScreenContentCallback {
    void onInterstitialAdFailedToLoad(LoadAdError loadAdError);

    void onInterstitialAdLoaded();
}
