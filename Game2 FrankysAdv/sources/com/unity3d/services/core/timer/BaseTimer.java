package com.unity3d.services.core.timer;

import com.unity3d.services.core.lifecycle.CachedLifecycle;
import com.unity3d.services.core.lifecycle.IAppActiveListener;
import com.unity3d.services.core.lifecycle.LifecycleCache;
import com.unity3d.services.core.lifecycle.LifecycleEvent;
import com.unity3d.services.core.log.DeviceLog;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseTimer implements IBaseTimer, IAppActiveListener {
    final Integer _delayMs;
    private final AtomicBoolean _hasPaused;
    private final AtomicBoolean _isRunning;
    private final LifecycleCache _lifecycleCache;
    Integer _remainingDurationMs;
    private ScheduledFuture<?> _task;
    private ITimerListener _timerListener;
    private ScheduledExecutorService _timerService;
    final Integer _totalDurationMs;

    public BaseTimer(Integer num, ITimerListener iTimerListener, LifecycleCache lifecycleCache) {
        this._delayMs = 1000;
        this._isRunning = new AtomicBoolean(false);
        this._hasPaused = new AtomicBoolean(false);
        this._totalDurationMs = num;
        this._remainingDurationMs = num;
        this._timerListener = iTimerListener;
        this._lifecycleCache = lifecycleCache;
        if (lifecycleCache != null) {
            lifecycleCache.addListener(this);
        }
    }

    public BaseTimer(Integer num, ITimerListener iTimerListener) {
        this(num, iTimerListener, CachedLifecycle.getLifecycleListener());
    }

    public void start(ScheduledExecutorService scheduledExecutorService) {
        if (this._isRunning.compareAndSet(false, true)) {
            this._timerService = scheduledExecutorService;
            schedule();
        }
    }

    public void stop() {
        ScheduledExecutorService scheduledExecutorService = this._timerService;
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            this._timerService.shutdown();
            this._timerService = null;
        }
        this._isRunning.getAndSet(false);
    }

    public boolean pause() {
        ScheduledFuture<?> scheduledFuture = this._task;
        boolean z = true;
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            z = false;
        } else {
            this._task.cancel(true);
            this._task = null;
        }
        this._isRunning.getAndSet(false);
        return z;
    }

    public boolean resume() {
        boolean z;
        ScheduledExecutorService scheduledExecutorService = this._timerService;
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            z = false;
        } else {
            z = true;
            schedule();
        }
        this._isRunning.getAndSet(z);
        return z;
    }

    public void restart(ScheduledExecutorService scheduledExecutorService) {
        ScheduledExecutorService scheduledExecutorService2 = this._timerService;
        if (scheduledExecutorService2 == null || scheduledExecutorService2.isShutdown()) {
            this._timerService = scheduledExecutorService;
        }
        this._remainingDurationMs = this._totalDurationMs;
        schedule();
        this._isRunning.getAndSet(true);
    }

    public void kill() {
        stop();
        LifecycleCache lifecycleCache = this._lifecycleCache;
        if (lifecycleCache != null) {
            lifecycleCache.removeListener(this);
        }
        this._timerListener = null;
    }

    public boolean isRunning() {
        return this._isRunning.get();
    }

    public void onStep() {
        if (this._remainingDurationMs.intValue() <= 0) {
            notifyListeners();
            kill();
        }
    }

    private void schedule() {
        try {
            this._task = this._timerService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    BaseTimer baseTimer = BaseTimer.this;
                    baseTimer._remainingDurationMs = Integer.valueOf(baseTimer._remainingDurationMs.intValue() - BaseTimer.this._delayMs.intValue());
                    BaseTimer.this.onStep();
                }
            }, (long) this._delayMs.intValue(), (long) this._delayMs.intValue(), TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException | RejectedExecutionException e) {
            DeviceLog.debug("ERROR: IntervalTimer failed to start due to exception " + e.getLocalizedMessage());
        }
    }

    private void notifyListeners() {
        ITimerListener iTimerListener = this._timerListener;
        if (iTimerListener != null) {
            iTimerListener.onTimerFinished();
        }
    }

    /* renamed from: com.unity3d.services.core.timer.BaseTimer$2 */
    static /* synthetic */ class C03182 {
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$services$core$lifecycle$LifecycleEvent;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.core.lifecycle.LifecycleEvent[] r0 = com.unity3d.services.core.lifecycle.LifecycleEvent.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$unity3d$services$core$lifecycle$LifecycleEvent = r0
                com.unity3d.services.core.lifecycle.LifecycleEvent r1 = com.unity3d.services.core.lifecycle.LifecycleEvent.PAUSED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$unity3d$services$core$lifecycle$LifecycleEvent     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.lifecycle.LifecycleEvent r1 = com.unity3d.services.core.lifecycle.LifecycleEvent.RESUMED     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.timer.BaseTimer.C03182.<clinit>():void");
        }
    }

    public void onAppStateChanged(LifecycleEvent lifecycleEvent) {
        int i = C03182.$SwitchMap$com$unity3d$services$core$lifecycle$LifecycleEvent[lifecycleEvent.ordinal()];
        if (i != 1) {
            if (i == 2 && this._hasPaused.get()) {
                this._hasPaused.getAndSet(false);
                resume();
            }
        } else if (isRunning()) {
            pause();
            this._hasPaused.getAndSet(true);
        }
    }
}
