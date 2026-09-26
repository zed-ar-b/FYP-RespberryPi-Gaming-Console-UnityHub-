package com.unity3d.services.core.webview.bridge;

import android.os.Parcel;
import android.os.Parcelable;
import com.unity3d.services.core.log.DeviceLog;
import java.util.ArrayList;
import java.util.Arrays;

public class WebViewCallback implements Parcelable {
    public static final Parcelable.Creator<WebViewCallback> CREATOR = new Parcelable.Creator<WebViewCallback>() {
        public WebViewCallback createFromParcel(Parcel parcel) {
            return new WebViewCallback(parcel);
        }

        public WebViewCallback[] newArray(int i) {
            return new WebViewCallback[i];
        }
    };
    private String _callbackId;
    private int _invocationId;
    private boolean _invoked;

    public int describeContents() {
        return 45678;
    }

    public WebViewCallback(String str, int i) {
        this._callbackId = str;
        this._invocationId = i;
    }

    public WebViewCallback(Parcel parcel) {
        this._callbackId = parcel.readString();
        this._invoked = parcel.readByte() != 0;
        this._invocationId = parcel.readInt();
    }

    public void invoke(Object... objArr) {
        invoke(CallbackStatus.OK, (Enum) null, objArr);
    }

    private void invoke(CallbackStatus callbackStatus, Enum enumR, Object... objArr) {
        String str;
        if (!this._invoked && (str = this._callbackId) != null && str.length() != 0) {
            this._invoked = true;
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(objArr));
            arrayList.add(0, this._callbackId);
            Invocation invocationById = Invocation.getInvocationById(this._invocationId);
            if (invocationById == null) {
                DeviceLog.error("Couldn't get batch with id: " + getInvocationId());
                return;
            }
            invocationById.setInvocationResponse(callbackStatus, enumR, arrayList.toArray());
        }
    }

    public void error(Enum enumR, Object... objArr) {
        invoke(CallbackStatus.ERROR, enumR, objArr);
    }

    public int getInvocationId() {
        return this._invocationId;
    }

    public String getCallbackId() {
        return this._callbackId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this._callbackId);
        parcel.writeByte(this._invoked ? (byte) 1 : 0);
        parcel.writeInt(this._invocationId);
    }
}
