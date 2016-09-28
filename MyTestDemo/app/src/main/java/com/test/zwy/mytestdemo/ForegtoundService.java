package com.test.zwy.mytestdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.test.zwy.main.MainActivity;

/**
 * Created by FERO010 on 2016/6/13.
 */
public class ForegtoundService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
//        Notification notification = new Notification(R.drawable.auth_logo, "Notification comes", System.currentTimeMillis());
//        Intent notificationIntent = new Intent(this, MainActivity.this);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(this, "This is title", "This is content", pendingIntent);
//        startForeground(1, notification);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.authlogo)).getBitmap();
        builder.setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.authlogo)
                .setContentTitle("Botification")
                .setContentText("notification")
                .setTicker("MyNotification")
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));
        final Notification notification = builder.getNotification();
        notificationManager.notify(1, notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
