package com.unity3d.services.core.request.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Metric {
    private static final String METRIC_NAME = "n";
    private static final String METRIC_TAGS = "t";
    private static final String METRIC_VALUE = "v";
    private final String name;
    private final Map<String, String> tags;
    private final Object value;

    public Metric(String str, Object obj, Map<String, String> map) {
        this.name = str;
        this.value = obj;
        this.tags = map;
    }

    /* access modifiers changed from: package-private */
    public Map<String, Object> asMap() {
        HashMap hashMap = new HashMap();
        String str = this.name;
        if (str != null) {
            hashMap.put(METRIC_NAME, str);
        }
        Object obj = this.value;
        if (obj != null) {
            hashMap.put(METRIC_VALUE, obj);
        }
        Map<String, String> map = this.tags;
        if (map != null) {
            hashMap.put(METRIC_TAGS, map);
        }
        return hashMap;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public String toString() {
        return "Metric{name='" + this.name + '\'' + ", value='" + this.value + '\'' + ", tags=" + this.tags + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Metric)) {
            return false;
        }
        Metric metric = (Metric) obj;
        if (!Objects.equals(this.name, metric.name) || !Objects.equals(this.value, metric.value) || !Objects.equals(this.tags, metric.tags)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.value, this.tags});
    }
}
