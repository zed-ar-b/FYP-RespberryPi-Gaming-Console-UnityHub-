package com.unity3d.player;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;

public class Camera2Wrapper implements C0136e {

    /* renamed from: a */
    private Context f13a;

    /* renamed from: b */
    private C0128c f14b = null;

    /* renamed from: c */
    private final int f15c = 100;

    public Camera2Wrapper(Context context) {
        this.f13a = context;
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
    public final void mo153a() {
        deinitCamera2Jni();
        closeCamera2();
    }

    /* renamed from: a */
    public final void mo154a(Object obj) {
        nativeSurfaceTextureReady(obj);
    }

    /* renamed from: a */
    public final void mo155a(Object obj, Object obj2, Object obj3, int i, int i2, int i3) {
        nativeFrameReady(obj, obj2, obj3, i, i2, i3);
    }

    /* access modifiers changed from: protected */
    public void closeCamera2() {
        C0128c cVar = this.f14b;
        if (cVar != null) {
            cVar.mo356b();
        }
        this.f14b = null;
    }

    /* access modifiers changed from: protected */
    public int getCamera2Count() {
        return C0128c.m74a(this.f13a);
    }

    /* access modifiers changed from: protected */
    public int getCamera2FocalLengthEquivalent(int i) {
        return C0128c.m95d(this.f13a, i);
    }

    /* access modifiers changed from: protected */
    public int[] getCamera2Resolutions(int i) {
        return C0128c.m98e(this.f13a, i);
    }

    /* access modifiers changed from: protected */
    public int getCamera2SensorOrientation(int i) {
        return C0128c.m75a(this.f13a, i);
    }

    /* access modifiers changed from: protected */
    public Object getCameraFocusArea(float f, float f2) {
        int a = m3a(f);
        int a2 = m3a(1.0f - f2);
        return new Camera.Area(new Rect(a - 100, a2 - 100, a + 100, a2 + 100), 1000);
    }

    /* access modifiers changed from: protected */
    public Rect getFrameSizeCamera2() {
        C0128c cVar = this.f14b;
        return cVar != null ? cVar.mo353a() : new Rect();
    }

    /* access modifiers changed from: protected */
    public boolean initializeCamera2(int i, int i2, int i3, int i4, int i5) {
        if (this.f14b != null || UnityPlayer.currentActivity == null) {
            return false;
        }
        C0128c cVar = new C0128c(this);
        this.f14b = cVar;
        return cVar.mo355a(this.f13a, i, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2AutoFocusPointSupported(int i) {
        return C0128c.m93c(this.f13a, i);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2FrontFacing(int i) {
        return C0128c.m91b(this.f13a, i);
    }

    /* access modifiers changed from: protected */
    public void pauseCamera2() {
        C0128c cVar = this.f14b;
        if (cVar != null) {
            cVar.mo358d();
        }
    }

    /* access modifiers changed from: protected */
    public boolean setAutoFocusPoint(float f, float f2) {
        C0128c cVar = this.f14b;
        if (cVar != null) {
            return cVar.mo354a(f, f2);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void startCamera2() {
        C0128c cVar = this.f14b;
        if (cVar != null) {
            cVar.mo357c();
        }
    }

    /* access modifiers changed from: protected */
    public void stopCamera2() {
        C0128c cVar = this.f14b;
        if (cVar != null) {
            cVar.mo359e();
        }
    }
}
