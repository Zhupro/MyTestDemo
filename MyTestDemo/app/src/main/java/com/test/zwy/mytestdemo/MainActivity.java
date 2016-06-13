package com.test.zwy.mytestdemo;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.nineoldandroids.view.ViewHelper;
import com.test.zwy.myapp.MyApplication;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    Button textview;
    GestureDetector mGestureDetector;
    View myView;
    Button threadTest;
    Button startService;
    Button stopService;
    Button bindService;
    Button unbindService;
    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        //以下两个方法 在活动与服务成功绑定以及解除绑定的时候调⽤用。
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("onServiceDisconnected", "onServiceDisconnected");
        }
    };
    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    private int mCount = 0;
    private Handler mHander = new Handler() {

        public void handerMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        textview.scrollTo(scrollX, 0);
                        mHander.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(false);
        mGestureDetector.setOnDoubleTapListener(this);
        init();
    }

    private void init() {

        myView = (View) findViewById(R.id.myView);
        textview = (Button) findViewById(R.id.textview);
        threadTest = (Button) findViewById(R.id.threadTest);
        startService = (Button) findViewById(R.id.startSerVice);
        stopService = (Button) findViewById(R.id.stopService);
        bindService = (Button) findViewById(R.id.bindService);
        unbindService = (Button) findViewById(R.id.unbindService);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取left，reight，top，buttom
//                int left = textview.getLeft();
//                int reight = textview.getRight();
//                int top = textview.getTop();
//                int buttom = textview.getBottom();

//                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) myView.getLayoutParams();
//                params.height += 100;
//                params.leftMargin += 100;
//                myView.requestLayout();
//                ObjectAnimator.ofFloat(textview, "translationX", 0, 400).setDuration(1000).start();
                //toAnimator();
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

        threadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThreadTest.class);
                startActivity(intent);
            }
        });
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);//启动服务
            }
        });
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                stopService(intent);//停止服务
            }
        });
        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bindIntent = new Intent(MainActivity.this, MyService.class);
                bindService(bindIntent, connection, BIND_ABOVE_CLIENT);//绑定服务 BIND_AUTO_CREATE表⽰示在活动和服务进⾏行绑定后⾃自动创建服务
            }
        });
        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);//解绑服务
            }
        });
    }

    /**
     * 弹性滑动
     */
    private void toAnimator() {

        final int startX = 0;
        final int deltaX = 200;
        ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                textview.scrollTo(startX + (int) (deltaX * fraction), 0);
            }
        });
        animator.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mLastX = 0;
        int mLastY = 0;
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d("TAG", "move,deltaX:" + deltaX + " deltaY:" + deltaY);
                int translationX = (int) (ViewHelper.getTranslationX(getWindow().getDecorView()) + deltaX);
                int translationY = (int) (ViewHelper.getTranslationY(getWindow().getDecorView()) + deltaY);

                ViewHelper.setTranslationX(getWindow().getDecorView(), translationX);
                ViewHelper.setTranslationY(getWindow().getDecorView(), translationY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;

        return true;
        //GestureDetector专用
        // this.mGestureDetector.onTouchEvent(event);
//        //速度追踪
//        VelocityTracker velocityTracker = VelocityTracker.obtain();
//        velocityTracker.addMovement(event);
//
//        velocityTracker.computeCurrentVelocity(1000);//每秒
//        int xVelocityTracker = (int) velocityTracker.getXVelocity();
//        int yVelocityTracker = (int) velocityTracker.getYVelocity();
//
//        Log.d("xVelocityTracker", String.valueOf(xVelocityTracker));
//        Log.d("yVelocityTracker", String.valueOf(yVelocityTracker));
//
//        velocityTracker.clear();
//        velocityTracker.recycle();

        //return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("!!onDown", "onDown: " + e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("!!onShowPress", "onShowPress: " + e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("!!onSingleTapUp", "onSingleTapUp: " + e.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("!!onScroll", "onScroll: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("!!onLongPress", "onLongPress: " + e.toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("!!onFling", "onFling: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }
}
