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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.zwy.myapp.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by FERO010 on 2016/6/12.
 */
public class ThreadTest extends AppCompatActivity {
    TextView textThread;
    Button threadButton;
    private Button execute;
    private Button cancel;
    private ProgressBar progressBar;
    private TextView textView;
    MyTask mTask;

    private static final String TAG = "ASYNC_TASK";

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
        threadButton = (Button) findViewById(R.id.threadButton);
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

        execute = (Button) findViewById(R.id.execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意每次需new一个实例,新建的任务只能执行一次,否则会出现异常
                mTask = new MyTask();
                mTask.execute("http://www.baidu.com");

                execute.setEnabled(false);
                cancel.setEnabled(true);
            }
        });
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消一个正在执行的任务,onCancelled方法将会被调用
                mTask.cancel(true);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.text_view);

    }
    private class MyTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText("loading");
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            try{
                URL url = new URL("http://www.baidu.com");
                connection = (HttpURLConnection) url.openConnection();//获取HttpURLConnection的实例
                connection.setRequestMethod("GET");//设置http请求使用方法
                connection.setConnectTimeout(8000);//设置连接超时毫秒数
                connection.setReadTimeout(8000);//设置读取超时毫秒数

                InputStream inputStream = connection.getInputStream();//获取服务器反悔的输入流
                long total = connection.getContentLength();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int count = 0;
                int length = -1;
                while ((length = inputStream.read(buf))!=-1) {
                    baos.write(buf, 0, length);
                    count += length;
                    publishProgress((int) (count / (float) total) * 100);
                    Thread.sleep(500);
                }

                return  new String(baos.toByteArray(),"gb2312");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            textView.setText("loading..." + values[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setProgress(0);
            textView.setText(s);
            execute.setEnabled(true);
            cancel.setEnabled(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            textView.setText("cancle");
            progressBar.setProgress(0);
            execute.setEnabled(true);
            cancel.setEnabled(false);
        }
    }
}
