package com.unity3d.services.core.connectivity;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import com.unity3d.services.core.properties.ClientProperties;

public class ConnectivityNetworkCallback extends ConnectivityManager.NetworkCallback {
    private static ConnectivityNetworkCallback _impl;

    public static synchronized void register() {
        synchronized (ConnectivityNetworkCallback.class) {
            if (_impl == null) {
                _impl = new ConnectivityNetworkCallback();
                ((ConnectivityManager) ClientProperties.getApplicationContext().getSystemService("connectivity")).registerNetworkCallback(new NetworkRequest.Builder().build(), _impl);
            }
        }
    }

    public static synchronized void unregister() {
        synchronized (ConnectivityNetworkCallback.class) {
            if (_impl != null) {
                ((ConnectivityManager) ClientProperties.getApplicationContext().getSystemService("connectivity")).unregisterNetworkCallback(_impl);
                _impl = null;
            }
        }
    }

    public void onAvailable(Network network) {
        ConnectivityMonitor.connected();
    }

    public void onLost(Network network) {
        ConnectivityMonitor.disconnected();
    }

    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        ConnectivityMonitor.connectionStatusChanged();
    }

    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        ConnectivityMonitor.connectionStatusChanged();
    }
}
