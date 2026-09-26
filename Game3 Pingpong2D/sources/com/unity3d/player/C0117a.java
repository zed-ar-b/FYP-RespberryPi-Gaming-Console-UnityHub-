package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.play.core.assetpacks.AssetPackLocation;
import com.google.android.play.core.assetpacks.AssetPackManager;
import com.google.android.play.core.assetpacks.AssetPackManagerFactory;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener;
import com.google.android.play.core.assetpacks.AssetPackStates;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.RuntimeExecutionException;
import com.google.android.play.core.tasks.Task;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* renamed from: com.unity3d.player.a */
final class C0117a implements C0135d {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public static C0117a f139a;

    /* renamed from: b */
    private AssetPackManager f140b;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public HashSet f141c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public Object f142d;

    /* renamed from: com.unity3d.player.a$a */
    private static class C0118a implements Runnable {

        /* renamed from: a */
        private Set f143a;

        /* renamed from: b */
        private String f144b;

        /* renamed from: c */
        private int f145c;

        /* renamed from: d */
        private long f146d;

        /* renamed from: e */
        private long f147e;

        /* renamed from: f */
        private int f148f;

        /* renamed from: g */
        private int f149g;

        C0118a(Set set, String str, int i, long j, long j2, int i2, int i3) {
            this.f143a = set;
            this.f144b = str;
            this.f145c = i;
            this.f146d = j;
            this.f147e = j2;
            this.f148f = i2;
            this.f149g = i3;
        }

        public final void run() {
            for (IAssetPackManagerDownloadStatusCallback onStatusUpdate : this.f143a) {
                onStatusUpdate.onStatusUpdate(this.f144b, this.f145c, this.f146d, this.f147e, this.f148f, this.f149g);
            }
        }
    }

    /* renamed from: com.unity3d.player.a$b */
    private class C0119b implements AssetPackStateUpdateListener {

        /* renamed from: b */
        private HashSet f151b;

        /* renamed from: c */
        private Looper f152c;

        public C0119b(C0117a aVar, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
            this(iAssetPackManagerDownloadStatusCallback, Looper.myLooper());
        }

        public C0119b(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, Looper looper) {
            HashSet hashSet = new HashSet();
            this.f151b = hashSet;
            hashSet.add(iAssetPackManagerDownloadStatusCallback);
            this.f152c = looper;
        }

        /* renamed from: a */
        private static Set m67a(HashSet hashSet) {
            return (Set) hashSet.clone();
        }

        /* access modifiers changed from: private */
        /* renamed from: a */
        public synchronized void onStateUpdate(AssetPackState assetPackState) {
            if (assetPackState.status() == 4 || assetPackState.status() == 5 || assetPackState.status() == 0) {
                synchronized (C0117a.f139a) {
                    C0117a.this.f141c.remove(assetPackState.name());
                    if (C0117a.this.f141c.isEmpty()) {
                        C0117a.this.mo336a(C0117a.this.f142d);
                        Object unused = C0117a.this.f142d = null;
                    }
                }
            }
            if (this.f151b.size() != 0) {
                new Handler(this.f152c).post(new C0118a(m67a(this.f151b), assetPackState.name(), assetPackState.status(), assetPackState.totalBytesToDownload(), assetPackState.bytesDownloaded(), assetPackState.transferProgressPercentage(), assetPackState.errorCode()));
            }
        }

        /* renamed from: a */
        public final synchronized void mo342a(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
            this.f151b.add(iAssetPackManagerDownloadStatusCallback);
        }
    }

    /* renamed from: com.unity3d.player.a$c */
    private static class C0120c implements OnSuccessListener {

        /* renamed from: a */
        private IAssetPackManagerMobileDataConfirmationCallback f153a;

        /* renamed from: b */
        private Looper f154b = Looper.myLooper();

        /* renamed from: com.unity3d.player.a$c$a */
        private static class C0121a implements Runnable {

            /* renamed from: a */
            private IAssetPackManagerMobileDataConfirmationCallback f155a;

            /* renamed from: b */
            private boolean f156b;

            C0121a(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback, boolean z) {
                this.f155a = iAssetPackManagerMobileDataConfirmationCallback;
                this.f156b = z;
            }

            public final void run() {
                this.f155a.onMobileDataConfirmationResult(this.f156b);
            }
        }

        public C0120c(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
            this.f153a = iAssetPackManagerMobileDataConfirmationCallback;
        }

        /* access modifiers changed from: private */
        /* renamed from: a */
        public void onSuccess(Integer num) {
            if (this.f153a != null) {
                new Handler(this.f154b).post(new C0121a(this.f153a, num.intValue() == -1));
            }
        }
    }

    /* renamed from: com.unity3d.player.a$d */
    private static class C0122d implements OnCompleteListener {

        /* renamed from: a */
        private IAssetPackManagerDownloadStatusCallback f157a;

        /* renamed from: b */
        private Looper f158b = Looper.myLooper();

        /* renamed from: c */
        private String f159c;

        public C0122d(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, String str) {
            this.f157a = iAssetPackManagerDownloadStatusCallback;
            this.f159c = str;
        }

        /* renamed from: a */
        private void m71a(String str, int i, int i2, long j) {
            new Handler(this.f158b).post(new C0118a(Collections.singleton(this.f157a), str, i, j, i == 4 ? j : 0, 0, i2));
        }

        public final void onComplete(Task task) {
            try {
                AssetPackStates assetPackStates = (AssetPackStates) task.getResult();
                Map packStates = assetPackStates.packStates();
                if (packStates.size() != 0) {
                    for (AssetPackState assetPackState : packStates.values()) {
                        if (assetPackState.errorCode() != 0 || assetPackState.status() == 4 || assetPackState.status() == 5 || assetPackState.status() == 0) {
                            m71a(assetPackState.name(), assetPackState.status(), assetPackState.errorCode(), assetPackStates.totalBytes());
                        } else {
                            C0117a.f139a.m56a(assetPackState.name(), this.f157a, this.f158b);
                        }
                    }
                }
            } catch (RuntimeExecutionException e) {
                m71a(this.f159c, 0, e.getErrorCode(), 0);
            }
        }
    }

