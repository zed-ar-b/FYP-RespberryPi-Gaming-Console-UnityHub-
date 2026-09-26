package com.unity3d.scar.adapter.v1920.scarads;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;

public class ScarInterstitialAdListener {
    private AdListener _adListener = new AdListener() {
        public void onAdLoaded() {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdLoaded();
            if (ScarInterstitialAdListener.this._loadListener != null) {
                ScarInterstitialAdListener.this._loadListener.onAdLoaded();
            }
        }

        public void onAdFailedToLoad(int i) {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdFailedToLoad(i, "SCAR ad failed to load");
        }

        public void onAdOpened() {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdOpened();
        }

        public void onAdClicked() {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdClicked();
        }

        public void onAdLeftApplication() {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdLeftApplication();
        }

        public void onAdClosed() {
            ScarInterstitialAdListener.this._adListenerWrapper.onAdClosed();
        }
    };
    /* access modifiers changed from: private */
    public IScarInterstitialAdListenerWrapper _adListenerWrapper;
    private InterstitialAd _interstitialAd;
    /* access modifiers changed from: private */
    public IScarLoadListener _loadListener;

    public ScarInterstitialAdListener(InterstitialAd interstitialAd, IScarInterstitialAdListenerWrapper iScarInterstitialAdListenerWrapper) {
        this._interstitialAd = interstitialAd;
        this._adListenerWrapper = iScarInterstitialAdListenerWrapper;
    }

    public AdListener getAdListener() {
        return this._adListener;
    }

    public void setLoadListener(IScarLoadListener iScarLoadListener) {
        this._loadListener = iScarLoadListener;
    }
}
