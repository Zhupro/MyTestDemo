package com.test.zwy.mytestdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by FERO010 on 2016/6/12.
 */
public class MyService extends Service {

    private DownloadBinder downloadBinder = new DownloadBinder();


    //stopSelf();  在内部任何位置调用停止服务
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    @Override//服务创建时调用
    public void onCreate() {
        Log.d("MyService", "onCreate executed");
        super.onCreate();
    }

    @Override//每次服务启动时调用
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        //service是在主线程中运行，所以service处理一些耗时处理时要开启子线程处理，否则会ANR（Application Not Responding）
        new Thread(new Runnable() {
            @Override
            public void run() {
                //处理具体的逻辑

                stopSelf();//如果想要实现让⼀一个服务在执⾏行完毕后⾃自动停⽌的功能则调用
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override//服务销毁时调用
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

    public class DownloadBinder extends Binder {

        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }
}

