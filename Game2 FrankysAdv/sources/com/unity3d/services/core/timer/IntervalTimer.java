package com.unity3d.services.core.timer;

import com.unity3d.services.core.lifecycle.LifecycleCache;
import java.util.concurrent.atomic.AtomicInteger;

public class IntervalTimer extends BaseTimer implements IIntervalTimer {
    private final AtomicInteger _currentPosition = new AtomicInteger(0);
    private final Integer _intervalDuration;
    private Integer _nextInterval;
    private IIntervalTimerListener _timerListener;
    private final Integer _totalIntervals;

    public IntervalTimer(Integer num, Integer num2, IIntervalTimerListener iIntervalTimerListener, LifecycleCache lifecycleCache) {
        super(num, (ITimerListener) null, lifecycleCache);
        this._totalIntervals = num2;
        this._timerListener = iIntervalTimerListener;
        int intValue = num2.intValue();
        int intValue2 = num.intValue();
        Integer valueOf = Integer.valueOf(intValue != 0 ? intValue2 / this._totalIntervals.intValue() : intValue2);
        this._intervalDuration = valueOf;
        this._nextInterval = valueOf;
    }

    public void onStep() {
        if (this._currentPosition.addAndGet(this._delayMs.intValue()) >= this._nextInterval.intValue()) {
            onNextInterval();
        }
        super.onStep();
    }

    public void onNextInterval() {
        IIntervalTimerListener iIntervalTimerListener = this._timerListener;
        if (iIntervalTimerListener != null) {
            iIntervalTimerListener.onNextIntervalTriggered();
        }
        this._nextInterval = Integer.valueOf(this._nextInterval.intValue() + this._intervalDuration.intValue());
    }

    public void kill() {
        super.kill();
        this._timerListener = null;
    }
}
