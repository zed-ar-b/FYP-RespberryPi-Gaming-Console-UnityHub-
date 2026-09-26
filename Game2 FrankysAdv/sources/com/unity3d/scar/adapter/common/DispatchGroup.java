package com.unity3d.scar.adapter.common;

public class DispatchGroup {
    private Runnable _runnable;
    private int _threadCount;

    public DispatchGroup() {
        this._threadCount = 0;
        this._threadCount = 0;
    }

    public synchronized void enter() {
        this._threadCount++;
    }

    public synchronized void leave() {
        this._threadCount--;
        notifyGroup();
    }

    public void notify(Runnable runnable) {
        this._runnable = runnable;
        notifyGroup();
    }

    private void notifyGroup() {
        Runnable runnable;
        if (this._threadCount <= 0 && (runnable = this._runnable) != null) {
            runnable.run();
        }
    }
}
