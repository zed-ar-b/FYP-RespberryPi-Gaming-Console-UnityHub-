package com.unity3d.services.core.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.unity3d.services.core.log.DeviceLog;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AdvertisingId {
    private static final String ADVERTISING_ID_SERVICE_NAME = "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService";
    private static AdvertisingId instance;
    private String advertisingIdentifier = null;
    private boolean limitedAdvertisingTracking = false;

    private static AdvertisingId getInstance() {
        if (instance == null) {
            instance = new AdvertisingId();
        }
        return instance;
    }

    public static void init(Context context) {
        getInstance().fetchAdvertisingId(context);
    }

    public static String getAdvertisingTrackingId() {
        return getInstance().advertisingIdentifier;
    }

    public static boolean getLimitedAdTracking() {
        return getInstance().limitedAdvertisingTracking;
    }

    private void fetchAdvertisingId(Context context) {
        boolean z;
        GoogleAdvertisingServiceConnection googleAdvertisingServiceConnection = new GoogleAdvertisingServiceConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        try {
            z = context.bindService(intent, googleAdvertisingServiceConnection, 1);
        } catch (Exception e) {
            DeviceLog.exception("Couldn't bind to identifier service intent", e);
            z = false;
        }
        if (z) {
            try {
                GoogleAdvertisingInfo create = GoogleAdvertisingInfo.GoogleAdvertisingInfoBinder.create(googleAdvertisingServiceConnection.getBinder());
                this.advertisingIdentifier = create.getId();
                this.limitedAdvertisingTracking = create.getEnabled(true);
            } catch (Exception e2) {
                DeviceLog.exception("Couldn't get advertising info", e2);
                if (!z) {
                    return;
                }
            } catch (Throwable th) {
                if (z) {
                    context.unbindService(googleAdvertisingServiceConnection);
                }
                throw th;
            }
        }
        if (!z) {
            return;
        }
        context.unbindService(googleAdvertisingServiceConnection);
    }

    private interface GoogleAdvertisingInfo extends IInterface {
        boolean getEnabled(boolean z) throws RemoteException;

        String getId() throws RemoteException;

        public static abstract class GoogleAdvertisingInfoBinder extends Binder implements GoogleAdvertisingInfo {
            public static GoogleAdvertisingInfo create(IBinder iBinder) {
                if (iBinder == null) {
                    return null;
                }
                IInterface queryLocalInterface = iBinder.queryLocalInterface(AdvertisingId.ADVERTISING_ID_SERVICE_NAME);
                if (queryLocalInterface == null || !(queryLocalInterface instanceof GoogleAdvertisingInfo)) {
                    return new GoogleAdvertisingInfoImplementation(iBinder);
                }
                return (GoogleAdvertisingInfo) queryLocalInterface;
            }

            public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
                if (i == 1) {
                    parcel.enforceInterface(AdvertisingId.ADVERTISING_ID_SERVICE_NAME);
                    String id = getId();
                    parcel2.writeNoException();
                    parcel2.writeString(id);
                    return true;
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceInterface(AdvertisingId.ADVERTISING_ID_SERVICE_NAME);
                    boolean enabled = getEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(enabled ? 1 : 0);
                    return true;
                }
            }

            private static class GoogleAdvertisingInfoImplementation implements GoogleAdvertisingInfo {
                private final IBinder _binder;

                GoogleAdvertisingInfoImplementation(IBinder iBinder) {
                    this._binder = iBinder;
                }

                public IBinder asBinder() {
                    return this._binder;
                }

                public String getId() throws RemoteException {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(AdvertisingId.ADVERTISING_ID_SERVICE_NAME);
                        this._binder.transact(1, obtain, obtain2, 0);
                        obtain2.readException();
                        return obtain2.readString();
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }

                public boolean getEnabled(boolean z) throws RemoteException {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(AdvertisingId.ADVERTISING_ID_SERVICE_NAME);
                        boolean z2 = true;
                        obtain.writeInt(z ? 1 : 0);
                        this._binder.transact(2, obtain, obtain2, 0);
                        obtain2.readException();
                        if (obtain2.readInt() == 0) {
                            z2 = false;
                        }
                        return z2;
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
            }
        }
    }

    private class GoogleAdvertisingServiceConnection implements ServiceConnection {
        private final BlockingQueue<IBinder> _binderQueue;
        boolean _consumed;

        public void onServiceDisconnected(ComponentName componentName) {
        }

        private GoogleAdvertisingServiceConnection() {
            this._consumed = false;
            this._binderQueue = new LinkedBlockingQueue();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this._binderQueue.put(iBinder);
            } catch (InterruptedException unused) {
                DeviceLog.debug("Couldn't put service to binder que");
            }
        }

        public IBinder getBinder() throws InterruptedException {
            if (!this._consumed) {
                this._consumed = true;
                return this._binderQueue.take();
            }
            throw new IllegalStateException();
        }
    }
}
