package com.clauzon.clauzcliente.Servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.clauzon.clauzcliente.MainActivity;
import com.clauzon.clauzcliente.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

public class FCM extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String frcsm =remoteMessage.getFrom();

        if(remoteMessage.getData().size()>0){
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            String imagen = remoteMessage.getData().get("imagen");
            notificacion_mayor_oreo(titulo,detalle,imagen);
        }

    }

    private void notificacion_mayor_oreo(String titulo, String detalle,String imagen) {
        String id ="mensaje";

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }
        try {
            Bitmap picasso = Picasso.with(getApplicationContext()).load(imagen).get();

            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(titulo)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(detalle)
                    .setContentInfo("nuevo")
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setSmallIcon(R.drawable.claudia)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picasso).bigLargeIcon(null))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLights(Color.WHITE, 1000, 1000)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(click_notificacion());

            Random random=new Random();
            int id_notify=random.nextInt(8000);
            manager.notify(id_notify,builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public PendingIntent click_notificacion(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,intent,0);
    }
}
