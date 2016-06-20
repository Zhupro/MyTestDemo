package com.test.zwy.mytestdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by FERO010 on 2016/6/20.
 */
public class HttpPractice extends AppCompatActivity{
    private Button httpPracticeButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_practice);
        init();
    }

    private void init() {
        httpPracticeButton = (Button) findViewById(R.id.idHttpPtactice);
        httpPracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
