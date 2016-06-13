package com.test.zwy.mytestdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.zwy.myapp.MyApplication;

/**
 * Created by FERO010 on 2016/6/12.
 */
public class ThreadTest extends AppCompatActivity {
    TextView textThread;
    Button threadButton;
    TextView asyncTaskButton;

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    private int mCount = 0;
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:

                    textThread.setText("Change");

                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threadtest);
        init();
    }

    private void init() {
        textThread = (TextView) findViewById(R.id.textThread);
        threadButton = (Button) findViewById(R.id.threadButton);
        asyncTaskButton = (Button) findViewById(R.id.asyncTaskButton);
        //new DownLoadTask().execute();//启动任务
        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = MESSAGE_SCROLL_TO;
                        mHander.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}

class DownLoadTask extends AsyncTask<Void, Integer, Boolean> {
    Boolean result;
    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();//显示进度对话框
    }


    @Override//处理耗时任务，在子线程中运行
    protected Boolean doInBackground(Void... params) {
        try {
            while (true) {
                int downloadPercent = doDownload();//虚构方法
                publishProgress(downloadPercent);
                if (downloadPercent >= 100) {
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //在这里更新下载进度（UI操作）
        progressDialog.setMessage("Downloaded " + values[0] + "%");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.dismiss();//关闭进度对话框（任务收尾工作）
        //在这里提示下载结果
        if (result) {
            Toast.makeText(MyApplication.getContext(), "Download succeeded",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "Download failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private int doDownload() {

        return 0;
    }
}
