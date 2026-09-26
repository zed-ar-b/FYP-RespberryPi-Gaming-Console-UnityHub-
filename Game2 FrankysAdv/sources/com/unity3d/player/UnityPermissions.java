package com.unity3d.player;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class UnityPermissions {
    private static final String SKIP_DIALOG_METADATA_NAME = "unityplayer.SkipPermissionsDialog";

    public static class ModalWaitForPermissionResponse implements IPermissionRequestCallbacks {
        private boolean haveResponse = false;

        public synchronized void onPermissionDenied(String str) {
            this.haveResponse = true;
            notify();
        }

        public synchronized void onPermissionDeniedAndDontAskAgain(String str) {
            this.haveResponse = true;
            notify();
        }

        public synchronized void onPermissionGranted(String str) {
            this.haveResponse = true;
            notify();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0010, code lost:
            return;
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void waitForResponse() {
            /*
                r1 = this;
                monitor-enter(r1)
                boolean r0 = r1.haveResponse     // Catch:{ InterruptedException -> 0x000f, all -> 0x000c }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r1)
                return
            L_0x0007:
                r1.wait()     // Catch:{ InterruptedException -> 0x000f, all -> 0x000c }
                monitor-exit(r1)
                return
            L_0x000c:
                r0 = move-exception
                monitor-exit(r1)
                throw r0
            L_0x000f:
                monitor-exit(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPermissions.ModalWaitForPermissionResponse.waitForResponse():void");
        }
    }

    private static boolean checkInfoForMetadata(PackageItemInfo packageItemInfo) {
        try {
            return packageItemInfo.metaData.getBoolean(SKIP_DIALOG_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean hasUserAuthorizedPermission(Activity activity, String str) {
        return activity.checkCallingOrSelfPermission(str) == 0;
    }

    public static void requestUserPermissions(Activity activity, String[] strArr, IPermissionRequestCallbacks iPermissionRequestCallbacks) {
        if (activity != null && strArr != null) {
            if (PlatformSupport.MARSHMALLOW_SUPPORT) {
                FragmentManager fragmentManager = activity.getFragmentManager();
                if (fragmentManager.findFragmentByTag("96489") == null) {
                    C0140g gVar = new C0140g(activity, iPermissionRequestCallbacks);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("PermissionNames", strArr);
                    gVar.setArguments(bundle);
                    FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                    beginTransaction.add(0, gVar, "96489");
                    beginTransaction.commit();
                }
            } else if (iPermissionRequestCallbacks != null) {
                for (String onPermissionDeniedAndDontAskAgain : strArr) {
                    iPermissionRequestCallbacks.onPermissionDeniedAndDontAskAgain(onPermissionDeniedAndDontAskAgain);
                }
            }
        }
    }

    public static boolean skipPermissionsDialog(Activity activity) {
        if (!PlatformSupport.MARSHMALLOW_SUPPORT) {
            return false;
        }
        try {
            PackageManager packageManager = activity.getPackageManager();
            return checkInfoForMetadata(packageManager.getActivityInfo(activity.getComponentName(), 128)) || checkInfoForMetadata(packageManager.getApplicationInfo(activity.getPackageName(), 128));
        } catch (Exception unused) {
        }
    }
}
