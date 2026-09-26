package com.google.unity.ads;

import com.google.android.gms.ads.AdError;

public interface UnityFullScreenContentCallback {
    void onAdDismissedFullScreenContent();

    void onAdFailedToShowFullScreenContent(AdError adError);

    void onAdImpression();

    void onAdShowedFullScreenContent();
}
