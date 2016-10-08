package com.test.zwy.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.test.zwy.myapp.helpApi;
import com.test.zwy.mytestdemo.R;

/**
 * Created by FERO010 on 2016/9/29.
 */

public class GuidePage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        fullScreen();
        setContentView(R.layout.guidepage);
        init();
    }

    private void init() {

        helpApi.setFirstUseCode();
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

}
