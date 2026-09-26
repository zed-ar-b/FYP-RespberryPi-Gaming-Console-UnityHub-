package com.unity3d.services.core.timer;

public interface IIntervalTimerFactory {
    IIntervalTimer createTimer(Integer num, Integer num2, IIntervalTimerListener iIntervalTimerListener);
}
