package com.google.unity.ads;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Interstitial {
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public UnityInterstitialAdCallback callback;
    /* access modifiers changed from: private */
    public InterstitialAd interstitialAd;

    public void destroy() {
    }

    public Interstitial(Activity activity2, UnityInterstitialAdCallback unityInterstitialAdCallback) {
        this.activity = activity2;
        this.callback = unityInterstitialAdCallback;
    }

    public void loadAd(final String str, final AdRequest adRequest) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                InterstitialAd.load(Interstitial.this.activity, str, adRequest, new InterstitialAdLoadCallback() {
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        InterstitialAd unused = Interstitial.this.interstitialAd = interstitialAd;
                        Interstitial.this.interstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                            public void onPaidEvent(final AdValue adValue) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (Interstitial.this.callback != null) {
                                            Interstitial.this.callback.onPaidEvent(adValue.getPrecisionType(), adValue.getValueMicros(), adValue.getCurrencyCode());
                                        }
                                    }
                                }).start();
                            }
                        });
                        Interstitial.this.interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            public void onAdFailedToShowFullScreenContent(final AdError adError) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (Interstitial.this.callback != null) {
                                            Interstitial.this.callback.onAdFailedToShowFullScreenContent(adError);
                                        }
                                    }
                                }).start();
                            }

                            public void onAdShowedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (Interstitial.this.callback != null) {
                                            Interstitial.this.callback.onAdShowedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdDismissedFullScreenContent() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (Interstitial.this.callback != null) {
                                            Interstitial.this.callback.onAdDismissedFullScreenContent();
                                        }
                                    }
                                }).start();
                            }

                            public void onAdImpression() {
                                new Thread(new Runnable() {
                                    public void run() {
                                        if (Interstitial.this.callback != null) {
                                            Interstitial.this.callback.onAdImpression();
                                        }
                                    }
                                }).start();
                            }
                        });
                        new Thread(new Runnable() {
                            public void run() {
                                if (Interstitial.this.callback != null) {
                                    Interstitial.this.callback.onInterstitialAdLoaded();
                                }
                            }
                        }).start();
                    }

                    public void onAdFailedToLoad(final LoadAdError loadAdError) {
                        new Thread(new Runnable() {
                            public void run() {
                                if (Interstitial.this.callback != null) {
                                    Interstitial.this.callback.onInterstitialAdFailedToLoad(loadAdError);
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }

    public ResponseInfo getResponseInfo() {
        FutureTask futureTask = new FutureTask(new Callable<ResponseInfo>() {
            public ResponseInfo call() {
                return Interstitial.this.interstitialAd.getResponseInfo();
            }
        });
        this.activity.runOnUiThread(futureTask);
        try {
            return (ResponseInfo) futureTask.get();
        } catch (InterruptedException e) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check interstitial response info: %s", new Object[]{e.getLocalizedMessage()}));
        } catch (ExecutionException e2) {
            Log.e(PluginUtils.LOGTAG, String.format("Unable to check interstitial response info: %s", new Object[]{e2.getLocalizedMessage()}));
        }
        return null;
    }

    public void show() {
        if (this.interstitialAd == null) {
            Log.e(PluginUtils.LOGTAG, "Tried to show interstitial ad before it was ready. This should in theory never happen. If it does, please contact the plugin owners.");
        } else {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    Interstitial.this.interstitialAd.show(Interstitial.this.activity);
                }
            });
        }
    }
}
