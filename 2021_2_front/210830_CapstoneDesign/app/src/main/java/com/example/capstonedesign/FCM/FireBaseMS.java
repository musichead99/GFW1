package com.example.capstonedesign.FCM;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.capstonedesign.MainActivity;
import com.example.capstonedesign.MySQLite.DbOpenHelper;
import com.example.capstonedesign.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FireBaseMS extends FirebaseMessagingService {
    private static final String TAG = "FCM_TEST";
    private static final String CHANNEL_ID = "test123";
    private static final String CHANNEL_NAME = "FCM_TEST";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("onMessageReceiver","in");

        Map<String,String> data = remoteMessage.getData();

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("title",remoteMessage.getNotification().getTitle());
        //intent.putExtra("content",remoteMessage.getNotification().getBody());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext()); //from(getApplicationContext());

        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        DbOpenHelper dbOpenHelper = new DbOpenHelper(getApplicationContext());
        dbOpenHelper.open();
        dbOpenHelper.create();

        String profilePhoto = data.get("profilePhoto");
        String title = data.get("title");
        String body = data.get("body");

        dbOpenHelper.insertColumn(profilePhoto,title,body);
        dbOpenHelper.close();

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent,true);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }
}
