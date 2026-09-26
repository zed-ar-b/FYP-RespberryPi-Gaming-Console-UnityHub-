package com.unity3d.services.core.request.metrics;

import android.text.TextUtils;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.InitializationStatusReader;
import com.unity3d.services.core.request.WebRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

public class MetricSender extends MetricSenderBase {
    /* access modifiers changed from: private */
    public final MetricCommonTags _commonTags;
    private final ExecutorService _executorService = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public final String _metricEndpoint;
    /* access modifiers changed from: private */
    public final String _metricSampleRate;

    public boolean areMetricsEnabledForCurrentSession() {
        return true;
    }

    public /* bridge */ /* synthetic */ void sendMetricWithInitState(Metric metric) {
        super.sendMetricWithInitState(metric);
    }

    public MetricSender(Configuration configuration, InitializationStatusReader initializationStatusReader) {
        super(initializationStatusReader);
        this._metricEndpoint = configuration.getMetricsUrl();
        this._metricSampleRate = String.valueOf((int) Math.round(configuration.getMetricSampleRate()));
        MetricCommonTags metricCommonTags = new MetricCommonTags();
        this._commonTags = metricCommonTags;
        metricCommonTags.updateWithConfig(configuration);
    }

    public void sendEvent(String str) {
        sendEvent(str, (Map<String, String>) null);
    }

    public void sendEvent(String str, Map<String, String> map) {
        if (str == null || str.isEmpty()) {
            DeviceLog.debug("Metric event not sent due to being null or empty: " + str);
            return;
        }
        sendEvent(str, (String) null, map);
    }

    public void sendEvent(String str, String str2, Map<String, String> map) {
        sendMetrics(new ArrayList(Collections.singletonList(new Metric(str, str2, map))));
    }

    public void sendMetric(Metric metric) {
        sendMetrics(new ArrayList(Collections.singletonList(metric)));
    }

    public void sendMetrics(final List<Metric> list) {
        if (list == null || list.size() <= 0) {
            DeviceLog.debug("Metrics event not send due to being null or empty");
        } else if (TextUtils.isEmpty(this._metricEndpoint)) {
            DeviceLog.debug("Metrics: " + list + " was not sent to null or empty endpoint: " + this._metricEndpoint);
        } else if (this._executorService.isShutdown()) {
            DeviceLog.debug("Metrics " + list + " was not sent due to misconfiguration");
        } else {
            this._executorService.submit(new Runnable() {
                public void run() {
                    try {
                        String jSONObject = new JSONObject(new MetricsContainer(MetricSender.this._metricSampleRate, MetricSender.this._commonTags, list).asMap()).toString();
                        WebRequest webRequest = new WebRequest(MetricSender.this._metricEndpoint, "POST", (Map<String, List<String>>) null);
                        webRequest.setBody(jSONObject);
                        webRequest.makeRequest();
                        if (webRequest.getResponseCode() / 100 == 2) {
                            DeviceLog.debug("Metric " + list + " sent to " + MetricSender.this._metricEndpoint);
                            return;
                        }
                        DeviceLog.debug("Metric " + list + " failed to send with response code: " + webRequest.getResponseCode());
                    } catch (Exception e) {
                        DeviceLog.debug("Metric " + list + " failed to send from exception: " + e.getMessage());
                    }
                }
            });
        }
    }

    public String getMetricEndPoint() {
        return this._metricEndpoint;
    }

    /* access modifiers changed from: package-private */
    public void shutdown() {
        this._commonTags.shutdown();
        this._executorService.shutdown();
    }
}
