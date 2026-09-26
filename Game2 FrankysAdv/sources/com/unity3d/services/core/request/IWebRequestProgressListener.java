package com.unity3d.services.core.request;

import java.util.List;
import java.util.Map;

public interface IWebRequestProgressListener {
    void onRequestProgress(String str, long j, long j2);

    void onRequestStart(String str, long j, int i, Map<String, List<String>> map);
}
