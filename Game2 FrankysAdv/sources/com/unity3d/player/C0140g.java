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
public final class C0140g extends Fragment {

    /* renamed from: a */
    private final IPermissionRequestCallbacks f216a;

    /* renamed from: b */
    private final Activity f217b;

    /* renamed from: c */
    private final Looper f218c;

    /* renamed from: com.unity3d.player.g$a */
    class C0142a implements Runnable {

        /* renamed from: b */
        private IPermissionRequestCallbacks f222b;

        /* renamed from: c */
        private String f223c;

        /* renamed from: d */
        private int f224d;

        /* renamed from: e */
        private boolean f225e;

        C0142a(IPermissionRequestCallbacks iPermissionRequestCallbacks, String str, int i, boolean z) {
            this.f222b = iPermissionRequestCallbacks;
            this.f223c = str;
            this.f224d = i;
            this.f225e = z;
        }

        public final void run() {
            int i = this.f224d;
            if (i == -1) {
                if (Build.VERSION.SDK_INT >= 30 || this.f225e) {
                    this.f222b.onPermissionDenied(this.f223c);
                } else {
                    this.f222b.onPermissionDeniedAndDontAskAgain(this.f223c);
                }
            } else if (i == 0) {
                this.f222b.onPermissionGranted(this.f223c);
            }
        }
    }

    public C0140g() {
        this.f216a = null;
        this.f217b = null;
        this.f218c = null;
    }

    public C0140g(Activity activity, IPermissionRequestCallbacks iPermissionRequestCallbacks) {
        this.f216a = iPermissionRequestCallbacks;
        this.f217b = activity;
        this.f218c = Looper.myLooper();
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m127a(String[] strArr) {
        for (String onPermissionDenied : strArr) {
            this.f216a.onPermissionDenied(onPermissionDenied);
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
                    IPermissionRequestCallbacks iPermissionRequestCallbacks = this.f216a;
                    if (!(iPermissionRequestCallbacks == null || this.f217b == null || this.f218c == null)) {
                        if (iPermissionRequestCallbacks instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                            iPermissionRequestCallbacks.onPermissionGranted(strArr[i2]);
                        } else {
                            String str = strArr[i2] == null ? "<null>" : strArr[i2];
                            new Handler(this.f218c).post(new C0142a(this.f216a, str, iArr[i2], this.f217b.shouldShowRequestPermissionRationale(str)));
                        }
                    }
                    i2++;
                }
            } else if (!(this.f216a == null || this.f217b == null || this.f218c == null)) {
                final String[] stringArray = getArguments().getStringArray("PermissionNames");
                if (this.f216a instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                    m127a(stringArray);
                } else {
                    new Handler(this.f218c).post(new Runnable() {
                        public final void run() {
                            C0140g.this.m127a(stringArray);
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
