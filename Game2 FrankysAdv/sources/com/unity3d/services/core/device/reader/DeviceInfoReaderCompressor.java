package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.log.DeviceLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.json.JSONObject;

public class DeviceInfoReaderCompressor implements IDeviceInfoDataCompressor {
    private final IDeviceInfoReader _deviceInfoReader;

    public DeviceInfoReaderCompressor(IDeviceInfoReader iDeviceInfoReader) {
        this._deviceInfoReader = iDeviceInfoReader;
    }

    public byte[] getDeviceData() {
        return compressDeviceInfo(getDeviceInfo());
    }

    public Map<String, Object> getDeviceInfo() {
        return this._deviceInfoReader.getDeviceInfoData();
    }

    public byte[] compressDeviceInfo(Map<String, Object> map) {
        if (map != null) {
            String jSONObject = new JSONObject(map).toString();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(jSONObject.length());
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                gZIPOutputStream.write(jSONObject.getBytes());
                gZIPOutputStream.flush();
                gZIPOutputStream.close();
                byteArrayOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException unused) {
                DeviceLog.error("Error occurred while trying to compress device data.");
                return null;
            }
        } else {
            DeviceLog.error("Invalid DeviceInfoData: Expected non null map provided by reader");
            return null;
        }
    }
}
