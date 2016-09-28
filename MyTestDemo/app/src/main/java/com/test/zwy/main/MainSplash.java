package com.test.zwy.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.test.zwy.mytestdemo.R;

/**
 * Created by FERO010 on 2016/9/28.
 */

public class MainSplash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        fullScreen();
        setContentView(R.layout.mainsplash);
        init();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(MainSplash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }



    /**
     * 设置全屏隐藏状态栏
     */
    private void fullScreen() {
        View decorView = getWindow().getDecorView();//获取到了当前界面的DecorView
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;//表示全屏的意思，也就是会将状态栏隐藏
        decorView.setSystemUiVisibility(option);//设置系统UI元素的可见性
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//将ActionBar也进行隐藏
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return true;
    }

    @Override
    public void finish() {
        //淡入淡出
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.finish();
    }
}
