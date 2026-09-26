package com.unity3d.scar.adapter.v2000.scarads;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;

public class ScarInterstitialAdListener extends ScarAdListener {
    /* access modifiers changed from: private */
    public final IScarInterstitialAdListenerWrapper _adListenerWrapper;
    private final InterstitialAdLoadCallback _adLoadCallback = new InterstitialAdLoadCallback() {
        public void onAdLoaded(InterstitialAd interstitialAd) {
            ScarInterstitialAdListener.super.onAdLoaded(interstitialAd);
            ScarInterstitialAdListener.this._adListenerWrapper.onAdLoaded();
            interstitialAd.setFullScreenContentCallback(ScarInterstitialAdListener.this._fullScreenContentCallback);
            ScarInterstitialAdListener.this._scarInterstitialAd.setGmaAd(interstitialAd);
            if (ScarInterstitialAdListener.this._loadListener != null) {
                ScarInterstitialAdListener.this._loadListener.onAdLoaded();
            }
        }

        public void onAdFailedToLoad(LoadAdError loadAdError) {
            ScarInterstitialAdListener.super.onAdFailedToLoad(loadAdError);
            ScarInterstitialAdListener.this._adListenerWrapper.onAdFailedToLoad(loadAdError.getCode(), loadAdError.toString());
        }
    };
    /* access modifiers changed from: private */
    public final FullScreenContentCallback _fullScreenContentCallback = new FullScreenContentCallback() {
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            ScarInterstitialAdListener.super.onAdFailedToShowFullScreenContent(adError);
            ScarInterstitialAdListener.this._adListenerWrapper.onAdFailedToShow(adError.getCode(), adError.toString());
        }

        public void onAdShowedFullScreenContent() {
            ScarInterstitialAdListener.super.onAdShowedFullScreenContent();
            ScarInterstitialAdListener.this._adListenerWrapper.onAdOpened();
        }

        public void onAdDismissedFullScreenContent() {
            ScarInterstitialAdListener.super.onAdDismissedFullScreenContent();
            ScarInterstitialAdListener.this._adListenerWrapper.onAdClosed();
        }

        public void onAdImpression() {
            ScarInterstitialAdListener.super.onAdImpression();
            ScarInterstitialAdListener.this._adListenerWrapper.onAdImpression();
        }
    };
    /* access modifiers changed from: private */
    public final ScarInterstitialAd _scarInterstitialAd;

    public ScarInterstitialAdListener(IScarInterstitialAdListenerWrapper iScarInterstitialAdListenerWrapper, ScarInterstitialAd scarInterstitialAd) {
        this._adListenerWrapper = iScarInterstitialAdListenerWrapper;
        this._scarInterstitialAd = scarInterstitialAd;
    }

    public InterstitialAdLoadCallback getAdLoadListener() {
        return this._adLoadCallback;
    }
}
