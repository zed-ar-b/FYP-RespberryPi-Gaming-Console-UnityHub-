package com.unity3d.services.banners;

import com.unity3d.services.banners.BannerView;
import com.unity3d.services.core.misc.Utilities;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class BannerViewCache {
    private static BannerViewCache instance;
    private HashMap<String, WeakReference<BannerView>> _bannerViews = new HashMap<>();

    public static BannerViewCache getInstance() {
        if (instance == null) {
            instance = new BannerViewCache();
        }
        return instance;
    }

    public synchronized String addBannerView(BannerView bannerView) {
        this._bannerViews.put(bannerView.getViewId(), new WeakReference(bannerView));
        return bannerView.getViewId();
    }

    public synchronized BannerView getBannerView(String str) {
        WeakReference weakReference = this._bannerViews.get(str);
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        return (BannerView) weakReference.get();
    }

    public synchronized void removeBannerView(String str) {
        this._bannerViews.remove(str);
    }

    public synchronized boolean loadWebPlayer(String str, UnityBannerSize unityBannerSize) {
        BannerView bannerView = getBannerView(str);
        if (bannerView == null) {
            return false;
        }
        bannerView.loadWebPlayer(unityBannerSize);
        return true;
    }

    public synchronized void triggerBannerLoadEvent(String str) {
        final BannerView bannerView = getBannerView(str);
        if (!(bannerView == null || bannerView.getListener() == null)) {
            final BannerView.IListener listener = bannerView.getListener();
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    BannerView.IListener iListener = listener;
                    if (iListener != null) {
                        iListener.onBannerLoaded(bannerView);
                    }
                }
            });
        }
    }

    public synchronized void triggerBannerClickEvent(String str) {
        final BannerView bannerView = getBannerView(str);
        if (!(bannerView == null || bannerView.getListener() == null)) {
            final BannerView.IListener listener = bannerView.getListener();
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    BannerView.IListener iListener = listener;
                    if (iListener != null) {
                        iListener.onBannerClick(bannerView);
                    }
                }
            });
        }
    }

    public synchronized void triggerBannerErrorEvent(String str, final BannerErrorInfo bannerErrorInfo) {
        final BannerView bannerView = getBannerView(str);
        if (!(bannerView == null || bannerView.getListener() == null)) {
            final BannerView.IListener listener = bannerView.getListener();
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    BannerView.IListener iListener = listener;
                    if (iListener != null) {
                        iListener.onBannerFailedToLoad(bannerView, bannerErrorInfo);
                    }
                }
            });
        }
    }

    public synchronized void triggerBannerLeftApplicationEvent(String str) {
        final BannerView bannerView = getBannerView(str);
        if (!(bannerView == null || bannerView.getListener() == null)) {
            final BannerView.IListener listener = bannerView.getListener();
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    BannerView.IListener iListener = listener;
                    if (iListener != null) {
                        iListener.onBannerLeftApplication(bannerView);
                    }
                }
            });
        }
    }
}
