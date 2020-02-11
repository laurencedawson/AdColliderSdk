package com.adcollider.sdk.util.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.R;

public final class NotificationHelper {

    private static final String ID = "AdCollider";

    private NotificationHelper(){
        // N/A
    }

    private static void setupNotifications(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && AdCollider.getDebug()) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ID, "AdCollider", importance);
            channel.setDescription("AdCollider debug notifications");

            NotificationManager notificationManager = AdCollider.getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(String title, String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && AdCollider.getDebug()) {
            setupNotifications();

            Notification.Builder builder = new Notification.Builder(AdCollider.getApplicationContext(), ID)
                    .setSmallIcon(R.drawable.ic_error_outline_white_48dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_HIGH);

            NotificationManager notificationManager = AdCollider.getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.notify(0, builder.build());
        }
    }

}
