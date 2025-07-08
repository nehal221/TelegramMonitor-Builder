package com.nehal.telegrammonitor;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class DeviceControl {

    public static void lockNow(Context context) {
        try {
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName compName = new ComponentName(context, AdminReceiver.class);
            if (dpm != null && dpm.isAdminActive(compName)) {
                dpm.lockNow();
            } else {
                Log.e("DeviceControl", "Admin not active. Cannot lock screen.");
            }
        } catch (Exception e) {
            Log.e("DeviceControl", "Lock failed: " + e.getMessage());
        }
    }

    public static void rebootDevice() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
            proc.waitFor();
        } catch (Exception e) {
            Log.e("DeviceControl", "Reboot failed: " + e.getMessage());
        }
    }
}