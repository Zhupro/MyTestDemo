package com.test.zwy.mytestdemo;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by FERO010 on 2016/6/13.
 * 可以简单地创建一个异步的、会⾃自动停止的服务
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");// 调⽤用⽗父类的有参构造函数
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 打印当前线程的id
        Log.d("MyIntentService", "Thread id is" + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentService", "onDestroy executed");
    }
}
