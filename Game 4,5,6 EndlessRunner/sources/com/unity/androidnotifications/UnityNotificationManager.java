package com.unity.androidnotifications;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UnityNotificationManager extends BroadcastReceiver {
    static final String KEY_CHANNEL_ID = "channelID";
    static final String KEY_FIRE_TIME = "fireTime";
    static final String KEY_ID = "id";
    static final String KEY_INTENT_DATA = "data";
    static final String KEY_LARGE_ICON = "largeIcon";
    static final String KEY_NOTIFICATION = "unityNotification";
    static final String KEY_NOTIFICATION_DISMISSED = "com.unity.NotificationDismissed";
    static final String KEY_NOTIFICATION_ID = "com.unity.NotificationID";
    static final String KEY_REPEAT_INTERVAL = "repeatInterval";
    static final String KEY_SHOW_IN_FOREGROUND = "com.unity.showInForeground";
    static final String KEY_SMALL_ICON = "smallIcon";
    static final String NOTIFICATION_CHANNELS_SHARED_PREFS = "UNITY_NOTIFICATIONS";
    static final String NOTIFICATION_CHANNELS_SHARED_PREFS_KEY = "ChannelIDs";
    static final String NOTIFICATION_IDS_SHARED_PREFS = "UNITY_STORED_NOTIFICATION_IDS";
    static final String NOTIFICATION_IDS_SHARED_PREFS_KEY = "UNITY_NOTIFICATION_IDS";
    static final String TAG_UNITY = "UnityNotifications";
    static UnityNotificationManager mUnityNotificationManager;
    private Activity mActivity = null;
    private UnityNotificationBackgroundThread mBackgroundThread;
    private Method mCanScheduleExactAlarms;
    private Context mContext = null;
    private int mExactSchedulingSetting = -1;
    private NotificationCallback mNotificationCallback;
    private Class mOpenActivity = null;
    private Random mRandom;
    private ConcurrentHashMap<Integer, Notification.Builder> mScheduledNotifications;
    private HashSet<Integer> mVisibleNotifications;

    private void initialize(Activity activity, NotificationCallback notificationCallback) {
        this.mContext = activity.getApplicationContext();
        this.mActivity = activity;
        this.mNotificationCallback = notificationCallback;
        if (this.mScheduledNotifications == null) {
            this.mScheduledNotifications = new ConcurrentHashMap<>();
        }
        UnityNotificationBackgroundThread unityNotificationBackgroundThread = this.mBackgroundThread;
        if (unityNotificationBackgroundThread == null || !unityNotificationBackgroundThread.isAlive()) {
            this.mBackgroundThread = new UnityNotificationBackgroundThread(this, this.mScheduledNotifications);
        }
        if (this.mRandom == null) {
            this.mRandom = new Random();
        }
        if (this.mVisibleNotifications == null) {
            this.mVisibleNotifications = new HashSet<>();
        }
        Bundle appMetadata = getAppMetadata();
        Boolean bool = false;
        if (appMetadata != null) {
            bool = Boolean.valueOf(appMetadata.getBoolean("reschedule_notifications_on_restart", false));
        }
        if (bool.booleanValue()) {
            this.mContext.getPackageManager().setComponentEnabledSetting(new ComponentName(this.mContext, UnityNotificationRestartOnBootReceiver.class), 1, 1);
        }
        Class<?> openAppActivity = UnityNotificationUtilities.getOpenAppActivity(this.mContext, false);
        this.mOpenActivity = openAppActivity;
        if (openAppActivity == null) {
            this.mOpenActivity = activity.getClass();
        }
        if (!this.mBackgroundThread.isAlive()) {
            this.mBackgroundThread.start();
        }
    }

    static synchronized UnityNotificationManager getNotificationManagerImpl(Context context) {
        UnityNotificationManager unityNotificationManager;
        synchronized (UnityNotificationManager.class) {
            if (mUnityNotificationManager == null) {
                UnityNotificationManager unityNotificationManager2 = new UnityNotificationManager();
                mUnityNotificationManager = unityNotificationManager2;
                unityNotificationManager2.mVisibleNotifications = new HashSet<>();
                mUnityNotificationManager.mScheduledNotifications = new ConcurrentHashMap<>();
            }
            mUnityNotificationManager.mContext = context.getApplicationContext();
            unityNotificationManager = mUnityNotificationManager;
        }
        return unityNotificationManager;
    }

    public static synchronized UnityNotificationManager getNotificationManagerImpl(Activity activity, NotificationCallback notificationCallback) {
        UnityNotificationManager unityNotificationManager;
        synchronized (UnityNotificationManager.class) {
            if (mUnityNotificationManager == null) {
                mUnityNotificationManager = new UnityNotificationManager();
            }
            mUnityNotificationManager.initialize(activity, notificationCallback);
            unityNotificationManager = mUnityNotificationManager;
        }
        return unityNotificationManager;
    }

    private Bundle getAppMetadata() {
        try {
            return this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128).metaData;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) this.mContext.getSystemService("notification");
    }

    public int getTargetSdk() {
        return this.mContext.getApplicationInfo().targetSdkVersion;
    }

    public void registerNotificationChannel(String str, String str2, int i, String str3, boolean z, boolean z2, boolean z3, boolean z4, long[] jArr, int i2) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(str, str2, i);
            notificationChannel.setDescription(str3);
            notificationChannel.enableLights(z);
            notificationChannel.enableVibration(z2);
            notificationChannel.setBypassDnd(z3);
            notificationChannel.setShowBadge(z4);
            notificationChannel.setVibrationPattern(jArr);
            notificationChannel.setLockscreenVisibility(i2);
            getNotificationManager().createNotificationChannel(notificationChannel);
            return;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(NOTIFICATION_CHANNELS_SHARED_PREFS, 0);
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet(NOTIFICATION_CHANNELS_SHARED_PREFS_KEY, new HashSet()));
        hashSet.add(str);
        SharedPreferences.Editor clear = sharedPreferences.edit().clear();
        clear.putStringSet(NOTIFICATION_CHANNELS_SHARED_PREFS_KEY, hashSet);
        clear.apply();
        SharedPreferences.Editor edit = this.mContext.getSharedPreferences(getSharedPrefsNameByChannelId(str), 0).edit();
        edit.putString("title", str2);
        edit.putInt("importance", i);
        edit.putString("description", str3);
        edit.putBoolean("enableLights", z);
        edit.putBoolean("enableVibration", z2);
        edit.putBoolean("canBypassDnd", z3);
        edit.putBoolean("canShowBadge", z4);
        edit.putString("vibrationPattern", Arrays.toString(jArr));
        edit.putInt("lockscreenVisibility", i2);
        edit.apply();
    }

    private static String getSharedPrefsNameByChannelId(String str) {
        return String.format("unity_notification_channel_%s", new Object[]{str});
    }

    public NotificationChannelWrapper getNotificationChannel(String str) {
        long[] jArr = null;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = getNotificationManagerImpl(this.mContext).getNotificationManager().getNotificationChannel(str);
            if (notificationChannel == null) {
                return null;
            }
            return notificationChannelToWrapper(notificationChannel);
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(getSharedPrefsNameByChannelId(str), 0);
        NotificationChannelWrapper notificationChannelWrapper = new NotificationChannelWrapper();
        notificationChannelWrapper.f12id = str;
        notificationChannelWrapper.name = sharedPreferences.getString("title", "undefined");
        notificationChannelWrapper.importance = sharedPreferences.getInt("importance", 3);
        notificationChannelWrapper.description = sharedPreferences.getString("description", "undefined");
        notificationChannelWrapper.enableLights = sharedPreferences.getBoolean("enableLights", false);
        notificationChannelWrapper.enableVibration = sharedPreferences.getBoolean("enableVibration", false);
        notificationChannelWrapper.canBypassDnd = sharedPreferences.getBoolean("canBypassDnd", false);
        notificationChannelWrapper.canShowBadge = sharedPreferences.getBoolean("canShowBadge", false);
        notificationChannelWrapper.lockscreenVisibility = sharedPreferences.getInt("lockscreenVisibility", 1);
        String[] split = sharedPreferences.getString("vibrationPattern", "[]").split(",");
        int length = split.length;
        long[] jArr2 = new long[length];
        if (length > 1) {
            for (int i = 0; i < split.length; i++) {
                try {
                    jArr2[i] = Long.parseLong(split[i]);
                } catch (NumberFormatException unused) {
                    jArr2[i] = 1;
                }
            }
        }
        if (length > 1) {
            jArr = jArr2;
        }
        notificationChannelWrapper.vibrationPattern = jArr;
        return notificationChannelWrapper;
    }

    private static NotificationChannelWrapper notificationChannelToWrapper(Object obj) {
        NotificationChannel notificationChannel = (NotificationChannel) obj;
        NotificationChannelWrapper notificationChannelWrapper = new NotificationChannelWrapper();
        notificationChannelWrapper.f12id = notificationChannel.getId();
        notificationChannelWrapper.name = notificationChannel.getName().toString();
        notificationChannelWrapper.importance = notificationChannel.getImportance();
        notificationChannelWrapper.description = notificationChannel.getDescription();
        notificationChannelWrapper.enableLights = notificationChannel.shouldShowLights();
        notificationChannelWrapper.enableVibration = notificationChannel.shouldVibrate();
        notificationChannelWrapper.canBypassDnd = notificationChannel.canBypassDnd();
        notificationChannelWrapper.canShowBadge = notificationChannel.canShowBadge();
        notificationChannelWrapper.vibrationPattern = notificationChannel.getVibrationPattern();
        notificationChannelWrapper.lockscreenVisibility = notificationChannel.getLockscreenVisibility();
        return notificationChannelWrapper;
    }

    public void deleteNotificationChannel(String str) {
        if (Build.VERSION.SDK_INT >= 26) {
            getNotificationManager().deleteNotificationChannel(str);
            return;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(NOTIFICATION_CHANNELS_SHARED_PREFS, 0);
        Set<String> stringSet = sharedPreferences.getStringSet(NOTIFICATION_CHANNELS_SHARED_PREFS_KEY, new HashSet());
        if (stringSet.contains(str)) {
            HashSet hashSet = new HashSet(stringSet);
            hashSet.remove(str);
            SharedPreferences.Editor clear = sharedPreferences.edit().clear();
            clear.putStringSet(NOTIFICATION_CHANNELS_SHARED_PREFS_KEY, hashSet);
            clear.apply();
            this.mContext.getSharedPreferences(getSharedPrefsNameByChannelId(str), 0).edit().clear().apply();
        }
    }

    public NotificationChannelWrapper[] getNotificationChannels() {
        int i = 0;
        if (Build.VERSION.SDK_INT >= 26) {
            List<NotificationChannel> notificationChannels = getNotificationManager().getNotificationChannels();
            if (notificationChannels.size() == 0) {
                return null;
            }
            NotificationChannelWrapper[] notificationChannelWrapperArr = new NotificationChannelWrapper[notificationChannels.size()];
            for (NotificationChannel notificationChannelToWrapper : notificationChannels) {
                notificationChannelWrapperArr[i] = notificationChannelToWrapper(notificationChannelToWrapper);
                i++;
            }
            return notificationChannelWrapperArr;
        }
        Set<String> stringSet = this.mContext.getSharedPreferences(NOTIFICATION_CHANNELS_SHARED_PREFS, 0).getStringSet(NOTIFICATION_CHANNELS_SHARED_PREFS_KEY, new HashSet());
        if (stringSet.size() == 0) {
            return null;
        }
        NotificationChannelWrapper[] notificationChannelWrapperArr2 = new NotificationChannelWrapper[stringSet.size()];
        for (String notificationChannel : stringSet) {
            notificationChannelWrapperArr2[i] = getNotificationChannel(notificationChannel);
            i++;
        }
        return notificationChannelWrapperArr2;
    }

    private int generateUniqueId() {
        int i = 0;
        do {
            i += this.mRandom.nextInt(1000);
        } while (this.mScheduledNotifications.containsKey(Integer.valueOf(i)));
        return i;
    }

    public int scheduleNotification(Notification.Builder builder, boolean z) {
        int i;
        Bundle extras = builder.getExtras();
        if (extras.containsKey(KEY_ID)) {
            i = builder.getExtras().getInt(KEY_ID, -1);
        } else {
            int generateUniqueId = generateUniqueId();
            extras.putInt(KEY_ID, generateUniqueId);
            i = generateUniqueId;
        }
        this.mBackgroundThread.enqueueNotification(i, builder, z, this.mScheduledNotifications.putIfAbsent(Integer.valueOf(i), builder) == null);
        return i;
    }

    /* access modifiers changed from: package-private */
    public void performNotificationScheduling(int i, Notification.Builder builder, boolean z) {
        Bundle extras = builder.getExtras();
        long j = extras.getLong(KEY_REPEAT_INTERVAL, -1);
        long j2 = extras.getLong(KEY_FIRE_TIME, -1);
        boolean z2 = j2 - Calendar.getInstance().getTime().getTime() < 1000;
        if (!z2 || j > 0) {
            if (z2) {
                j2 += j;
            }
            Intent buildNotificationIntent = buildNotificationIntent();
            if (buildNotificationIntent != null) {
                saveNotification(builder.build(), z);
                scheduleAlarmWithNotification(builder, buildNotificationIntent, j2);
            }
        }
        if (z2) {
            notify(i, buildNotificationForSending(this.mOpenActivity, builder));
        }
    }

    /* access modifiers changed from: package-private */
    public void scheduleAlarmWithNotification(Notification.Builder builder, Intent intent, long j) {
        Bundle extras = builder.getExtras();
        int i = extras.getInt(KEY_ID, -1);
        long j2 = extras.getLong(KEY_REPEAT_INTERVAL, -1);
        this.mScheduledNotifications.putIfAbsent(Integer.valueOf(i), builder);
        intent.putExtra(KEY_NOTIFICATION_ID, i);
        scheduleNotificationIntentAlarm(j2, j, getBroadcastPendingIntent(i, intent, 134217728));
    }

    /* access modifiers changed from: package-private */
    public void scheduleAlarmWithNotification(Notification.Builder builder) {
        scheduleAlarmWithNotification(builder, buildNotificationIntent(), builder.getExtras().getLong(KEY_FIRE_TIME, 0));
    }

    private Notification buildNotificationForSending(Class cls, Notification.Builder builder) {
        int i = builder.getExtras().getInt(KEY_ID, -1);
        Intent intent = new Intent(this.mContext, cls);
        intent.addFlags(805306368);
        intent.putExtra(KEY_NOTIFICATION_ID, i);
        builder.setContentIntent(getActivityPendingIntent(i, intent, 0));
        if (Build.VERSION.SDK_INT < 23) {
            Intent intent2 = new Intent(this.mContext, UnityNotificationManager.class);
            intent2.setAction(KEY_NOTIFICATION_DISMISSED);
            intent2.putExtra(KEY_NOTIFICATION_DISMISSED, i);
            builder.setDeleteIntent(getBroadcastPendingIntent(i, intent2, 0));
        }
        finalizeNotificationForDisplay(builder);
        return builder.build();
    }

    /* access modifiers changed from: package-private */
    public void performNotificationHousekeeping(Set<String> set) {
        Log.d(TAG_UNITY, "Checking for invalid notification IDs still hanging around");
        Set<String> findInvalidNotificationIds = findInvalidNotificationIds(set);
        HashSet hashSet = new HashSet(set);
        for (String next : findInvalidNotificationIds) {
            hashSet.remove(next);
            this.mScheduledNotifications.remove(next);
        }
        for (String deleteExpiredNotificationIntent : findInvalidNotificationIds) {
            deleteExpiredNotificationIntent(deleteExpiredNotificationIntent);
        }
    }

    private Set<String> findInvalidNotificationIds(Set<String> set) {
        Intent buildNotificationIntent = buildNotificationIntent();
        HashSet hashSet = new HashSet();
        for (String next : set) {
            if (getBroadcastPendingIntent(Integer.valueOf(next).intValue(), buildNotificationIntent, 536870912) == null) {
                hashSet.add(next);
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            for (StatusBarNotification id : getNotificationManager().getActiveNotifications()) {
                hashSet.remove(String.valueOf(id.getId()));
            }
        } else {
            synchronized (this) {
                Iterator<Integer> it = this.mVisibleNotifications.iterator();
                while (it.hasNext()) {
                    hashSet.remove(String.valueOf(it.next()));
                }
            }
        }
        if (UnityPlayer.currentActivity != null) {
            Intent intent = UnityPlayer.currentActivity.getIntent();
            if (intent.hasExtra(KEY_NOTIFICATION_ID)) {
                hashSet.remove(String.valueOf(intent.getExtras().getInt(KEY_NOTIFICATION_ID)));
            }
        }
        return hashSet;
    }

    private Intent buildNotificationIntent() {
        Intent intent = new Intent(this.mContext, UnityNotificationManager.class);
        intent.setFlags(268468224);
        return intent;
    }

    private PendingIntent getActivityPendingIntent(int i, Intent intent, int i2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PendingIntent.getActivity(this.mContext, i, intent, i2 | 67108864);
        }
        return PendingIntent.getActivity(this.mContext, i, intent, i2);
    }

    private PendingIntent getBroadcastPendingIntent(int i, Intent intent, int i2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PendingIntent.getBroadcast(this.mContext, i, intent, i2 | 67108864);
        }
        return PendingIntent.getBroadcast(this.mContext, i, intent, i2);
    }

    /* access modifiers changed from: package-private */
    public synchronized void saveNotification(Notification notification, boolean z) {
        UnityNotificationUtilities.serializeNotification(this.mContext.getSharedPreferences(getSharedPrefsNameByNotificationId(Integer.toString(notification.extras.getInt(KEY_ID, -1))), 0), notification, z);
    }

    static String getSharedPrefsNameByNotificationId(String str) {
        return String.format("u_notification_data_%s", new Object[]{str});
    }

    /* access modifiers changed from: package-private */
    public synchronized List<Notification.Builder> loadSavedNotifications() {
        ArrayList arrayList;
        Set<String> scheduledNotificationIDs = getScheduledNotificationIDs();
        arrayList = new ArrayList();
        HashSet<String> hashSet = new HashSet<>();
        for (String next : scheduledNotificationIDs) {
            Notification.Builder builder = null;
            Object deserializeNotification = UnityNotificationUtilities.deserializeNotification(this.mContext, this.mContext.getSharedPreferences(getSharedPrefsNameByNotificationId(next), 0));
            if (deserializeNotification != null) {
                if (deserializeNotification instanceof Notification.Builder) {
                    builder = (Notification.Builder) deserializeNotification;
                } else {
                    builder = UnityNotificationUtilities.recoverBuilder(this.mContext, (Notification) deserializeNotification);
                }
            }
            if (builder != null) {
                arrayList.add(builder);
            } else {
                hashSet.add(next);
            }
        }
        if (hashSet.size() > 0) {
            HashSet hashSet2 = new HashSet(scheduledNotificationIDs);
            for (String str : hashSet) {
                hashSet2.remove(str);
                deleteExpiredNotificationIntent(str);
            }
            saveScheduledNotificationIDs(hashSet2);
        }
        return arrayList;
    }

    private boolean canScheduleExactAlarms(AlarmManager alarmManager) {
        Bundle appMetadata;
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        if (this.mExactSchedulingSetting < 0 && (appMetadata = getAppMetadata()) != null) {
            this.mExactSchedulingSetting = appMetadata.getInt("com.unity.androidnotifications.exact_scheduling", 1);
        }
        if (this.mExactSchedulingSetting == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 31) {
            return true;
        }
        try {
            if (this.mCanScheduleExactAlarms == null) {
                this.mCanScheduleExactAlarms = AlarmManager.class.getMethod("canScheduleExactAlarms", new Class[0]);
            }
            return ((Boolean) this.mCanScheduleExactAlarms.invoke(alarmManager, new Object[0])).booleanValue();
        } catch (NoSuchMethodException e) {
            Log.e(TAG_UNITY, "No AlarmManager.canScheduleExactAlarms() on Android 31+ device, should not happen", e);
            return false;
        } catch (Exception e2) {
            Log.e(TAG_UNITY, "AlarmManager.canScheduleExactAlarms() threw", e2);
            return false;
        }
    }

    private void scheduleNotificationIntentAlarm(long j, long j2, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        if (j > 0) {
            alarmManager.setInexactRepeating(0, j2, j, pendingIntent);
        } else if (Build.VERSION.SDK_INT < 23 || !canScheduleExactAlarms(alarmManager)) {
            alarmManager.set(0, j2, pendingIntent);
        } else {
            alarmManager.setExactAndAllowWhileIdle(0, j2, pendingIntent);
        }
    }

    public int checkNotificationStatus(int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (StatusBarNotification id : getNotificationManager().getActiveNotifications()) {
                if (i == id.getId()) {
                    return 2;
                }
            }
        } else {
            synchronized (this) {
                Iterator<Integer> it = this.mVisibleNotifications.iterator();
                while (it.hasNext()) {
                    if (it.next().intValue() == i) {
                        return 2;
                    }
                }
            }
        }
        if (!this.mScheduledNotifications.containsKey(Integer.valueOf(i)) && !checkIfPendingNotificationIsRegistered(i)) {
            return 0;
        }
        return 1;
    }

    public boolean checkIfPendingNotificationIsRegistered(int i) {
        return getBroadcastPendingIntent(i, new Intent(this.mActivity, UnityNotificationManager.class), 536870912) != null;
    }

    public void cancelAllPendingNotificationIntents() {
        this.mBackgroundThread.enqueueCancelAllNotifications();
    }

    private synchronized Set<String> getScheduledNotificationIDs() {
        return this.mContext.getSharedPreferences(NOTIFICATION_IDS_SHARED_PREFS, 0).getStringSet(NOTIFICATION_IDS_SHARED_PREFS_KEY, new HashSet());
    }

    /* access modifiers changed from: package-private */
    public synchronized void saveScheduledNotificationIDs(Set<String> set) {
        SharedPreferences.Editor clear = this.mContext.getSharedPreferences(NOTIFICATION_IDS_SHARED_PREFS, 0).edit().clear();
        clear.putStringSet(NOTIFICATION_IDS_SHARED_PREFS_KEY, set);
        clear.apply();
    }

    public void cancelPendingNotification(int i) {
        this.mBackgroundThread.enqueueCancelNotification(i);
    }

    /* access modifiers changed from: package-private */
    public void cancelPendingNotificationIntent(int i) {
        PendingIntent broadcastPendingIntent = getBroadcastPendingIntent(i, new Intent(this.mContext, UnityNotificationManager.class), 536870912);
        if (broadcastPendingIntent != null) {
            ((AlarmManager) this.mContext.getSystemService("alarm")).cancel(broadcastPendingIntent);
            broadcastPendingIntent.cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void deleteExpiredNotificationIntent(String str) {
        this.mContext.getSharedPreferences(getSharedPrefsNameByNotificationId(str), 0).edit().clear().apply();
    }

    public void cancelDisplayedNotification(int i) {
        getNotificationManager().cancel(i);
    }

    public void cancelAllNotifications() {
        getNotificationManager().cancelAll();
    }

    public void onReceive(Context context, Intent intent) {
        getNotificationManagerImpl(context).onReceive(intent);
    }

    public void onReceive(Intent intent) {
        Notification notification;
        int i;
        if (Build.VERSION.SDK_INT >= 23 || !KEY_NOTIFICATION_DISMISSED.equals(intent.getAction())) {
            Object notificationOrBuilderForIntent = getNotificationOrBuilderForIntent(intent);
            if (notificationOrBuilderForIntent != null) {
                if (notificationOrBuilderForIntent instanceof Notification) {
                    notification = (Notification) notificationOrBuilderForIntent;
                    i = notification.extras.getInt(KEY_ID, -1);
                } else {
                    Notification.Builder builder = (Notification.Builder) notificationOrBuilderForIntent;
                    if (builder == null) {
                        Log.e(TAG_UNITY, "Failed to recover builder, can't send notification");
                        return;
                    }
                    Class<?> cls = this.mOpenActivity;
                    if (cls == null) {
                        cls = UnityNotificationUtilities.getOpenAppActivity(this.mContext, true);
                    }
                    int i2 = builder.getExtras().getInt(KEY_ID, -1);
                    notification = buildNotificationForSending(cls, builder);
                    i = i2;
                }
                if (notification != null) {
                    notify(i, notification);
                    return;
                }
                return;
            }
            return;
        }
        int intExtra = intent.getIntExtra(KEY_NOTIFICATION_DISMISSED, -1);
        if (intExtra > 0) {
            synchronized (this) {
                this.mVisibleNotifications.remove(Integer.valueOf(intExtra));
            }
        }
    }

    private void notify(int i, Notification notification) {
        boolean z = notification.extras.getBoolean(KEY_SHOW_IN_FOREGROUND, true);
        if (!isInForeground() || z) {
            getNotificationManager().notify(i, notification);
            if (Build.VERSION.SDK_INT < 23) {
                synchronized (this) {
                    this.mVisibleNotifications.add(Integer.valueOf(i));
                }
            }
        }
        if (notification.extras.getLong(KEY_REPEAT_INTERVAL, -1) <= 0) {
            this.mScheduledNotifications.remove(Integer.valueOf(i));
            cancelPendingNotificationIntent(i);
        }
        try {
            this.mNotificationCallback.onSentNotification(notification);
        } catch (RuntimeException unused) {
            Log.w(TAG_UNITY, "Can not invoke OnNotificationReceived event when the app is not running!");
        }
    }

    public static Integer getNotificationColor(Notification notification) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 26 || notification.extras.containsKey("android.colorized")) {
            return Integer.valueOf(notification.color);
        }
        return null;
    }

    public static int getNotificationGroupAlertBehavior(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        }
        return 0;
    }

    private void finalizeNotificationForDisplay(Notification.Builder builder) {
        int findResourceIdInContextByName = UnityNotificationUtilities.findResourceIdInContextByName(this.mContext, builder.getExtras().getString(KEY_SMALL_ICON));
        if (findResourceIdInContextByName == 0) {
            findResourceIdInContextByName = this.mContext.getApplicationInfo().icon;
        }
        builder.setSmallIcon(findResourceIdInContextByName);
        int findResourceIdInContextByName2 = UnityNotificationUtilities.findResourceIdInContextByName(this.mContext, builder.getExtras().getString(KEY_LARGE_ICON));
        if (findResourceIdInContextByName2 != 0) {
            builder.setLargeIcon(BitmapFactory.decodeResource(this.mContext.getResources(), findResourceIdInContextByName2));
        }
    }

    public Notification.Builder createNotificationBuilder(String str) {
        if (Build.VERSION.SDK_INT >= 26) {
            return new Notification.Builder(this.mContext, str);
        }
        Notification.Builder builder = new Notification.Builder(this.mContext);
        NotificationChannelWrapper notificationChannel = getNotificationChannel(str);
        int i = -1;
        if (notificationChannel.vibrationPattern == null || notificationChannel.vibrationPattern.length <= 0) {
            builder.setDefaults(-1);
        } else {
            builder.setDefaults(5);
            builder.setVibrate(notificationChannel.vibrationPattern);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(notificationChannel.lockscreenVisibility);
        }
        int i2 = notificationChannel.importance;
        if (i2 == 0) {
            i = -2;
        } else if (i2 != 2) {
            i = (i2 == 3 || i2 != 4) ? 0 : 2;
        }
        builder.setPriority(i);
        builder.getExtras().putString(KEY_CHANNEL_ID, str);
        return builder;
    }

    public static void setNotificationIcon(Notification.Builder builder, String str, String str2) {
        if (str2 == null || (str2.length() == 0 && builder.getExtras().getString(str) != null)) {
            builder.getExtras().remove(str);
        } else {
            builder.getExtras().putString(str, str2);
        }
    }

    public static void setNotificationColor(Notification.Builder builder, int i) {
        if (Build.VERSION.SDK_INT >= 21 && i != 0) {
            builder.setColor(i);
            if (Build.VERSION.SDK_INT >= 26) {
                builder.setColorized(true);
            }
        }
    }

    public static void setNotificationUsesChronometer(Notification.Builder builder, boolean z) {
        if (Build.VERSION.SDK_INT >= 22) {
            builder.setUsesChronometer(z);
        }
    }

    public static void setNotificationGroupAlertBehavior(Notification.Builder builder, int i) {
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setGroupAlertBehavior(i);
        }
    }

    public static String getNotificationChannelId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        }
        return null;
    }

    private static boolean isInForeground() {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(runningAppProcessInfo);
        return runningAppProcessInfo.importance == 100 || runningAppProcessInfo.importance == 200;
    }

    public Notification getNotificationFromIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 23 && intent.hasExtra(KEY_NOTIFICATION_ID)) {
            int i = intent.getExtras().getInt(KEY_NOTIFICATION_ID);
            for (StatusBarNotification statusBarNotification : getNotificationManager().getActiveNotifications()) {
                if (statusBarNotification.getId() == i) {
                    return statusBarNotification.getNotification();
                }
            }
        }
        Object notificationOrBuilderForIntent = getNotificationOrBuilderForIntent(intent);
        if (notificationOrBuilderForIntent == null) {
            return null;
        }
        if (notificationOrBuilderForIntent instanceof Notification) {
            return (Notification) notificationOrBuilderForIntent;
        }
        return ((Notification.Builder) notificationOrBuilderForIntent).build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0045 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object getNotificationOrBuilderForIntent(android.content.Intent r5) {
        /*
            r4 = this;
            java.lang.String r0 = "com.unity.NotificationID"
            boolean r1 = r5.hasExtra(r0)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0034
            android.os.Bundle r5 = r5.getExtras()
            int r5 = r5.getInt(r0)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r5)
            java.util.concurrent.ConcurrentHashMap<java.lang.Integer, android.app.Notification$Builder> r1 = r4.mScheduledNotifications
            java.lang.Object r0 = r1.get(r0)
            if (r0 == 0) goto L_0x001f
            goto L_0x0043
        L_0x001f:
            android.content.Context r0 = r4.mContext
            java.lang.String r5 = java.lang.String.valueOf(r5)
            java.lang.String r5 = getSharedPrefsNameByNotificationId(r5)
            android.content.SharedPreferences r5 = r0.getSharedPreferences(r5, r3)
            android.content.Context r0 = r4.mContext
            java.lang.Object r0 = com.unity.androidnotifications.UnityNotificationUtilities.deserializeNotification((android.content.Context) r0, (android.content.SharedPreferences) r5)
            goto L_0x0042
        L_0x0034:
            java.lang.String r0 = "unityNotification"
            boolean r1 = r5.hasExtra(r0)
            if (r1 == 0) goto L_0x0041
            android.os.Parcelable r0 = r5.getParcelableExtra(r0)
            goto L_0x0043
        L_0x0041:
            r0 = 0
        L_0x0042:
            r2 = 0
        L_0x0043:
            if (r0 == 0) goto L_0x0059
            if (r2 == 0) goto L_0x0048
            goto L_0x0059
        L_0x0048:
            boolean r5 = r0 instanceof android.app.Notification
            if (r5 == 0) goto L_0x0055
            android.content.Context r5 = r4.mContext
            android.app.Notification r0 = (android.app.Notification) r0
            android.app.Notification$Builder r5 = com.unity.androidnotifications.UnityNotificationUtilities.recoverBuilder(r5, r0)
            goto L_0x0058
        L_0x0055:
            r5 = r0
            android.app.Notification$Builder r5 = (android.app.Notification.Builder) r5
        L_0x0058:
            return r5
        L_0x0059:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity.androidnotifications.UnityNotificationManager.getNotificationOrBuilderForIntent(android.content.Intent):java.lang.Object");
    }

    public void showNotificationSettings(String str) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 26) {
            intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.mContext.getPackageName(), (String) null));
        } else {
            if (str == null || str.length() <= 0) {
                intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
            } else {
                Intent intent2 = new Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS");
                intent2.putExtra("android.provider.extra.CHANNEL_ID", str);
                intent = intent2;
            }
            intent.putExtra("android.provider.extra.APP_PACKAGE", this.mContext.getPackageName());
        }
        intent.addFlags(268435456);
        this.mActivity.startActivity(intent);
    }
}
