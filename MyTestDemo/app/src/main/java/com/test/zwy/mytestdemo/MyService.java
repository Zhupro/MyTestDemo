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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override//服务销毁时调用
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

    class DownloadBinder extends Binder {

        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }
}

