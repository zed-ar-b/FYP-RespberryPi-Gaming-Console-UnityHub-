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
final class C0143h implements Application.ActivityLifecycleCallbacks {

    /* renamed from: a */
    WeakReference f226a = new WeakReference((Object) null);

    /* renamed from: b */
    Activity f227b;

    /* renamed from: c */
    C0144a f228c = null;

    /* renamed from: com.unity3d.player.h$a */
    class C0144a extends View implements PixelCopy.OnPixelCopyFinishedListener {

        /* renamed from: a */
        Bitmap f229a;

        C0144a(Context context) {
            super(context);
        }

        /* renamed from: a */
        public final void mo416a(SurfaceView surfaceView) {
            Bitmap createBitmap = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
            this.f229a = createBitmap;
            PixelCopy.request(surfaceView, createBitmap, this, new Handler(Looper.getMainLooper()));
        }

        public final void onPixelCopyFinished(int i) {
            if (i == 0) {
                setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), new BitmapDrawable(getResources(), this.f229a)}));
            }
        }
    }

    C0143h(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.f227b = activity;
            activity.getApplication().registerActivityLifecycleCallbacks(this);
        }
    }

    /* renamed from: a */
    public final void mo404a() {
        Activity activity = this.f227b;
        if (activity != null) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    /* renamed from: a */
    public final void mo405a(SurfaceView surfaceView) {
        if (PlatformSupport.NOUGAT_SUPPORT) {
            if (this.f228c == null) {
                this.f228c = new C0144a(this.f227b);
            }
            this.f228c.mo416a(surfaceView);
        }
    }

    /* renamed from: a */
    public final void mo406a(ViewGroup viewGroup) {
        C0144a aVar = this.f228c;
        if (aVar != null && aVar.getParent() == null) {
            viewGroup.addView(this.f228c);
            viewGroup.bringChildToFront(this.f228c);
        }
    }

    /* renamed from: b */
    public final void mo407b() {
        this.f228c = null;
    }

    /* renamed from: b */
    public final void mo408b(ViewGroup viewGroup) {
        C0144a aVar = this.f228c;
        if (aVar != null && aVar.getParent() != null) {
            viewGroup.removeView(this.f228c);
        }
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public final void onActivityDestroyed(Activity activity) {
    }

    public final void onActivityPaused(Activity activity) {
    }

    public final void onActivityResumed(Activity activity) {
        this.f226a = new WeakReference(activity);
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }
}
