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
final class C0050a implements C0068d {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public static C0050a f140a;

    /* renamed from: b */
    private AssetPackManager f141b;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public HashSet f142c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public Object f143d;

    /* renamed from: com.unity3d.player.a$a */
    private static class C0051a implements Runnable {

        /* renamed from: a */
        private Set f144a;

        /* renamed from: b */
        private String f145b;

        /* renamed from: c */
        private int f146c;

        /* renamed from: d */
        private long f147d;

        /* renamed from: e */
        private long f148e;

        /* renamed from: f */
        private int f149f;

        /* renamed from: g */
        private int f150g;

        C0051a(Set set, String str, int i, long j, long j2, int i2, int i3) {
            this.f144a = set;
            this.f145b = str;
            this.f146c = i;
            this.f147d = j;
            this.f148e = j2;
            this.f149f = i2;
            this.f150g = i3;
        }

        public final void run() {
            for (IAssetPackManagerDownloadStatusCallback onStatusUpdate : this.f144a) {
                onStatusUpdate.onStatusUpdate(this.f145b, this.f146c, this.f147d, this.f148e, this.f149f, this.f150g);
            }
        }
    }

    /* renamed from: com.unity3d.player.a$b */
    private class C0052b implements AssetPackStateUpdateListener {

        /* renamed from: b */
        private HashSet f152b;

        /* renamed from: c */
        private Looper f153c;

        public C0052b(C0050a aVar, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
            this(iAssetPackManagerDownloadStatusCallback, Looper.myLooper());
        }

        public C0052b(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, Looper looper) {
            HashSet hashSet = new HashSet();
            this.f152b = hashSet;
            hashSet.add(iAssetPackManagerDownloadStatusCallback);
            this.f153c = looper;
        }

        /* renamed from: a */
        private static Set m67a(HashSet hashSet) {
            return (Set) hashSet.clone();
        }

        /* access modifiers changed from: private */
        /* renamed from: a */
        public synchronized void onStateUpdate(AssetPackState assetPackState) {
            if (assetPackState.status() == 4 || assetPackState.status() == 5 || assetPackState.status() == 0) {
                synchronized (C0050a.f140a) {
                    C0050a.this.f142c.remove(assetPackState.name());
                    if (C0050a.this.f142c.isEmpty()) {
                        C0050a.this.mo238a(C0050a.this.f143d);
                        Object unused = C0050a.this.f143d = null;
                    }
                }
            }
            if (this.f152b.size() != 0) {
                new Handler(this.f153c).post(new C0051a(m67a(this.f152b), assetPackState.name(), assetPackState.status(), assetPackState.totalBytesToDownload(), assetPackState.bytesDownloaded(), assetPackState.transferProgressPercentage(), assetPackState.errorCode()));
            }
        }

        /* renamed from: a */
        public final synchronized void mo244a(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
            this.f152b.add(iAssetPackManagerDownloadStatusCallback);
        }
    }

    /* renamed from: com.unity3d.player.a$c */
    private static class C0053c implements OnSuccessListener {

        /* renamed from: a */
        private IAssetPackManagerMobileDataConfirmationCallback f154a;

        /* renamed from: b */
        private Looper f155b = Looper.myLooper();

        /* renamed from: com.unity3d.player.a$c$a */
        private static class C0054a implements Runnable {

            /* renamed from: a */
            private IAssetPackManagerMobileDataConfirmationCallback f156a;

            /* renamed from: b */
            private boolean f157b;

            C0054a(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback, boolean z) {
                this.f156a = iAssetPackManagerMobileDataConfirmationCallback;
                this.f157b = z;
            }

            public final void run() {
                this.f156a.onMobileDataConfirmationResult(this.f157b);
            }
        }

        public C0053c(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
            this.f154a = iAssetPackManagerMobileDataConfirmationCallback;
        }

        /* access modifiers changed from: private */
        /* renamed from: a */
        public void onSuccess(Integer num) {
            if (this.f154a != null) {
                new Handler(this.f155b).post(new C0054a(this.f154a, num.intValue() == -1));
            }
        }
    }

    /* renamed from: com.unity3d.player.a$d */
    private static class C0055d implements OnCompleteListener {

        /* renamed from: a */
        private IAssetPackManagerDownloadStatusCallback f158a;

        /* renamed from: b */
        private Looper f159b = Looper.myLooper();

        /* renamed from: c */
        private String f160c;

        public C0055d(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, String str) {
            this.f158a = iAssetPackManagerDownloadStatusCallback;
            this.f160c = str;
        }

