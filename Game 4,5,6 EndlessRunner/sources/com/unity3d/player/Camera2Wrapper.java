package com.unity3d.player;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;

public class Camera2Wrapper implements C0069e {

    /* renamed from: a */
    private Context f14a;

    /* renamed from: b */
    private C0061c f15b = null;

    /* renamed from: c */
    private final int f16c = 100;

    public Camera2Wrapper(Context context) {
        this.f14a = context;
        initCamera2Jni();
    }

    /* renamed from: a */
    private static int m3a(float f) {
        return (int) Math.min(Math.max((f * 2000.0f) - 0.0040893555f, -900.0f), 900.0f);
    }

    private final native void deinitCamera2Jni();

    private final native void initCamera2Jni();

    private final native void nativeFrameReady(Object obj, Object obj2, Object obj3, int i, int i2, int i3);

    private final native void nativeSurfaceTextureReady(Object obj);

    /* renamed from: a */
    public final void mo55a() {
        deinitCamera2Jni();
        closeCamera2();
    }

    /* renamed from: a */
    public final void mo56a(Object obj) {
        nativeSurfaceTextureReady(obj);
    }

    /* renamed from: a */
    public final void mo57a(Object obj, Object obj2, Object obj3, int i, int i2, int i3) {
        nativeFrameReady(obj, obj2, obj3, i, i2, i3);
    }

    /* access modifiers changed from: protected */
    public void closeCamera2() {
        C0061c cVar = this.f15b;
        if (cVar != null) {
            cVar.mo258b();
        }
        this.f15b = null;
    }

    /* access modifiers changed from: protected */
    public int getCamera2Count() {
        return C0061c.m74a(this.f14a);
    }

    /* access modifiers changed from: protected */
    public int getCamera2FocalLengthEquivalent(int i) {
        return C0061c.m95d(this.f14a, i);
    }

    /* access modifiers changed from: protected */
    public int[] getCamera2Resolutions(int i) {
        return C0061c.m98e(this.f14a, i);
    }

    /* access modifiers changed from: protected */
    public int getCamera2SensorOrientation(int i) {
        return C0061c.m75a(this.f14a, i);
    }

    /* access modifiers changed from: protected */
    public Object getCameraFocusArea(float f, float f2) {
        int a = m3a(f);
        int a2 = m3a(1.0f - f2);
        return new Camera.Area(new Rect(a - 100, a2 - 100, a + 100, a2 + 100), 1000);
    }

    /* access modifiers changed from: protected */
    public Rect getFrameSizeCamera2() {
        C0061c cVar = this.f15b;
        return cVar != null ? cVar.mo255a() : new Rect();
    }

    /* access modifiers changed from: protected */
    public boolean initializeCamera2(int i, int i2, int i3, int i4, int i5) {
        if (this.f15b != null || UnityPlayer.currentActivity == null) {
            return false;
        }
        C0061c cVar = new C0061c(this);
        this.f15b = cVar;
        return cVar.mo257a(this.f14a, i, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2AutoFocusPointSupported(int i) {
        return C0061c.m93c(this.f14a, i);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2FrontFacing(int i) {
        return C0061c.m91b(this.f14a, i);
    }

    /* access modifiers changed from: protected */
    public void pauseCamera2() {
        C0061c cVar = this.f15b;
        if (cVar != null) {
            cVar.mo260d();
        }
    }

    /* access modifiers changed from: protected */
    public boolean setAutoFocusPoint(float f, float f2) {
        C0061c cVar = this.f15b;
        if (cVar != null) {
            return cVar.mo256a(f, f2);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void startCamera2() {
        C0061c cVar = this.f15b;
        if (cVar != null) {
            cVar.mo259c();
        }
    }

    /* access modifiers changed from: protected */
    public void stopCamera2() {
        C0061c cVar = this.f15b;
        if (cVar != null) {
            cVar.mo261e();
        }
    }
}
