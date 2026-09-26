package com.google.unity.ads;

import com.google.android.gms.ads.LoadAdError;

public interface UnityRewardedInterstitialAdCallback extends UnityPaidEventListener, UnityFullScreenContentCallback {
    void onRewardedInterstitialAdFailedToLoad(LoadAdError loadAdError);

    void onRewardedInterstitialAdLoaded();

    void onUserEarnedReward(String str, float f);
}
