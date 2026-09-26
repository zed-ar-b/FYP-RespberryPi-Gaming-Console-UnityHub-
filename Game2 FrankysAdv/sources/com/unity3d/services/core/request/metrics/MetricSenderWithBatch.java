package com.unity3d.services.core.request.metrics;

import android.text.TextUtils;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.InitializationStatusReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class MetricSenderWithBatch extends IMetricSenderWithBatch {
    private ISDKMetrics _original;
    private final LinkedBlockingQueue<Metric> _queue = new LinkedBlockingQueue<>();

    public MetricSenderWithBatch(ISDKMetrics iSDKMetrics, InitializationStatusReader initializationStatusReader) {
        super(initializationStatusReader);
        this._original = iSDKMetrics;
    }

    public void updateOriginal(ISDKMetrics iSDKMetrics) {
        this._original = iSDKMetrics;
    }

    public boolean areMetricsEnabledForCurrentSession() {
        return this._original.areMetricsEnabledForCurrentSession();
    }

    public void sendEvent(String str) {
        sendEvent(str, (Map<String, String>) null);
    }

    public void sendEvent(String str, String str2, Map<String, String> map) {
        if (str == null || str.isEmpty()) {
            DeviceLog.debug("Metric event not sent due to being null or empty: " + str);
            return;
        }
        sendMetrics(new ArrayList(Collections.singletonList(new Metric(str, str2, map))));
    }

    public void sendEvent(String str, Map<String, String> map) {
        sendEvent(str, (String) null, map);
    }

    public void sendMetric(Metric metric) {
        sendMetrics(new ArrayList(Collections.singletonList(metric)));
    }

    public synchronized void sendMetrics(List<Metric> list) {
        this._queue.addAll(list);
        if (!TextUtils.isEmpty(this._original.getMetricEndPoint()) && this._queue.size() > 0) {
            ArrayList arrayList = new ArrayList();
            this._queue.drainTo(arrayList);
            this._original.sendMetrics(arrayList);
        }
    }

    public String getMetricEndPoint() {
        ISDKMetrics iSDKMetrics = this._original;
        if (iSDKMetrics == null) {
            return null;
        }
        return iSDKMetrics.getMetricEndPoint();
    }

    public void sendQueueIfNeeded() {
        sendMetrics(new ArrayList());
    }
}
