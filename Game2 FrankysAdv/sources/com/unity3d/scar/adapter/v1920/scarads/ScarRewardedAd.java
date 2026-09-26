package com.unity3d.scar.adapter.v1920.scarads;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.unity3d.scar.adapter.common.GMAAdsError;
import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.IScarRewardedAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.scar.adapter.v1920.signals.QueryInfoMetadata;

public class ScarRewardedAd extends ScarAdBase {
    private RewardedAd _rewardedAd;
    private ScarRewardedAdListener _rewardedAdDelegate;

    public ScarRewardedAd(Context context, QueryInfoMetadata queryInfoMetadata, ScarAdMetadata scarAdMetadata, IAdsErrorHandler iAdsErrorHandler, IScarRewardedAdListenerWrapper iScarRewardedAdListenerWrapper) {
        super(context, scarAdMetadata, queryInfoMetadata, iAdsErrorHandler);
        RewardedAd rewardedAd = new RewardedAd(this._context, this._scarAdMetadata.getAdUnitId());
        this._rewardedAd = rewardedAd;
        this._rewardedAdDelegate = new ScarRewardedAdListener(rewardedAd, iScarRewardedAdListenerWrapper);
    }

    public void loadAdInternal(IScarLoadListener iScarLoadListener, AdRequest adRequest) {
        this._rewardedAdDelegate.setLoadListener(iScarLoadListener);
        this._rewardedAd.loadAd(adRequest, this._rewardedAdDelegate.getRewardedAdLoadCallback());
    }

    public void show(Activity activity) {
        if (this._rewardedAd.isLoaded()) {
            this._rewardedAd.show(activity, this._rewardedAdDelegate.getRewardedAdCallback());
        } else {
            this._adsErrorHandler.handleError(GMAAdsError.AdNotLoadedError(this._scarAdMetadata));
        }
    }
}
