package com.upreal.utils;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.upreal.R;

/**
 * Created by Kyosukke on 10/07/2015.
 */
public class WearManager {

    public static void notifyWear(Context context, String msg) {
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("UpReal")
                .setContentText(msg)
                .extend(
                        new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }
}
