package com.unity3d.services.core.sensorinfo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import org.json.JSONException;
import org.json.JSONObject;

public class SensorInfoListener implements SensorEventListener {
    private static SensorInfoListener _accelerometerListener;
    private static Sensor _accelerometerSensor;
    private static SensorEvent _latestAccelerometerEvent;

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public static boolean startAccelerometerListener(int i) {
        if (_accelerometerListener == null) {
            _accelerometerListener = new SensorInfoListener();
        }
        SensorManager sensorManager = (SensorManager) ClientProperties.getApplicationContext().getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(1);
        _accelerometerSensor = defaultSensor;
        return sensorManager.registerListener(_accelerometerListener, defaultSensor, i);
    }

    public static void stopAccelerometerListener() {
        if (_accelerometerListener != null) {
            ((SensorManager) ClientProperties.getApplicationContext().getSystemService("sensor")).unregisterListener(_accelerometerListener);
            _accelerometerListener = null;
        }
    }

    public static boolean isAccelerometerListenerActive() {
        return _accelerometerListener != null;
    }

    public static JSONObject getAccelerometerData() {
        if (_latestAccelerometerEvent == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("x", (double) _latestAccelerometerEvent.values[0]);
            jSONObject.put("y", (double) _latestAccelerometerEvent.values[1]);
            jSONObject.put("z", (double) _latestAccelerometerEvent.values[2]);
            return jSONObject;
        } catch (JSONException e) {
            DeviceLog.exception("JSON error while constructing accelerometer data", e);
            return jSONObject;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1) {
            _latestAccelerometerEvent = sensorEvent;
        }
    }
}
