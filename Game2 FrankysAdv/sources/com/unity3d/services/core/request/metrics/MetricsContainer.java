package com.unity3d.services.core.request.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsContainer {
    private static final String METRICS_CONTAINER = "m";
    private static final String METRICS_CONTAINER_TAGS = "t";
    private static final String METRIC_CONTAINER_SAMPLE_RATE = "msr";
    private final MetricCommonTags _commonTags;
    private final String _metricSampleRate;
    private final List<Metric> _metrics;

    public MetricsContainer(String str, MetricCommonTags metricCommonTags, List<Metric> list) {
        this._metricSampleRate = str;
        this._commonTags = metricCommonTags;
        this._metrics = list;
    }

    public Map<String, Object> asMap() {
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (Metric asMap : this._metrics) {
            arrayList.add(asMap.asMap());
        }
        hashMap.put(METRIC_CONTAINER_SAMPLE_RATE, this._metricSampleRate);
        hashMap.put(METRICS_CONTAINER, arrayList);
        hashMap.put(METRICS_CONTAINER_TAGS, this._commonTags.asMap());
        return hashMap;
    }
}
