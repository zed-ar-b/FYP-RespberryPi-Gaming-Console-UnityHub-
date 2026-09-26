package com.unity3d.scar.adapter.v1920.scarads;

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

        public void onRewardedAdFailedToLoad(int i) {
            ScarRewardedAdListener.this._adListenerWrapper.onAdFailedToLoad(i, "SCAR ad failed to show");
        }
    };
    private RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
        public void onRewardedAdOpened() {
            ScarRewardedAdListener.this._adListenerWrapper.onAdOpened();
        }

        public void onRewardedAdFailedToShow(int i) {
            ScarRewardedAdListener.this._adListenerWrapper.onAdFailedToShow(i, "SCAR ad failed to show");
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
