package com.test.zwy.mytestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhuwenyu on 16/6/14.
 */
public class MainActivityTwo extends AppCompatActivity {
    RelativeLayout mainlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintwo);
        init();
    }

    void init() {
        mainlay = (RelativeLayout) findViewById(R.id.mainlay);
        mainlay.setBackgroundColor(0xff161616);
        mainlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        addCCView(mainlay, (int) event.getX(), (int) event.getY());
                        break;
                }
                return true;
            }
        });
    }

    Toast toast;

    void addCCView(RelativeLayout mainlay, int x, int y) {
        //创建
        CCView cc = new CCView(this);
        cc.setOnClickItem(new CCView.OnClickItem() {
            @Override
            public void onClick(String title, JSONObject data) {
                if (toast == null) {
                    toast = Toast.makeText(MainActivityTwo.this, title, Toast.LENGTH_SHORT);
                }
                toast.setText(title);
                toast.show();
            }
        });
        //
        ArrayList ts = new ArrayList<String>();
        //ts.add("不错");
        /*
        ts.add("很好");
        cc.setPrice("10");
        cc.setBrand("大米牌");
        cc.setThing("小米杀虫剂");
        cc.setTraits(ts);
        */
        //

        cc.addTagText(null, "小米杀虫剂", false);
        cc.addTagText(null, "￥10", false);
        cc.addTagText(null, "大米牌", false);
        cc.addTagText(null, "不错", true);
        cc.addTagText(null, "很好", true);


        cc.addToLayout(mainlay, x, y);

    }
}
