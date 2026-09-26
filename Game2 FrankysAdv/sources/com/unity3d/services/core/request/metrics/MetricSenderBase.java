package com.unity3d.services.core.request.metrics;

import com.unity3d.services.core.properties.InitializationStatusReader;
import com.unity3d.services.core.properties.SdkProperties;
import java.util.HashMap;

abstract class MetricSenderBase implements ISDKMetrics {
    private final InitializationStatusReader _initStatusReader;

    public MetricSenderBase(InitializationStatusReader initializationStatusReader) {
        this._initStatusReader = initializationStatusReader;
    }

    public void sendMetricWithInitState(Metric metric) {
        if (metric != null) {
            if (metric.getTags() == null) {
                metric = new Metric(metric.getName(), metric.getValue(), new HashMap());
            }
            metric.getTags().put(AdOperationMetric.INIT_STATE, this._initStatusReader.getInitializationStateString(SdkProperties.getCurrentInitializationState()));
            sendMetric(metric);
        }
    }
}
