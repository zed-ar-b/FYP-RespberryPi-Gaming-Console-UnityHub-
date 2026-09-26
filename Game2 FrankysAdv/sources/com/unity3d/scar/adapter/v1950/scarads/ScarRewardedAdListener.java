package com.unity3d.scar.adapter.v1950.scarads;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.unity3d.scar.adapter.common.IScarRewardedAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;

public class ScarRewardedAdListener {
    /* access modifiers changed from: private */
    public IScarRewardedAdListenerWrapper _adListenerWrapper;
    /* access modifiers changed from: private */
    public IScarLoadListener _loadListener;
    private RewardedAd _rewardedAd;
    private RewardedAdLoadCallback _rewardedAdLoadCallback = new RewardedAdLoadCallback() {
        public void onRewardedAdLoaded() {
            ScarRewardedAdListener.this._adListenerWrapper.onAdLoaded();
            if (ScarRewardedAdListener.this._loadListener != null) {
                ScarRewardedAdListener.this._loadListener.onAdLoaded();
            }
        }

        public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
            ScarRewardedAdListener.this._adListenerWrapper.onAdFailedToLoad(loadAdError.getCode(), loadAdError.toString());
        }
    };
    private RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
        public void onRewardedAdOpened() {
            ScarRewardedAdListener.this._adListenerWrapper.onAdOpened();
        }

        public void onRewardedAdFailedToShow(AdError adError) {
            ScarRewardedAdListener.this._adListenerWrapper.onAdFailedToShow(adError.getCode(), adError.toString());
        }

        public void onUserEarnedReward(RewardItem rewardItem) {
            ScarRewardedAdListener.this._adListenerWrapper.onUserEarnedReward();
        }

        public void onRewardedAdClosed() {
            ScarRewardedAdListener.this._adListenerWrapper.onAdClosed();
        }
    };

    public ScarRewardedAdListener(RewardedAd rewardedAd, IScarRewardedAdListenerWrapper iScarRewardedAdListenerWrapper) {
        this._rewardedAd = rewardedAd;
        this._adListenerWrapper = iScarRewardedAdListenerWrapper;
    }

    public RewardedAdCallback getRewardedAdCallback() {
        return this.rewardedAdCallback;
    }

    public RewardedAdLoadCallback getRewardedAdLoadCallback() {
        return this._rewardedAdLoadCallback;
    }

    public void setLoadListener(IScarLoadListener iScarLoadListener) {
        this._loadListener = iScarLoadListener;
    }
}
