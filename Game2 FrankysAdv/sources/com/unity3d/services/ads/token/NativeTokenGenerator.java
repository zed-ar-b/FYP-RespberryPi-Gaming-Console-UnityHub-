package com.unity3d.services.ads.token;

import android.util.Base64;
import com.unity3d.services.core.device.reader.DeviceInfoReaderBuilder;
import com.unity3d.services.core.device.reader.DeviceInfoReaderCompressor;
import com.unity3d.services.core.log.DeviceLog;
import java.util.concurrent.ExecutorService;

public class NativeTokenGenerator implements INativeTokenGenerator {
    private static final String DEFAULT_NATIVE_TOKEN_PREFIX = "1:";
    /* access modifiers changed from: private */
    public DeviceInfoReaderBuilder _deviceInfoReaderBuilder;
    private ExecutorService _executorService;
    /* access modifiers changed from: private */
    public String _prependStr;

    public NativeTokenGenerator(ExecutorService executorService, DeviceInfoReaderBuilder deviceInfoReaderBuilder) {
        this(executorService, deviceInfoReaderBuilder, DEFAULT_NATIVE_TOKEN_PREFIX);
    }

    public NativeTokenGenerator(ExecutorService executorService, DeviceInfoReaderBuilder deviceInfoReaderBuilder, String str) {
        this._executorService = executorService;
        this._deviceInfoReaderBuilder = deviceInfoReaderBuilder;
        this._prependStr = str;
    }

    public void generateToken(final INativeTokenGeneratorListener iNativeTokenGeneratorListener) {
        this._executorService.execute(new Runnable() {
            public void run() {
                try {
                    String encodeToString = Base64.encodeToString(new DeviceInfoReaderCompressor(NativeTokenGenerator.this._deviceInfoReaderBuilder.build()).getDeviceData(), 2);
                    if (NativeTokenGenerator.this._prependStr == null || NativeTokenGenerator.this._prependStr.isEmpty()) {
                        iNativeTokenGeneratorListener.onReady(encodeToString);
                        return;
                    }
                    StringBuilder sb = new StringBuilder(NativeTokenGenerator.this._prependStr.length() + encodeToString.length());
                    sb.append(NativeTokenGenerator.this._prependStr);
                    sb.append(encodeToString);
                    iNativeTokenGeneratorListener.onReady(sb.toString());
                } catch (Exception e) {
                    DeviceLog.exception("Unity Ads failed to generate token.", e);
                    iNativeTokenGeneratorListener.onReady((String) null);
                }
            }
        });
    }
}
