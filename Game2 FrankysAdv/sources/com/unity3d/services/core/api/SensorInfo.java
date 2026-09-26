package com.unity3d.services.core.api;

import com.unity3d.services.core.sensorinfo.SensorInfoError;
import com.unity3d.services.core.sensorinfo.SensorInfoListener;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import org.json.JSONObject;

public class SensorInfo {
    @WebViewExposed
    public static void startAccelerometerUpdates(Integer num, WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(SensorInfoListener.startAccelerometerListener(num.intValue())));
    }

    @WebViewExposed
    public static void stopAccelerometerUpdates(WebViewCallback webViewCallback) {
        SensorInfoListener.stopAccelerometerListener();
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void isAccelerometerActive(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(SensorInfoListener.isAccelerometerListenerActive()));
    }

    @WebViewExposed
    public static void getAccelerometerData(WebViewCallback webViewCallback) {
        JSONObject accelerometerData = SensorInfoListener.getAccelerometerData();
        if (accelerometerData != null) {
            webViewCallback.invoke(accelerometerData);
            return;
        }
        webViewCallback.error(SensorInfoError.ACCELEROMETER_DATA_NOT_AVAILABLE, new Object[0]);
    }
}
