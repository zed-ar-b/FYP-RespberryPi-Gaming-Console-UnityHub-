package com.unity.androidnotifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;

public class UnityNotificationRestartOnBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            rescheduleSavedNotifications(context);
        }
    }

    private static void rescheduleSavedNotifications(Context context) {
        UnityNotificationManager notificationManagerImpl = UnityNotificationManager.getNotificationManagerImpl(context);
        for (Notification.Builder next : notificationManagerImpl.loadSavedNotifications()) {
            Bundle extras = next.getExtras();
            long j = extras.getLong("repeatInterval", 0);
            long j2 = extras.getLong("fireTime", 0);
            Date time = Calendar.getInstance().getTime();
            Date date = new Date(j2);
            boolean z = j > 0;
            if (date.after(time) || z) {
                notificationManagerImpl.scheduleAlarmWithNotification(next);
            }
        }
    }
}
