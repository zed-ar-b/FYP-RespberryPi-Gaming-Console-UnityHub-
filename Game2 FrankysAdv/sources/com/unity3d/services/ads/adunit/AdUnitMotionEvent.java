package com.unity3d.services.ads.adunit;

public class AdUnitMotionEvent {
    private int _action;
    private int _deviceId;
    private long _eventTime;
    private boolean _isObscured;
    private float _pressure;
    private float _size;
    private int _source;
    private int _toolType;

    /* renamed from: _x */
    private float f337_x;

    /* renamed from: _y */
    private float f338_y;

    public AdUnitMotionEvent(int i, boolean z, int i2, int i3, int i4, float f, float f2, long j, float f3, float f4) {
        this._action = i;
        this._isObscured = z;
        this._toolType = i2;
        this._source = i3;
        this._deviceId = i4;
        this.f337_x = f;
        this.f338_y = f2;
        this._eventTime = j;
        this._pressure = f3;
        this._size = f4;
    }

    public int getAction() {
        return this._action;
    }

    public boolean isObscured() {
        return this._isObscured;
    }

    public int getToolType() {
        return this._toolType;
    }

    public int getSource() {
        return this._source;
    }

    public int getDeviceId() {
        return this._deviceId;
    }

    public float getX() {
        return this.f337_x;
    }

    public float getY() {
        return this.f338_y;
    }

    public long getEventTime() {
        return this._eventTime;
    }

    public float getPressure() {
        return this._pressure;
    }

    public float getSize() {
        return this._size;
    }
}
