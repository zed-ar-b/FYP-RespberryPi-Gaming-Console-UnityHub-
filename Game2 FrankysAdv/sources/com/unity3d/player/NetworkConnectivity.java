package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkConnectivity extends Activity {

    /* renamed from: a */
    private final int f27a = 0;

    /* renamed from: b */
    private final int f28b;

    /* renamed from: c */
    private final int f29c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public int f30d;

    /* renamed from: e */
    private ConnectivityManager f31e;

    /* renamed from: f */
    private final ConnectivityManager.NetworkCallback f32f;

    public NetworkConnectivity(Context context) {
        int i = 1;
        this.f28b = 1;
        this.f29c = 2;
        this.f30d = 0;
        this.f32f = new ConnectivityManager.NetworkCallback() {
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
                int unused = networkConnectivity.f30d = i;
            }

            public final void onLost(Network network) {
                super.onLost(network);
                int unused = NetworkConnectivity.this.f30d = 0;
            }

            public final void onUnavailable() {
                super.onUnavailable();
                int unused = NetworkConnectivity.this.f30d = 0;
            }
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.f31e = connectivityManager;
        connectivityManager.registerDefaultNetworkCallback(this.f32f);
        NetworkInfo activeNetworkInfo = this.f31e.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            this.f30d = activeNetworkInfo.getType() != 0 ? 2 : i;
        }
    }

    /* renamed from: a */
    public final int mo215a() {
        return this.f30d;
    }

    /* renamed from: b */
    public final void mo216b() {
        this.f31e.unregisterNetworkCallback(this.f32f);
    }
}
