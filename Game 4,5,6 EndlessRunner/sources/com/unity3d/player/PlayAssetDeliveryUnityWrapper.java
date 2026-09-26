package com.unity3d.player;

import android.app.Activity;
import android.content.Context;

class PlayAssetDeliveryUnityWrapper {

    /* renamed from: a */
    private static PlayAssetDeliveryUnityWrapper f37a;

    /* renamed from: b */
    private C0068d f38b;

    private PlayAssetDeliveryUnityWrapper(Context context) {
        if (f37a == null) {
            try {
                Class.forName("com.google.android.play.core.assetpacks.AssetPackManager");
                this.f38b = m19a(context);
            } catch (ClassNotFoundException unused) {
                this.f38b = null;
            }
        } else {
            throw new RuntimeException("PlayAssetDeliveryUnityWrapper should be created only once. Use getInstance() instead.");
        }
    }

    /* renamed from: a */
    private static C0068d m19a(Context context) {
        return C0050a.m53a(context);
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
            while (f37a == null) {
                try {
                    cls.wait(3000);
                } catch (InterruptedException e) {
                    C0070f.Log(6, e.getMessage());
                }
            }
            if (f37a != null) {
                playAssetDeliveryUnityWrapper = f37a;
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
            if (f37a == null) {
                f37a = new PlayAssetDeliveryUnityWrapper(context);
                cls.notifyAll();
                playAssetDeliveryUnityWrapper = f37a;
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
        this.f38b.mo239a(strArr);
    }

    public void downloadAssetPack(String str, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        downloadAssetPacks(new String[]{str}, iAssetPackManagerDownloadStatusCallback);
    }

    public void downloadAssetPacks(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        m20a();
        this.f38b.mo240a(strArr, iAssetPackManagerDownloadStatusCallback);
    }

    public String getAssetPackPath(String str) {
        m20a();
        return this.f38b.mo236a(str);
    }

    public void getAssetPackState(String str, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        getAssetPackStates(new String[]{str}, iAssetPackManagerStatusQueryCallback);
    }

    public void getAssetPackStates(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        m20a();
        this.f38b.mo241a(strArr, iAssetPackManagerStatusQueryCallback);
    }

    public boolean playCoreApiMissing() {
        return this.f38b == null;
    }

    public Object registerDownloadStatusListener(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        m20a();
        return this.f38b.mo235a(iAssetPackManagerDownloadStatusCallback);
    }

    public void removeAssetPack(String str) {
        m20a();
        this.f38b.mo242b(str);
    }

    public void requestToUseMobileData(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        m20a();
        this.f38b.mo237a(activity, iAssetPackManagerMobileDataConfirmationCallback);
    }

    public void unregisterDownloadStatusListener(Object obj) {
        m20a();
        this.f38b.mo238a(obj);
    }
}
