package com.unity3d.services.ads.operation;

import android.os.ConditionVariable;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.timer.BaseTimer;
import com.unity3d.services.core.webview.bridge.IWebViewSharedObject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OperationState implements IWebViewSharedObject {
    private static String _emptyPlacementId = "";
    public Configuration configuration;

    /* renamed from: id */
    public String f339id;
    public String placementId;
    public long startTime;
    public ConditionVariable timeoutCV;
    public BaseTimer timeoutTimer;

    public OperationState(String str, Configuration configuration2) {
        this.placementId = str == null ? _emptyPlacementId : str;
        this.configuration = configuration2;
        this.timeoutCV = new ConditionVariable();
        this.f339id = UUID.randomUUID().toString();
    }

    public String getId() {
        return this.f339id;
    }

    public void start() {
        this.startTime = System.nanoTime();
    }

    public long duration() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.startTime);
    }
}
