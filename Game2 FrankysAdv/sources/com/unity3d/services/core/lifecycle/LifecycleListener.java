package com.unity3d.services.core.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import java.util.ArrayList;

public class LifecycleListener implements Application.ActivityLifecycleCallbacks {
    private ArrayList<String> _events;

    public LifecycleListener(ArrayList<String> arrayList) {
        this._events = arrayList;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (this._events.contains("onActivityCreated") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.CREATED, activity.getClass().getName());
        }
    }

    public void onActivityStarted(Activity activity) {
        if (this._events.contains("onActivityStarted") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.STARTED, activity.getClass().getName());
        }
    }

    public void onActivityResumed(Activity activity) {
        if (this._events.contains("onActivityResumed") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.RESUMED, activity.getClass().getName());
        }
    }

    public void onActivityPaused(Activity activity) {
        if (this._events.contains("onActivityPaused") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.PAUSED, activity.getClass().getName());
        }
    }

    public void onActivityStopped(Activity activity) {
        if (this._events.contains("onActivityStopped") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.STOPPED, activity.getClass().getName());
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (this._events.contains("onActivitySaveInstanceState") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.SAVE_INSTANCE_STATE, activity.getClass().getName());
        }
    }

    public void onActivityDestroyed(Activity activity) {
        if (this._events.contains("onActivityDestroyed") && WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.LIFECYCLE, LifecycleEvent.DESTROYED, activity.getClass().getName());
        }
    }
}
