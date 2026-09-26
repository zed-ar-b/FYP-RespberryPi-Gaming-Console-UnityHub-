package com.unity3d.services.banners;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.properties.BannerRefreshInfo;
import com.unity3d.services.banners.view.BannerPosition;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.misc.ViewUtilities;
import com.unity3d.services.core.properties.ClientProperties;
import java.util.concurrent.TimeUnit;

public final class UnityBanners {
    private static UnityBanners instance;
    /* access modifiers changed from: private */
    public IUnityBannerListener _bannerListener;
    private BannerPosition _currentBannerPosition = BannerPosition.NONE;
    private BannerWrapper _currentBannerWrapper;

    private static UnityBanners getInstance() {
        if (instance == null) {
            instance = new UnityBanners();
        }
        return instance;
    }

    private class BannerWrapper extends FrameLayout {
        private BannerAdRefreshView _bannerAdRefreshView;
        private BannerPosition _bannerPosition = BannerPosition.NONE;

        public BannerWrapper(Context context, BannerAdRefreshView bannerAdRefreshView) {
            super(context);
            this._bannerAdRefreshView = bannerAdRefreshView;
            addView(bannerAdRefreshView);
            setupLayoutConstraints();
            setBackgroundColor(0);
        }

        public void setBannerPosition(BannerPosition bannerPosition) {
            this._bannerPosition = bannerPosition;
            setupLayoutConstraints();
        }

        private void setupLayoutConstraints() {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = this._bannerPosition.getGravity();
            setLayoutParams(layoutParams);
        }

