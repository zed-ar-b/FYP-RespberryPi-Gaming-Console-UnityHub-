package com.unity3d.services.core.preferences;

import android.content.SharedPreferences;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;

public class AndroidPreferences {
    public static boolean hasKey(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences == null || !sharedPreferences.contains(str2)) {
            return false;
        }
        return true;
    }

    public static String getString(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null && sharedPreferences.contains(str2)) {
            try {
                return sharedPreferences.getString(str2, "");
            } catch (ClassCastException e) {
                DeviceLog.error("Unity Ads failed to cast " + str2 + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static Integer getInteger(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null && sharedPreferences.contains(str2)) {
            try {
                return Integer.valueOf(sharedPreferences.getInt(str2, -1));
            } catch (ClassCastException e) {
                DeviceLog.error("Unity Ads failed to cast " + str2 + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static Long getLong(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null && sharedPreferences.contains(str2)) {
            try {
                return Long.valueOf(sharedPreferences.getLong(str2, -1));
            } catch (ClassCastException e) {
                DeviceLog.error("Unity Ads failed to cast " + str2 + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static Boolean getBoolean(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null && sharedPreferences.contains(str2)) {
            try {
                return Boolean.valueOf(sharedPreferences.getBoolean(str2, false));
            } catch (ClassCastException e) {
                DeviceLog.error("Unity Ads failed to cast " + str2 + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static Float getFloat(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null && sharedPreferences.contains(str2)) {
            try {
                return Float.valueOf(sharedPreferences.getFloat(str2, Float.NaN));
            } catch (ClassCastException e) {
                DeviceLog.error("Unity Ads failed to cast " + str2 + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static void setString(String str, String str2, String str3) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(str2, str3);
            edit.commit();
        }
    }

    public static void setInteger(String str, String str2, Integer num) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(str2, num.intValue());
            edit.commit();
        }
    }

    public static void setLong(String str, String str2, Long l) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong(str2, l.longValue());
            edit.commit();
        }
    }

    public static void setBoolean(String str, String str2, Boolean bool) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(str2, bool.booleanValue());
            edit.commit();
        }
    }

    public static void setFloat(String str, String str2, Double d) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putFloat(str2, d.floatValue());
            edit.commit();
        }
    }

    public static void removeKey(String str, String str2) {
        SharedPreferences sharedPreferences = ClientProperties.getApplicationContext().getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(str2);
            edit.commit();
        }
    }
}