    /* renamed from: com.unity3d.player.a$e */
    private static class C0123e implements OnCompleteListener {

        /* renamed from: a */
        private IAssetPackManagerStatusQueryCallback f160a;

        /* renamed from: b */
        private Looper f161b = Looper.myLooper();

        /* renamed from: c */
        private String[] f162c;

        /* renamed from: com.unity3d.player.a$e$a */
        private static class C0124a implements Runnable {

            /* renamed from: a */
            private IAssetPackManagerStatusQueryCallback f163a;

            /* renamed from: b */
            private long f164b;

            /* renamed from: c */
            private String[] f165c;

            /* renamed from: d */
            private int[] f166d;

            /* renamed from: e */
            private int[] f167e;

            C0124a(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, long j, String[] strArr, int[] iArr, int[] iArr2) {
                this.f163a = iAssetPackManagerStatusQueryCallback;
                this.f164b = j;
                this.f165c = strArr;
                this.f166d = iArr;
                this.f167e = iArr2;
            }

            public final void run() {
                this.f163a.onStatusResult(this.f164b, this.f165c, this.f166d, this.f167e);
            }
        }

        public C0123e(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, String[] strArr) {
            this.f160a = iAssetPackManagerStatusQueryCallback;
            this.f162c = strArr;
        }

        public final void onComplete(Task task) {
            if (this.f160a != null) {
                int i = 0;
                try {
                    AssetPackStates assetPackStates = (AssetPackStates) task.getResult();
                    Map packStates = assetPackStates.packStates();
                    int size = packStates.size();
                    String[] strArr = new String[size];
                    int[] iArr = new int[size];
                    int[] iArr2 = new int[size];
                    for (AssetPackState assetPackState : packStates.values()) {
                        strArr[i] = assetPackState.name();
                        iArr[i] = assetPackState.status();
                        iArr2[i] = assetPackState.errorCode();
                        i++;
                    }
                    new Handler(this.f161b).post(new C0124a(this.f160a, assetPackStates.totalBytes(), strArr, iArr, iArr2));
                } catch (RuntimeExecutionException e) {
                    String message = e.getMessage();
                    for (String str : this.f162c) {
                        if (message.contains(str)) {
                            new Handler(this.f161b).post(new C0124a(this.f160a, 0, new String[]{str}, new int[]{0}, new int[]{e.getErrorCode()}));
                            return;
                        }
                    }
                    String[] strArr2 = this.f162c;
                    int[] iArr3 = new int[strArr2.length];
                    int[] iArr4 = new int[strArr2.length];
                    for (int i2 = 0; i2 < this.f162c.length; i2++) {
                        iArr3[i2] = 0;
                        iArr4[i2] = e.getErrorCode();
                    }
                    new Handler(this.f161b).post(new C0124a(this.f160a, 0, this.f162c, iArr3, iArr4));
                }
            }
        }
    }

    private C0117a(Context context) {
        if (f139a == null) {
            this.f140b = AssetPackManagerFactory.getInstance(context);
            this.f141c = new HashSet();
            return;
        }
        throw new RuntimeException("AssetPackManagerWrapper should be created only once. Use getInstance() instead.");
    }

    /* renamed from: a */
    public static C0135d m53a(Context context) {
        if (f139a == null) {
            f139a = new C0117a(context);
        }
        return f139a;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m56a(String str, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, Looper looper) {
        synchronized (f139a) {
            if (this.f142d == null) {
                C0119b bVar = new C0119b(iAssetPackManagerDownloadStatusCallback, looper);
                this.f140b.registerListener(bVar);
                this.f142d = bVar;
            } else {
                ((C0119b) this.f142d).mo342a(iAssetPackManagerDownloadStatusCallback);
            }
            this.f141c.add(str);
            this.f140b.fetch(Collections.singletonList(str));
        }
    }

    /* renamed from: a */
    public final Object mo333a(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        C0119b bVar = new C0119b(this, iAssetPackManagerDownloadStatusCallback);
        this.f140b.registerListener(bVar);
        return bVar;
    }

    /* renamed from: a */
    public final String mo334a(String str) {
        AssetPackLocation packLocation = this.f140b.getPackLocation(str);
        return packLocation == null ? "" : packLocation.assetsPath();
    }

    /* renamed from: a */
    public final void mo335a(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        this.f140b.showCellularDataConfirmation(activity).addOnSuccessListener(new C0120c(iAssetPackManagerMobileDataConfirmationCallback));
    }

    /* renamed from: a */
    public final void mo336a(Object obj) {
        if (obj instanceof C0119b) {
            this.f140b.unregisterListener((C0119b) obj);
        }
    }

    /* renamed from: a */
    public final void mo337a(String[] strArr) {
        this.f140b.cancel(Arrays.asList(strArr));
    }

    /* renamed from: a */
    public final void mo338a(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        for (String str : strArr) {
            this.f140b.getPackStates(Collections.singletonList(str)).addOnCompleteListener(new C0122d(iAssetPackManagerDownloadStatusCallback, str));
        }
    }

    /* renamed from: a */
    public final void mo339a(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        this.f140b.getPackStates(Arrays.asList(strArr)).addOnCompleteListener(new C0123e(iAssetPackManagerStatusQueryCallback, strArr));
    }

    /* renamed from: b */
    public final void mo340b(String str) {
        this.f140b.removePack(str);
    }
}
