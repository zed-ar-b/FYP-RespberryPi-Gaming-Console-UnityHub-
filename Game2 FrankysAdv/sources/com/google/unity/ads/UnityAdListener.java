package com.google.unity.ads;

import com.google.android.gms.ads.LoadAdError;

public interface UnityAdListener extends UnityPaidEventListener {
    void onAdClosed();

    void onAdFailedToLoad(LoadAdError loadAdError);

    void onAdLeftApplication();

    void onAdLoaded();

    void onAdOpened();
}
