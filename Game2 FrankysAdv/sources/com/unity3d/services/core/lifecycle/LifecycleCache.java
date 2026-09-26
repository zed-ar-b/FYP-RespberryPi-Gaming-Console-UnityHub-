package com.unity3d.services.core.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Set;

public class LifecycleCache implements Application.ActivityLifecycleCallbacks {
    private boolean _appActive = true;
    private Set<IAppActiveListener> _appActiveListeners = new HashSet();
    private LifecycleEvent _currentState = LifecycleEvent.RESUMED;

    public void onActivityCreated(Activity activity, Bundle bundle) {
        this._currentState = LifecycleEvent.CREATED;
    }

    public void onActivityStarted(Activity activity) {
        this._currentState = LifecycleEvent.STARTED;
    }

    public void onActivityResumed(Activity activity) {
        this._currentState = LifecycleEvent.RESUMED;
        this._appActive = true;
        notifyListeners();
    }

    public void onActivityPaused(Activity activity) {
        this._currentState = LifecycleEvent.PAUSED;
        this._appActive = false;
        notifyListeners();
    }

    public void onActivityStopped(Activity activity) {
        this._currentState = LifecycleEvent.STOPPED;
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this._currentState = LifecycleEvent.SAVE_INSTANCE_STATE;
    }

    public void onActivityDestroyed(Activity activity) {
        this._currentState = LifecycleEvent.DESTROYED;
    }

    public LifecycleEvent getCurrentState() {
        return this._currentState;
    }

    public boolean isAppActive() {
        return this._appActive;
    }

    public synchronized void notifyListeners() {
        LifecycleEvent lifecycleEvent = this._appActive ? LifecycleEvent.RESUMED : LifecycleEvent.PAUSED;
        for (IAppActiveListener onAppStateChanged : this._appActiveListeners) {
            onAppStateChanged.onAppStateChanged(lifecycleEvent);
        }
    }

    public synchronized void addListener(IAppActiveListener iAppActiveListener) {
        this._appActiveListeners.add(iAppActiveListener);
    }

    public synchronized void removeListener(IAppActiveListener iAppActiveListener) {
        this._appActiveListeners.remove(iAppActiveListener);
    }
}
