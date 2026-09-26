package com.unity3d.scar.adapter.v1950.scarads;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.unity3d.scar.adapter.common.GMAAdsError;
import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.scar.adapter.v1950.signals.QueryInfoMetadata;

public class ScarInterstitialAd extends ScarAdBase {
    private InterstitialAd _interstitialAd;
    private ScarInterstitialAdListener _interstitialAdDelegate;

    public ScarInterstitialAd(Context context, QueryInfoMetadata queryInfoMetadata, ScarAdMetadata scarAdMetadata, IAdsErrorHandler iAdsErrorHandler, IScarInterstitialAdListenerWrapper iScarInterstitialAdListenerWrapper) {
        super(context, scarAdMetadata, queryInfoMetadata, iAdsErrorHandler);
        InterstitialAd interstitialAd = new InterstitialAd(this._context);
        this._interstitialAd = interstitialAd;
        interstitialAd.setAdUnitId(this._scarAdMetadata.getAdUnitId());
        this._interstitialAdDelegate = new ScarInterstitialAdListener(this._interstitialAd, iScarInterstitialAdListenerWrapper);
    }

    public void loadAdInternal(IScarLoadListener iScarLoadListener, AdRequest adRequest) {
        this._interstitialAd.setAdListener(this._interstitialAdDelegate.getAdListener());
        this._interstitialAdDelegate.setLoadListener(iScarLoadListener);
        this._interstitialAd.loadAd(adRequest);
    }

    public void show(Activity activity) {
        if (this._interstitialAd.isLoaded()) {
            this._interstitialAd.show();
        } else {
            this._adsErrorHandler.handleError(GMAAdsError.AdNotLoadedError(this._scarAdMetadata));
        }
    }
}
