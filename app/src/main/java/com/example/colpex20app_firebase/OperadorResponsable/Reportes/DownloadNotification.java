package com.example.colpex20app_firebase.OperadorResponsable.Reportes;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.colpex20app_firebase.R;

public class DownloadNotification {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public void showNotification(Context context, int progress, String title) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(context, "download_channel")
                    .setSmallIcon(R.drawable.flecha_volver)
                    .setContentTitle(title)
                    .setProgress(100, progress, false)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true);
        }
        mBuilder.setProgress(100, progress, false);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
