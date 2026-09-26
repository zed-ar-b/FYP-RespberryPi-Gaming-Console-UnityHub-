package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.request.metrics.MetricCommonTags;
import java.util.HashMap;
import java.util.Map;

public class MinimalDeviceInfoReader implements IDeviceInfoReader {
    final IGameSessionIdReader _gameSessionIdReader;

    public MinimalDeviceInfoReader(IGameSessionIdReader iGameSessionIdReader) {
        this._gameSessionIdReader = iGameSessionIdReader;
    }

    public Map<String, Object> getDeviceInfoData() {
        HashMap hashMap = new HashMap();
        hashMap.put("platform", MetricCommonTags.METRIC_COMMON_TAG_PLATFORM_ANDROID);
        hashMap.put("sdkVersion", Integer.valueOf(SdkProperties.getVersionCode()));
        hashMap.put("sdkVersionName", SdkProperties.getVersionName());
        hashMap.put("idfi", Device.getIdfi());
        hashMap.put(JsonStorageKeyNames.GAME_SESSION_ID_NORMALIZED_KEY, this._gameSessionIdReader.getGameSessionIdAndStore());
        hashMap.put("ts", Long.valueOf(System.currentTimeMillis()));
        hashMap.put("gameId", ClientProperties.getGameId());
        return hashMap;
    }
}
