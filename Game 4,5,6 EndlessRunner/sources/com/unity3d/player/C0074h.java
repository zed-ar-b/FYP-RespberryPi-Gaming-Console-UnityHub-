package com.unity3d.player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;

/* renamed from: com.unity3d.player.h */
final class C0074h implements Application.ActivityLifecycleCallbacks {

    /* renamed from: a */
    WeakReference f227a = new WeakReference((Object) null);

    /* renamed from: b */
    Activity f228b;

    /* renamed from: c */
    C0075a f229c = null;

    /* renamed from: com.unity3d.player.h$a */
    class C0075a extends View implements PixelCopy.OnPixelCopyFinishedListener {

        /* renamed from: a */
        Bitmap f230a;

        C0075a(Context context) {
            super(context);
        }

        /* renamed from: a */
        public final void mo290a(SurfaceView surfaceView) {
            Bitmap createBitmap = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
            this.f230a = createBitmap;
            PixelCopy.request(surfaceView, createBitmap, this, new Handler(Looper.getMainLooper()));
        }

        public final void onPixelCopyFinished(int i) {
            if (i == 0) {
                setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), new BitmapDrawable(getResources(), this.f230a)}));
            }
        }
    }

    C0074h(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.f228b = activity;
            activity.getApplication().registerActivityLifecycleCallbacks(this);
        }
    }

    /* renamed from: a */
    public final void mo278a() {
        Activity activity = this.f228b;
        if (activity != null) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    /* renamed from: a */
    public final void mo279a(SurfaceView surfaceView) {
        if (PlatformSupport.NOUGAT_SUPPORT) {
            if (this.f229c == null) {
                this.f229c = new C0075a(this.f228b);
            }
            this.f229c.mo290a(surfaceView);
        }
    }

    /* renamed from: a */
    public final void mo280a(ViewGroup viewGroup) {
        C0075a aVar = this.f229c;
        if (aVar != null && aVar.getParent() == null) {
            viewGroup.addView(this.f229c);
            viewGroup.bringChildToFront(this.f229c);
        }
    }

    /* renamed from: b */
    public final void mo281b() {
        this.f229c = null;
    }

    /* renamed from: b */
    public final void mo282b(ViewGroup viewGroup) {
        C0075a aVar = this.f229c;
        if (aVar != null && aVar.getParent() != null) {
            viewGroup.removeView(this.f229c);
        }
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public final void onActivityDestroyed(Activity activity) {
    }

    public final void onActivityPaused(Activity activity) {
    }

    public final void onActivityResumed(Activity activity) {
        this.f227a = new WeakReference(activity);
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }
}
