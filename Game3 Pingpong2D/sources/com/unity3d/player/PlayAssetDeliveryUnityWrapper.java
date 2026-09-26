package com.unity3d.player;

import android.app.Activity;
import android.content.Context;

class PlayAssetDeliveryUnityWrapper {

    /* renamed from: a */
    private static PlayAssetDeliveryUnityWrapper f36a;

    /* renamed from: b */
    private C0135d f37b;

    private PlayAssetDeliveryUnityWrapper(Context context) {
        if (f36a == null) {
            try {
                Class.forName("com.google.android.play.core.assetpacks.AssetPackManager");
                this.f37b = m19a(context);
            } catch (ClassNotFoundException unused) {
                this.f37b = null;
            }
        } else {
            throw new RuntimeException("PlayAssetDeliveryUnityWrapper should be created only once. Use getInstance() instead.");
        }
    }

    /* renamed from: a */
    private static C0135d m19a(Context context) {
        return C0117a.m53a(context);
    }

    /* renamed from: a */
    private void m20a() {
        if (playCoreApiMissing()) {
            throw new RuntimeException("AssetPackManager API is not available! Make sure your gradle project includes \"com.google.android.play:core\" dependency.");
        }
    }

    public static synchronized PlayAssetDeliveryUnityWrapper getInstance() {
        PlayAssetDeliveryUnityWrapper playAssetDeliveryUnityWrapper;
        Class<PlayAssetDeliveryUnityWrapper> cls = PlayAssetDeliveryUnityWrapper.class;
        synchronized (cls) {
            while (f36a == null) {
                try {
                    cls.wait(3000);
                } catch (InterruptedException e) {
                    C0137f.Log(6, e.getMessage());
                }
            }
            if (f36a != null) {
                playAssetDeliveryUnityWrapper = f36a;
            } else {
                throw new RuntimeException("PlayAssetDeliveryUnityWrapper is not yet initialised.");
            }
        }
        return playAssetDeliveryUnityWrapper;
    }

    public static synchronized PlayAssetDeliveryUnityWrapper init(Context context) {
        PlayAssetDeliveryUnityWrapper playAssetDeliveryUnityWrapper;
        Class<PlayAssetDeliveryUnityWrapper> cls = PlayAssetDeliveryUnityWrapper.class;
        synchronized (cls) {
            if (f36a == null) {
                f36a = new PlayAssetDeliveryUnityWrapper(context);
                cls.notifyAll();
                playAssetDeliveryUnityWrapper = f36a;
            } else {
                throw new RuntimeException("PlayAssetDeliveryUnityWrapper.init() should be called only once. Use getInstance() instead.");
            }
        }
        return playAssetDeliveryUnityWrapper;
    }

    public void cancelAssetPackDownload(String str) {
        cancelAssetPackDownloads(new String[]{str});
    }

    public void cancelAssetPackDownloads(String[] strArr) {
        m20a();
        this.f37b.mo337a(strArr);
    }

    public void downloadAssetPack(String str, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        downloadAssetPacks(new String[]{str}, iAssetPackManagerDownloadStatusCallback);
    }

    public void downloadAssetPacks(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        m20a();
        this.f37b.mo338a(strArr, iAssetPackManagerDownloadStatusCallback);
    }

    public String getAssetPackPath(String str) {
        m20a();
        return this.f37b.mo334a(str);
    }

    public void getAssetPackState(String str, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        getAssetPackStates(new String[]{str}, iAssetPackManagerStatusQueryCallback);
    }

    public void getAssetPackStates(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        m20a();
        this.f37b.mo339a(strArr, iAssetPackManagerStatusQueryCallback);
    }

    public boolean playCoreApiMissing() {
        return this.f37b == null;
    }

    public Object registerDownloadStatusListener(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        m20a();
        return this.f37b.mo333a(iAssetPackManagerDownloadStatusCallback);
    }

    public void removeAssetPack(String str) {
        m20a();
        this.f37b.mo340b(str);
    }

    public void requestToUseMobileData(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        m20a();
        this.f37b.mo335a(activity, iAssetPackManagerMobileDataConfirmationCallback);
    }

    public void unregisterDownloadStatusListener(Object obj) {
        m20a();
        this.f37b.mo336a(obj);
    }
}
