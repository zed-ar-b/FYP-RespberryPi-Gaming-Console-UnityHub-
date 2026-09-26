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
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UnityRewardedAd {
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public UnityRewardedAdCallback callback;
    /* access modifiers changed from: private */
    public RewardedAd rewardedAd;

    public void destroy() {
    }

    public UnityRewardedAd(Activity activity2, UnityRewardedAdCallback unityRewardedAdCallback) {
        this.activity = activity2;
        this.callback = unityRewardedAdCallback;
    }

    public void loadAd(final String str, final AdRequest adRequest) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                RewardedAd.load(UnityRewardedAd.this.activity, str, adRequest, new RewardedAdLoadCallback() {
                    public void onAdLoaded(RewardedAd rewardedAd) {
                        RewardedAd unused = UnityRewardedAd.this.rewardedAd = rewardedAd;
                        UnityRewardedAd.this.rewardedAd.setOnPaidEventListener(new OnPaidEventListener() {
                            public void onPaidEvent(final AdValue adValue) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedAd.this.callback != null) {
                                            UnityRewardedAd.this.callback.onPaidEvent(adValue.getPrecisionType(), adValue.getValueMicros(), adValue.getCurrencyCode());
                                        }
                                    }
                                }).start();
                            }
                        });
                        UnityRewardedAd.this.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            public void onAdFailedToShowFullScreenContent(final AdError adError) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedAd.this.callback != null) {
                                            UnityRewardedAd.this.callback.onAdFailedToShowFullScreenContent(adError);
                                        }
                                    }
                                }).start();
                            }

                            public void onAdShowedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedAd.this.callback != null) {
                                            UnityRewardedAd.this.callback.onAdShowedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdDismissedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedAd.this.callback != null) {
                                            UnityRewardedAd.this.callback.onAdDismissedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdImpression() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (UnityRewardedAd.this.callback != null) {
                                            UnityRewardedAd.this.callback.onAdImpression();
                                        }
                                    }
                                }).start();
                            }
                        });
                        new Thread(new Runnable() {
                            public void run() {
                                if (UnityRewardedAd.this.callback != null) {
                                    UnityRewardedAd.this.callback.onRewardedAdLoaded();
                                }
                            }
                        }).start();
                    }

                    public void onAdFailedToLoad(final LoadAdError loadAdError) {
                        new Thread(new Runnable() {
                            public void run() {
                                if (UnityRewardedAd.this.callback != null) {
                                    UnityRewardedAd.this.callback.onRewardedAdFailedToLoad(loadAdError);
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }

    public void show() {
        if (this.rewardedAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried to show rewarded ad before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
        } else {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    UnityRewardedAd.this.rewardedAd.show(UnityRewardedAd.this.activity, new OnUserEarnedRewardListener() {
                        public void onUserEarnedReward(final RewardItem rewardItem) {
                            new Thread(new Runnable() {
                                public void run() {
                                    if (UnityRewardedAd.this.callback != null) {
                                        UnityRewardedAd.this.callback.onUserEarnedReward(rewardItem.getType(), (float) rewardItem.getAmount());
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
        if (this.rewardedAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried set server side verification before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
        } else {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    UnityRewardedAd.this.rewardedAd.setServerSideVerificationOptions(serverSideVerificationOptions);
                }
            });
        }
    }

    public ResponseInfo getResponseInfo() {
        FutureTask futureTask = new FutureTask(new Callable<ResponseInfo>() {
            public ResponseInfo call() {
                return UnityRewardedAd.this.rewardedAd.getResponseInfo();
            }
        });
        this.activity.runOnUiThread(futureTask);
        try {
            return (ResponseInfo) futureTask.get();
        } catch (InterruptedException e) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check unity rewarded ad response info: %s", new Object[]{e.getLocalizedMessage()}));
        } catch (ExecutionException e2) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check unity rewarded ad response info: %s", new Object[]{e2.getLocalizedMessage()}));
        }
        return null;
    }

    public RewardItem getRewardItem() {
        if (this.rewardedAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried to get reward item before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
            return null;
        }
        FutureTask futureTask = new FutureTask(new Callable<RewardItem>() {
            public RewardItem call() {
                return UnityRewardedAd.this.rewardedAd.getRewardItem();
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