        /* renamed from: a */
        private void m71a(String str, int i, int i2, long j) {
            new Handler(this.f159b).post(new C0051a(Collections.singleton(this.f158a), str, i, j, i == 4 ? j : 0, 0, i2));
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
                            C0050a.f140a.m56a(assetPackState.name(), this.f158a, this.f159b);
                        }
                    }
                }
            } catch (RuntimeExecutionException e) {
                m71a(this.f160c, 0, e.getErrorCode(), 0);
            }
        }
    }

    /* renamed from: com.unity3d.player.a$e */
    private static class C0056e implements OnCompleteListener {

        /* renamed from: a */
        private IAssetPackManagerStatusQueryCallback f161a;

        /* renamed from: b */
        private Looper f162b = Looper.myLooper();

        /* renamed from: c */
        private String[] f163c;

        /* renamed from: com.unity3d.player.a$e$a */
        private static class C0057a implements Runnable {

            /* renamed from: a */
            private IAssetPackManagerStatusQueryCallback f164a;

            /* renamed from: b */
            private long f165b;

            /* renamed from: c */
            private String[] f166c;

            /* renamed from: d */
            private int[] f167d;

            /* renamed from: e */
            private int[] f168e;

            C0057a(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, long j, String[] strArr, int[] iArr, int[] iArr2) {
                this.f164a = iAssetPackManagerStatusQueryCallback;
                this.f165b = j;
                this.f166c = strArr;
                this.f167d = iArr;
                this.f168e = iArr2;
            }

            public final void run() {
                this.f164a.onStatusResult(this.f165b, this.f166c, this.f167d, this.f168e);
            }
        }

        public C0056e(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, String[] strArr) {
            this.f161a = iAssetPackManagerStatusQueryCallback;
            this.f163c = strArr;
        }

        public final void onComplete(Task task) {
            if (this.f161a != null) {
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
                    new Handler(this.f162b).post(new C0057a(this.f161a, assetPackStates.totalBytes(), strArr, iArr, iArr2));
                } catch (RuntimeExecutionException e) {
                    String message = e.getMessage();
                    for (String str : this.f163c) {
                        if (message.contains(str)) {
                            new Handler(this.f162b).post(new C0057a(this.f161a, 0, new String[]{str}, new int[]{0}, new int[]{e.getErrorCode()}));
                            return;
                        }
                    }
                    String[] strArr2 = this.f163c;
                    int[] iArr3 = new int[strArr2.length];
                    int[] iArr4 = new int[strArr2.length];
                    for (int i2 = 0; i2 < this.f163c.length; i2++) {
                        iArr3[i2] = 0;
                        iArr4[i2] = e.getErrorCode();
                    }
                    new Handler(this.f162b).post(new C0057a(this.f161a, 0, this.f163c, iArr3, iArr4));
                }
            }
        }
    }

    private C0050a(Context context) {
        if (f140a == null) {
            this.f141b = AssetPackManagerFactory.getInstance(context);
            this.f142c = new HashSet();
            return;
        }
        throw new RuntimeException("AssetPackManagerWrapper should be created only once. Use getInstance() instead.");
    }

    /* renamed from: a */
    public static C0068d m53a(Context context) {
        if (f140a == null) {
            f140a = new C0050a(context);
        }
        return f140a;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m56a(String str, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, Looper looper) {
        synchronized (f140a) {
            if (this.f143d == null) {
                C0052b bVar = new C0052b(iAssetPackManagerDownloadStatusCallback, looper);
                this.f141b.registerListener(bVar);
                this.f143d = bVar;
            } else {
                ((C0052b) this.f143d).mo244a(iAssetPackManagerDownloadStatusCallback);
            }
            this.f142c.add(str);
            this.f141b.fetch(Collections.singletonList(str));
        }
    }

    /* renamed from: a */
    public final Object mo235a(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        C0052b bVar = new C0052b(this, iAssetPackManagerDownloadStatusCallback);
        this.f141b.registerListener(bVar);
        return bVar;
    }

    /* renamed from: a */
    public final String mo236a(String str) {
        AssetPackLocation packLocation = this.f141b.getPackLocation(str);
        return packLocation == null ? "" : packLocation.assetsPath();
    }

    /* renamed from: a */
    public final void mo237a(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        this.f141b.showCellularDataConfirmation(activity).addOnSuccessListener(new C0053c(iAssetPackManagerMobileDataConfirmationCallback));
    }

    /* renamed from: a */
    public final void mo238a(Object obj) {
        if (obj instanceof C0052b) {
            this.f141b.unregisterListener((C0052b) obj);
        }
    }

    /* renamed from: a */
    public final void mo239a(String[] strArr) {
        this.f141b.cancel(Arrays.asList(strArr));
    }

    /* renamed from: a */
    public final void mo240a(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        for (String str : strArr) {
            this.f141b.getPackStates(Collections.singletonList(str)).addOnCompleteListener(new C0055d(iAssetPackManagerDownloadStatusCallback, str));
        }
    }

    /* renamed from: a */
    public final void mo241a(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        this.f141b.getPackStates(Arrays.asList(strArr)).addOnCompleteListener(new C0056e(iAssetPackManagerStatusQueryCallback, strArr));
    }

    /* renamed from: b */
    public final void mo242b(String str) {
        this.f141b.removePack(str);
    }
}
