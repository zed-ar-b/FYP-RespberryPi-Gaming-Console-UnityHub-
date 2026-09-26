package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkConnectivity extends Activity {

    /* renamed from: a */
    private final int f28a = 0;

    /* renamed from: b */
    private final int f29b;

    /* renamed from: c */
    private final int f30c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public int f31d;

    /* renamed from: e */
    private ConnectivityManager f32e;

    /* renamed from: f */
    private final ConnectivityManager.NetworkCallback f33f;

    public NetworkConnectivity(Context context) {
        int i = 1;
        this.f29b = 1;
        this.f30c = 2;
        this.f31d = 0;
        this.f33f = new ConnectivityManager.NetworkCallback() {
            public final void onAvailable(Network network) {
                super.onAvailable(network);
            }

            public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                NetworkConnectivity networkConnectivity;
                int i;
                super.onCapabilitiesChanged(network, networkCapabilities);
                if (networkCapabilities.hasTransport(0)) {
                    networkConnectivity = NetworkConnectivity.this;
                    i = 1;
                } else {
                    networkConnectivity = NetworkConnectivity.this;
                    i = 2;
                }
                int unused = networkConnectivity.f31d = i;
            }

            public final void onLost(Network network) {
                super.onLost(network);
                int unused = NetworkConnectivity.this.f31d = 0;
            }

            public final void onUnavailable() {
                super.onUnavailable();
                int unused = NetworkConnectivity.this.f31d = 0;
            }
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.f32e = connectivityManager;
        connectivityManager.registerDefaultNetworkCallback(this.f33f);
        NetworkInfo activeNetworkInfo = this.f32e.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            this.f31d = activeNetworkInfo.getType() != 0 ? 2 : i;
        }
    }

    /* renamed from: a */
    public final int mo89a() {
        return this.f31d;
    }

    /* renamed from: b */
    public final void mo90b() {
        this.f32e.unregisterNetworkCallback(this.f33f);
    }
}
