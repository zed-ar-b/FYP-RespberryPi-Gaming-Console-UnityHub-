package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.unity3d.player.UnityPermissions;

/* renamed from: com.unity3d.player.g */
public final class C0071g extends Fragment {

    /* renamed from: a */
    private final IPermissionRequestCallbacks f217a;

    /* renamed from: b */
    private final Activity f218b;

    /* renamed from: c */
    private final Looper f219c;

    /* renamed from: com.unity3d.player.g$a */
    class C0073a implements Runnable {

        /* renamed from: b */
        private IPermissionRequestCallbacks f223b;

        /* renamed from: c */
        private String f224c;

        /* renamed from: d */
        private int f225d;

        /* renamed from: e */
        private boolean f226e;

        C0073a(IPermissionRequestCallbacks iPermissionRequestCallbacks, String str, int i, boolean z) {
            this.f223b = iPermissionRequestCallbacks;
            this.f224c = str;
            this.f225d = i;
            this.f226e = z;
        }

        public final void run() {
            int i = this.f225d;
            if (i == -1) {
                if (Build.VERSION.SDK_INT >= 30 || this.f226e) {
                    this.f223b.onPermissionDenied(this.f224c);
                } else {
                    this.f223b.onPermissionDeniedAndDontAskAgain(this.f224c);
                }
            } else if (i == 0) {
                this.f223b.onPermissionGranted(this.f224c);
            }
        }
    }

    public C0071g() {
        this.f217a = null;
        this.f218b = null;
        this.f219c = null;
    }

    public C0071g(Activity activity, IPermissionRequestCallbacks iPermissionRequestCallbacks) {
        this.f217a = iPermissionRequestCallbacks;
        this.f218b = activity;
        this.f219c = Looper.myLooper();
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m127a(String[] strArr) {
        for (String onPermissionDenied : strArr) {
            this.f217a.onPermissionDenied(onPermissionDenied);
        }
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestPermissions(getArguments().getStringArray("PermissionNames"), 96489);
    }

    public final void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 96489) {
            if (strArr.length != 0) {
                int i2 = 0;
                while (i2 < strArr.length && i2 < iArr.length) {
                    IPermissionRequestCallbacks iPermissionRequestCallbacks = this.f217a;
                    if (!(iPermissionRequestCallbacks == null || this.f218b == null || this.f219c == null)) {
                        if (iPermissionRequestCallbacks instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                            iPermissionRequestCallbacks.onPermissionGranted(strArr[i2]);
                        } else {
                            String str = strArr[i2] == null ? "<null>" : strArr[i2];
                            new Handler(this.f219c).post(new C0073a(this.f217a, str, iArr[i2], this.f218b.shouldShowRequestPermissionRationale(str)));
                        }
                    }
                    i2++;
                }
            } else if (!(this.f217a == null || this.f218b == null || this.f219c == null)) {
                final String[] stringArray = getArguments().getStringArray("PermissionNames");
                if (this.f217a instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                    m127a(stringArray);
                } else {
                    new Handler(this.f219c).post(new Runnable() {
                        public final void run() {
                            C0071g.this.m127a(stringArray);
                        }
                    });
                }
            }
            FragmentTransaction beginTransaction = getActivity().getFragmentManager().beginTransaction();
            beginTransaction.remove(this);
            beginTransaction.commit();
        }
    }
}
