package com.unity3d.services.core.broadcast;

import android.content.IntentFilter;
import com.unity3d.services.core.properties.ClientProperties;
import java.util.HashMap;
import java.util.Map;

public class BroadcastMonitor {
    private static Map<String, BroadcastEventReceiver> _eventReceivers;

    public static void addBroadcastListener(String str, String str2, String[] strArr) {
        removeBroadcastListener(str);
        IntentFilter intentFilter = new IntentFilter();
        for (String addAction : strArr) {
            intentFilter.addAction(addAction);
        }
        if (str2 != null) {
            intentFilter.addDataScheme(str2);
        }
        if (_eventReceivers == null) {
            _eventReceivers = new HashMap();
        }
        BroadcastEventReceiver broadcastEventReceiver = new BroadcastEventReceiver(str);
        _eventReceivers.put(str, broadcastEventReceiver);
        ClientProperties.getApplicationContext().registerReceiver(broadcastEventReceiver, intentFilter);
    }

    public static void removeBroadcastListener(String str) {
        Map<String, BroadcastEventReceiver> map = _eventReceivers;
        if (map != null && map.containsKey(str)) {
            ClientProperties.getApplicationContext().unregisterReceiver(_eventReceivers.get(str));
            _eventReceivers.remove(str);
        }
    }

    public static void removeAllBroadcastListeners() {
        Map<String, BroadcastEventReceiver> map = _eventReceivers;
        if (map != null) {
            for (String str : map.keySet()) {
                ClientProperties.getApplicationContext().unregisterReceiver(_eventReceivers.get(str));
            }
            _eventReceivers = null;
        }
    }
}