        public void destroy() {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    this.removeAllViews();
                    ViewUtilities.removeViewFromParent(this);
                }
            });
            BannerAdRefreshView bannerAdRefreshView = this._bannerAdRefreshView;
            if (bannerAdRefreshView != null) {
                bannerAdRefreshView.destroy();
                this._bannerAdRefreshView = null;
            }
        }
    }

    private UnityBanners() {
    }

    private void _loadBanner(Activity activity, String str) {
        if (this._currentBannerWrapper == null) {
            BannerAdRefreshView bannerAdRefreshView = new BannerAdRefreshView(activity, str, new UnityBannerSize(320, 50));
            final BannerWrapper bannerWrapper = new BannerWrapper(activity, bannerAdRefreshView);
            bannerWrapper.setBannerPosition(this._currentBannerPosition);
            this._currentBannerWrapper = bannerWrapper;
            bannerAdRefreshView.setListener(new BannerView.Listener() {
                public void onBannerLoaded(BannerView bannerView) {
                    if (this._bannerListener != null) {
                        this._bannerListener.onUnityBannerLoaded(bannerView.getPlacementId(), bannerWrapper);
                    }
                }

                public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                    if (this._bannerListener != null) {
                        IUnityBannerListener access$000 = this._bannerListener;
                        access$000.onUnityBannerError(bannerView.getPlacementId() + " " + bannerErrorInfo.errorMessage);
                    }
                }

                public void onBannerClick(BannerView bannerView) {
                    if (this._bannerListener != null) {
                        this._bannerListener.onUnityBannerClick(bannerView.getPlacementId());
                    }
                }
            });
            bannerAdRefreshView.load();
            return;
        }
        sendError("A Banner is already in use, please call destroy before loading another banner!");
    }

    private void _destroy() {
        BannerWrapper bannerWrapper = this._currentBannerWrapper;
        if (bannerWrapper != null) {
            bannerWrapper.destroy();
            this._currentBannerWrapper = null;
        }
    }

    private static void sendError(final String str) {
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                IUnityBannerListener bannerListener = UnityBanners.getBannerListener();
                if (bannerListener != null) {
                    bannerListener.onUnityBannerError(str);
                }
            }
        });
    }

    @Deprecated
    public static void loadBanner(Activity activity, String str) {
        DeviceLog.entered();
        if (!UnityAds.isSupported()) {
            sendError("Unity Ads is not supported on this device.");
        }
        if (!UnityAds.isInitialized()) {
            sendError("UnityAds is not initialized.");
            return;
        }
        ClientProperties.setActivity(activity);
        getInstance()._loadBanner(activity, str);
    }

    @Deprecated
    public static void destroy() {
        getInstance()._destroy();
    }

    @Deprecated
    public static void setBannerListener(IUnityBannerListener iUnityBannerListener) {
        getInstance()._bannerListener = iUnityBannerListener;
    }

    @Deprecated
    public static IUnityBannerListener getBannerListener() {
        return getInstance()._bannerListener;
    }

    @Deprecated
    public static void setBannerPosition(BannerPosition bannerPosition) {
        getInstance()._currentBannerPosition = bannerPosition;
    }

    private class BannerAdRefreshView extends RelativeLayout {
        private BannerView bannerView;
        private boolean didLoad = false;
        private boolean didShow = false;
        private boolean didSubscribeToLifecycle = false;
        private LifecycleListener lifecycleListener;
        private String placementId;
        private Handler refreshHandler;
        private long refreshRate = 30;
        private long refreshTime;
        private Runnable reloadRunnable;

        public BannerAdRefreshView(Activity activity, String str, UnityBannerSize unityBannerSize) {
            super(activity);
            this.placementId = str;
            this.refreshHandler = new Handler();
            this.reloadRunnable = new Runnable(UnityBanners.this) {
                public void run() {
                    this.reload();
                }
            };
            setupLayoutParams();
            setBackgroundColor(0);
            BannerView bannerView2 = new BannerView(activity, str, unityBannerSize);
            this.bannerView = bannerView2;
            addView(bannerView2);
        }

        public String getPlacementId() {
            return this.bannerView.getPlacementId();
        }

        public UnityBannerSize getSize() {
            return this.bannerView.getSize();
        }

        public void setListener(BannerView.IListener iListener) {
            this.bannerView.setListener(iListener);
        }

        public BannerView.IListener getListener() {
            return this.bannerView.getListener();
        }

        public void load() {
            if (!this.didLoad) {
                this.didLoad = true;
                Integer refreshRate2 = BannerRefreshInfo.getInstance().getRefreshRate(this.placementId);
                if (refreshRate2 != null) {
                    this.refreshRate = refreshRate2.longValue();
                }
                reload();
            }
        }

        public void destroy() {
            stopReloadTask();
            this.bannerView.destroy();
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    this.removeAllViews();
                }
            });
            this.bannerView = null;
        }

        /* access modifiers changed from: private */
        public void startReloadTask() {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = this.refreshTime;
            if (uptimeMillis < j) {
                this.refreshHandler.postAtTime(this.reloadRunnable, j);
            } else {
                reload();
            }
        }

        /* access modifiers changed from: private */
        public void stopReloadTask() {
            Runnable runnable;
            Handler handler = this.refreshHandler;
            if (handler != null && (runnable = this.reloadRunnable) != null) {
                handler.removeCallbacks(runnable);
            }
        }

        /* access modifiers changed from: private */
        public void reload() {
            this.bannerView.load();
            long uptimeMillis = SystemClock.uptimeMillis() + TimeUnit.SECONDS.toMillis(this.refreshRate);
            this.refreshTime = uptimeMillis;
            this.refreshHandler.postAtTime(this.reloadRunnable, uptimeMillis);
        }

        private void subscribeToLifecycle() {
            if (!this.didSubscribeToLifecycle && Build.VERSION.SDK_INT >= 14 && ClientProperties.getApplication() != null) {
                this.lifecycleListener = new LifecycleListener() {
                    public void onActivityPaused(Activity activity) {
                        this.stopReloadTask();
                    }

                    public void onActivityStopped(Activity activity) {
                        this.stopReloadTask();
                    }

                    public void onActivityDestroyed(Activity activity) {
                        this.stopReloadTask();
                    }

                    public void onActivityResumed(Activity activity) {
                        this.startReloadTask();
                    }
                };
                this.didSubscribeToLifecycle = true;
                ClientProperties.getApplication().registerActivityLifecycleCallbacks(this.lifecycleListener);
            }
        }

        private void unsubscribeFromLifecycle() {
            if (this.didSubscribeToLifecycle && Build.VERSION.SDK_INT >= 14 && this.lifecycleListener != null && ClientProperties.getApplication() != null) {
                this.didSubscribeToLifecycle = false;
                ClientProperties.getApplication().unregisterActivityLifecycleCallbacks(this.lifecycleListener);
            }
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            stopReloadTask();
            unsubscribeFromLifecycle();
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            subscribeToLifecycle();
            if (this.didShow) {
                startReloadTask();
            } else {
                this.didShow = true;
            }
        }

        private void setupLayoutParams() {
            setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        }

        private class LifecycleListener implements Application.ActivityLifecycleCallbacks {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            private LifecycleListener() {
            }
        }
    }
}
