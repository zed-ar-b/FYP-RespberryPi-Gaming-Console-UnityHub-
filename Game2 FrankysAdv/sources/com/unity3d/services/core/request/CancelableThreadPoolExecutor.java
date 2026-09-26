package com.unity3d.services.core.request;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CancelableThreadPoolExecutor extends ThreadPoolExecutor {
    private final List<Runnable> _activeRunnable = new LinkedList();

    public CancelableThreadPoolExecutor(int i, int i2, long j, TimeUnit timeUnit, LinkedBlockingQueue<Runnable> linkedBlockingQueue) {
        super(i, i2, j, timeUnit, linkedBlockingQueue);
    }

    /* access modifiers changed from: protected */
    public synchronized void beforeExecute(Thread thread, Runnable runnable) {
        super.beforeExecute(thread, runnable);
        this._activeRunnable.add(runnable);
    }

    /* access modifiers changed from: protected */
    public synchronized void afterExecute(Runnable runnable, Throwable th) {
        super.afterExecute(runnable, th);
        this._activeRunnable.remove(runnable);
    }

    public synchronized void cancel() {
        for (Runnable next : this._activeRunnable) {
            if (next instanceof WebRequestRunnable) {
                ((WebRequestRunnable) next).setCancelStatus(true);
            }
        }
    }
}
