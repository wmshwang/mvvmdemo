package com.desaysv.mvvmdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static MyService instance;

    public static MyService getInstance() {
        return instance;
    }

    public static void setInstance(MyService instance) {
        MyService.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(" MyService ", " onCreate ");
        setInstance(this);
        setNotification();//设置通知
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setInstance(null);
    }

    //设置通知
    public static void setNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {//适配8.0service
            NotificationManager notificationManager = (NotificationManager) instance.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel("InitService", "主服务", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(instance, "InitService").build();
            instance.startForeground(1, notification);
        }
    }
}