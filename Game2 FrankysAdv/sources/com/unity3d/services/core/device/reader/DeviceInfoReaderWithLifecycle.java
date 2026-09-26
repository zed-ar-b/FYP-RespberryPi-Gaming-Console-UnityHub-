package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.lifecycle.LifecycleCache;
import java.util.Map;

public class DeviceInfoReaderWithLifecycle implements IDeviceInfoReader {
    private final IDeviceInfoReader _deviceInfoReader;
    private final LifecycleCache _lifecycleCache;

    public DeviceInfoReaderWithLifecycle(IDeviceInfoReader iDeviceInfoReader, LifecycleCache lifecycleCache) {
        this._deviceInfoReader = iDeviceInfoReader;
        this._lifecycleCache = lifecycleCache;
    }

    public Map<String, Object> getDeviceInfoData() {
        Map<String, Object> deviceInfoData = this._deviceInfoReader.getDeviceInfoData();
        deviceInfoData.put("appActive", Boolean.valueOf(this._lifecycleCache.isAppActive()));
        return deviceInfoData;
    }
}
