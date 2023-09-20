package com.example.colpex20app_firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.example.colpex20app_firebase.Model.CircleTransform;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    CircleTransform circleTransform;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token", "mi token es:" + s);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();

        if (remoteMessage.getData().size() > 0) {
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            String contendio = remoteMessage.getData().get("contenido");
            String foto = remoteMessage.getData().get("foto");

            mayorqueoreo(titulo, detalle, contendio, foto);


        }
    }

    private void mayorqueoreo(String titulo, String detalle, String contendio, String foto) {
        String id = "mensaje";
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
        try {
            CircleTransform data = new CircleTransform();
            Bitmap imf_foto = Picasso.get().load(foto).transform(data).get();
            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.colpexvertical)
                    .setContentTitle(titulo)
                    .setContentText(detalle)
                    .setLargeIcon(imf_foto)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contendio))
                    .setContentIntent(clicknoti())

                    .setContentInfo("nuevo");
            Random random = new Random();
            int idNotity = random.nextInt(8000);

            assert nm != null;
            nm.notify(idNotity, builder.build());
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public PendingIntent clicknoti() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            Intent nf = new Intent(getApplicationContext(), MainActivity.class);
            nf.putExtra("color", "rojo");
            nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return PendingIntent.getActivity(this, 0, nf, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            Intent nf = new Intent(getApplicationContext(), MainActivity.class);
            nf.putExtra("color", "rojo");
            nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return PendingIntent.getActivity(this, 0, nf, 0);
        }


    }


}
