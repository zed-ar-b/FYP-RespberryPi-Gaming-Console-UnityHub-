package com.unity.androidnotifications;

import android.app.Notification;
import android.util.Log;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;

public class UnityNotificationBackgroundThread extends Thread {
    private static final int TASKS_FOR_HOUSEKEEPING = 50;
    private UnityNotificationManager mManager;
    private ConcurrentHashMap<Integer, Notification.Builder> mScheduledNotifications;
    private LinkedTransferQueue<Task> mTasks = new LinkedTransferQueue<>();
    private int mTasksSinceHousekeeping = TASKS_FOR_HOUSEKEEPING;

    private static abstract class Task {
        public abstract boolean run(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap);

        private Task() {
        }
    }

    private static class ScheduleNotificationTask extends Task {
        private boolean isCustomized;
        private boolean isNew;
        private Notification.Builder notificationBuilder;
        private int notificationId;

        public ScheduleNotificationTask(int i, Notification.Builder builder, boolean z, boolean z2) {
            super();
            this.notificationId = i;
            this.notificationBuilder = builder;
            this.isCustomized = z;
            this.isNew = z2;
        }

        public boolean run(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
            String valueOf = String.valueOf(this.notificationId);
            Integer.valueOf(this.notificationId);
            try {
                UnityNotificationManager.mUnityNotificationManager.performNotificationScheduling(this.notificationId, this.notificationBuilder, this.isCustomized);
                return this.isNew;
            } catch (Throwable th) {
                concurrentHashMap.remove(Integer.valueOf(this.notificationId));
                unityNotificationManager.cancelPendingNotificationIntent(this.notificationId);
                unityNotificationManager.deleteExpiredNotificationIntent(valueOf);
                throw th;
            }
        }
    }

    private static class CancelNotificationTask extends Task {
        private int notificationId;

        public CancelNotificationTask(int i) {
            super();
            this.notificationId = i;
        }

        public boolean run(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
            unityNotificationManager.cancelPendingNotificationIntent(this.notificationId);
            if (concurrentHashMap.remove(Integer.valueOf(this.notificationId)) == null) {
                return false;
            }
            unityNotificationManager.deleteExpiredNotificationIntent(String.valueOf(this.notificationId));
            return true;
        }
    }

    private static class CancelAllNotificationsTask extends Task {
        private CancelAllNotificationsTask() {
            super();
        }

        public boolean run(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
            if (concurrentHashMap.isEmpty()) {
                return false;
            }
            Enumeration<Integer> keys = concurrentHashMap.keys();
            while (keys.hasMoreElements()) {
                Integer nextElement = keys.nextElement();
                unityNotificationManager.cancelPendingNotificationIntent(nextElement.intValue());
                unityNotificationManager.deleteExpiredNotificationIntent(String.valueOf(nextElement));
            }
            concurrentHashMap.clear();
            return true;
        }
    }

    private static class HousekeepingTask extends Task {
        UnityNotificationBackgroundThread thread;

        public HousekeepingTask(UnityNotificationBackgroundThread unityNotificationBackgroundThread) {
            super();
            this.thread = unityNotificationBackgroundThread;
        }

        public boolean run(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
            HashSet hashSet = new HashSet();
            Enumeration<Integer> keys = concurrentHashMap.keys();
            while (keys.hasMoreElements()) {
                hashSet.add(String.valueOf(keys.nextElement()));
            }
            this.thread.performHousekeeping(hashSet);
            return false;
        }
    }

    public UnityNotificationBackgroundThread(UnityNotificationManager unityNotificationManager, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
        this.mManager = unityNotificationManager;
        this.mScheduledNotifications = concurrentHashMap;
        if (concurrentHashMap.size() == 0) {
            loadNotifications();
        }
    }

    public void enqueueNotification(int i, Notification.Builder builder, boolean z, boolean z2) {
        this.mTasks.add(new ScheduleNotificationTask(i, builder, z, z2));
    }

    public void enqueueCancelNotification(int i) {
        this.mTasks.add(new CancelNotificationTask(i));
    }

    public void enqueueCancelAllNotifications() {
        this.mTasks.add(new CancelAllNotificationsTask());
    }

    private void enqueueHousekeeping() {
        this.mTasks.add(new HousekeepingTask(this));
    }

    public void run() {
        while (true) {
            boolean z = false;
            while (true) {
                try {
                    Task take = this.mTasks.take();
                    z |= executeTask(this.mManager, take, this.mScheduledNotifications);
                    if (!(take instanceof HousekeepingTask)) {
                        this.mTasksSinceHousekeeping++;
                    }
                    if (this.mTasks.size() == 0 && z) {
                        try {
                            enqueueHousekeeping();
                        } catch (InterruptedException unused) {
                            z = false;
                        }
                    }
                } catch (InterruptedException unused2) {
                    if (this.mTasks.isEmpty()) {
                        return;
                    }
                }
            }
        }
    }

    private boolean executeTask(UnityNotificationManager unityNotificationManager, Task task, ConcurrentHashMap<Integer, Notification.Builder> concurrentHashMap) {
        try {
            return task.run(unityNotificationManager, concurrentHashMap);
        } catch (Exception e) {
            Log.e("UnityNotifications", "Exception executing notification task", e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void performHousekeeping(Set<String> set) {
        boolean z = this.mTasksSinceHousekeeping >= TASKS_FOR_HOUSEKEEPING;
        this.mTasksSinceHousekeeping = 0;
        if (z) {
            this.mManager.performNotificationHousekeeping(set);
        }
        this.mManager.saveScheduledNotificationIDs(set);
    }

    private void loadNotifications() {
        for (Notification.Builder next : this.mManager.loadSavedNotifications()) {
            this.mScheduledNotifications.put(Integer.valueOf(next.getExtras().getInt("id", -1)), next);
        }
    }
}
