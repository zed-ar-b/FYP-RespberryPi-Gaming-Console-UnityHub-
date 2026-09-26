package com.google.unity.ads;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UnityRewardedInterstitialAd {
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public UnityRewardedInterstitialAdCallback callback;
    /* access modifiers changed from: private */
    public RewardedInterstitialAd rewardedInterstitialAd;

    public void destroy() {
    }

    public UnityRewardedInterstitialAd(Activity activity2, UnityRewardedInterstitialAdCallback unityRewardedInterstitialAdCallback) {
        this.activity = activity2;
        this.callback = unityRewardedInterstitialAdCallback;
    }

    public void loadAd(final String str, final AdRequest adRequest) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                RewardedInterstitialAd.load(UnityRewardedInterstitialAd.this.activity, str, adRequest, new RewardedInterstitialAdLoadCallback() {
                    public void onAdLoaded(RewardedInterstitialAd rewardedInterstitialAd) {
                        RewardedInterstitialAd unused = UnityRewardedInterstitialAd.this.rewardedInterstitialAd = rewardedInterstitialAd;
                        UnityRewardedInterstitialAd.this.rewardedInterstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                            public void onPaidEvent(final AdValue adValue) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedInterstitialAd.this.callback != null) {
                                            UnityRewardedInterstitialAd.this.callback.onPaidEvent(adValue.getPrecisionType(), adValue.getValueMicros(), adValue.getCurrencyCode());
                                        }
                                    }
                                }).start();
                            }
                        });
                        UnityRewardedInterstitialAd.this.rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            public void onAdFailedToShowFullScreenContent(final AdError adError) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedInterstitialAd.this.callback != null) {
                                            UnityRewardedInterstitialAd.this.callback.onAdFailedToShowFullScreenContent(adError);
                                        }
                                    }
                                }).start();
                            }

                            public void onAdShowedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedInterstitialAd.this.callback != null) {
                                            UnityRewardedInterstitialAd.this.callback.onAdShowedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdDismissedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedInterstitialAd.this.callback != null) {
                                            UnityRewardedInterstitialAd.this.callback.onAdDismissedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdImpression() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedInterstitialAd.this.callback != null) {
                                            UnityRewardedInterstitialAd.this.callback.onAdImpression();
                                        }
                                    }
                                }).start();
                            }
                        });
                        new Thread(new Runnable() {
                            public void run() {
                                if (UnityRewardedInterstitialAd.this.callback != null) {
                                    UnityRewardedInterstitialAd.this.callback.onRewardedInterstitialAdLoaded();
                                }
                            }
                        }).start();
                    }

                    public void onAdFailedToLoad(final LoadAdError loadAdError) {
                        new Thread(new Runnable() {
                            public void run() {
                                if (UnityRewardedInterstitialAd.this.callback != null) {
                                    UnityRewardedInterstitialAd.this.callback.onRewardedInterstitialAdFailedToLoad(loadAdError);
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }

    public void show() {
        if (this.rewardedInterstitialAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried to show rewarded interstitial ad before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
        } else {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    UnityRewardedInterstitialAd.this.rewardedInterstitialAd.show(UnityRewardedInterstitialAd.this.activity, new OnUserEarnedRewardListener() {
                        public void onUserEarnedReward(final RewardItem rewardItem) {
                            new Thread(new Runnable() {
                                public void run() {
                                    if (UnityRewardedInterstitialAd.this.callback != null) {
                                        UnityRewardedInterstitialAd.this.callback.onUserEarnedReward(rewardItem.getType(), (float) rewardItem.getAmount());
                                    }
                                }
                            }).start();
                        }
                    });
                }
            });
        }
    }

    public void setServerSideVerificationOptions(final ServerSideVerificationOptions serverSideVerificationOptions) {
        if (this.rewardedInterstitialAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried set server side verification before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
        } else {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    UnityRewardedInterstitialAd.this.rewardedInterstitialAd.setServerSideVerificationOptions(serverSideVerificationOptions);
                }
            });
        }
    }

    public ResponseInfo getResponseInfo() {
        if (this.rewardedInterstitialAd == null) {
            return null;
        }
        FutureTask futureTask = new FutureTask(new Callable<ResponseInfo>() {
            public ResponseInfo call() {
                return UnityRewardedInterstitialAd.this.rewardedInterstitialAd.getResponseInfo();
            }
        });
        this.activity.runOnUiThread(futureTask);
        try {
            return (ResponseInfo) futureTask.get();
        } catch (InterruptedException e) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check unity rewarded interstitial ad response info: %s", new Object[]{e.getLocalizedMessage()}));
            return null;
        } catch (ExecutionException e2) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check unity rewarded interstitial ad response info: %s", new Object[]{e2.getLocalizedMessage()}));
            return null;
        }
    }

    public RewardItem getRewardItem() {
        if (this.rewardedInterstitialAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried to get reward item before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
            return null;
        }
        FutureTask futureTask = new FutureTask(new Callable<RewardItem>() {
            public RewardItem call() {
                return UnityRewardedInterstitialAd.this.rewardedInterstitialAd.getRewardItem();
            }
        });
        this.activity.runOnUiThread(futureTask);
        try {
            return (RewardItem) futureTask.get();
        } catch (InterruptedException e) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to get reward item: %s", new Object[]{e.getLocalizedMessage()}));
            return null;
        } catch (ExecutionException e2) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to get reward item: %s", new Object[]{e2.getLocalizedMessage()}));
            return null;
        }
    }
}
