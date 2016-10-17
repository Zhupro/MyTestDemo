package com.test.zwy.mytestdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.zwy.myapp.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FERO010 on 10/17/2016.
 * http://m.2cto.com/kf/201605/509332.html     blog地址
 */

public class ChangeColorImage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.seekBar1)
    SeekBar seekBarRed;
    @Bind(R.id.seekBar2)
    SeekBar seekBarGreen;
    @Bind(R.id.seekBar3)
    SeekBar seekBarBlue;
    @Bind(R.id.seekBar4)
    SeekBar seekBarAlpha;
    @Bind(R.id.changeImage)
    ImageView changeImage;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.textView4)
    TextView textView4;

    private Bitmap copyBitmap;
    private Bitmap baseBitmap;
    private Canvas canvas;
    private Paint paint;
    private int msg = 0;
    private float[] colorArray;//颜色矩阵
    private float count;//符合矩阵中每个元素的取值

    private float redValue, greenValue, blueValue, alphaValue;//这些值都是通过SeekBar移动过程中而得到的变化的值（msg 等于 1.颜色值2.偏移量值3.色调，饱和度，亮度）

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changecolor);
        ButterKnife.bind(this);
        init();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    textView1.setText("调整红色值");
                    textView1.setText("调整绿色值");
                    textView1.setText("调整蓝色值");
                    textView1.setText("调整透明度");
                    initBitMap();
                    break;
                case 1:
                    textView1.setText("调整红色值");
                    textView1.setText("调整绿色值");
                    textView1.setText("调整蓝色值");
                    textView1.setText("调整透明度");
                    initBitMap();
                    break;
                case 2:
                    textView1.setText("调整色调");
                    textView1.setText("调整饱和度");
                    textView1.setText("调整亮度");
                    initBitMap();
                    break;
            }
        }
    };

    private void init() {
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);
        seekBarAlpha.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg == 0) {
                    msg = 1;
                    Toast.makeText(MyApplication.getContext(), "msg -> 1 ,此时调整偏移量", Toast.LENGTH_SHORT).show();
                } else if (msg == 1) {
                    msg = 2;
                    Toast.makeText(MyApplication.getContext(), "msg -> 2 ,此时调整色调", Toast.LENGTH_SHORT).show();
                } else {
                    msg = 0;
                    Toast.makeText(MyApplication.getContext(), "msg -> 0 ,此时调整色值", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {

            if (msg == 0) {
                count = seekBar.getProgress() / 50f;//因为使得拖动条的取值为0f-2f，符合矩阵中每个元素的取值
            } else if (msg == 1) {
                count = seekBar.getProgress();//因为使得拖动条的取值为0f-100f，符合矩阵中元素的偏移量取值
            } else {
                count = seekBar.getProgress() / 50f;//因为使得拖动条的取值为0f-2f，符合矩阵中元素的偏移量取值
            }
            switch (seekBar.getId()) {
                case R.id.seekBar1:
                    this.redValue = count;
                    break;
                case R.id.seekBar2:
                    this.greenValue = count;
                    break;
                case R.id.seekBar3:
                    this.blueValue = count;
                    break;
                case R.id.seekBar4:
                    this.alphaValue = count;
                    break;
                default:
                    break;
            }
            updateUi(msg);

        }
    }

    private void updateUi(final int msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(msg);

            }
        }).start();
    }

    private void initBitMap() {
        //先加载出一张原图(baseBitmap)，然后复制出来新的图片(copyBitmap)来，因为andorid不允许对原图进行修改.
        baseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hhh);
        //既然是复制一张与原图一模一样的图片那么这张复制图片的画纸的宽度和高度以及分辨率都要与原图一样,copyBitmap就为一张与原图相同尺寸分辨率的空白画纸
        copyBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), baseBitmap.getConfig());
        canvas = new Canvas(copyBitmap);//将画纸固定在画布上
        paint = new Paint();//实例画笔对象
        ColorMatrixColorFilter colorFilter;
        if (msg == 0) {
            colorArray = new float[]{
                    redValue, 0, 0, 0, 0,
                    0, greenValue, 0, 0, 0,
                    0, 0, blueValue, 0, 0,
                    0, 0, 0, alphaValue, 0
            };
            ColorMatrix colorMatrix = new ColorMatrix(colorArray);//将保存的颜色矩阵的数组作为参数传入
            colorFilter = new ColorMatrixColorFilter(colorMatrix);//再把该colorMatrix作为参数传入来实例化ColorMatrixColorFilter
        } else if (msg == 1) {
            colorArray = new float[]{
                    1, 0, 0, 0, redValue,
                    0, 1, 0, 0, greenValue,
                    0, 0, 1, 0, blueValue,
                    0, 0, 0, 1, alphaValue};
            ColorMatrix colorMatrix = new ColorMatrix(colorArray);//将保存的颜色矩阵的数组作为参数传入
            colorFilter = new ColorMatrixColorFilter(colorMatrix);//再把该colorMatrix作为参数传入来实例化ColorMatrixColorFilter
        } else {

            ColorMatrix mColorMatrix = new ColorMatrix(); //设置色调
            mColorMatrix.setRotate(0, redValue);//redValue表示色调变化值
            mColorMatrix.setRotate(1, redValue);
            mColorMatrix.setRotate(2, redValue);
            //设置饱和度
            ColorMatrix mBaoheMatrix = new ColorMatrix();
            mBaoheMatrix.setSaturation(greenValue);//greenValue表示饱和度变化值
            //设置亮度：colorMatrix.setScale(rScale, gScale, bScale, aScale)//第一个参数表示:红色第二个表示绿色，第三个表示蓝色，第四个表示透明度
            // 当三原色如果是以相同的比例混合的话，就会显示出白色。系统也就是根据这些原理来修改一个图像的亮度的。当亮度为０，图像就变黑了。
            // 所以他们比例一样
            ColorMatrix mLightMatrix = new ColorMatrix();
            mLightMatrix.setScale(blueValue, blueValue, blueValue, 1);//blueValue表示亮度变化值
            //再创建组合的ColorMatrix对象将上面三种ColorMatrix的效果混合在一起
            ColorMatrix mImageViewMatrix = new ColorMatrix();
            mImageViewMatrix.postConcat(mColorMatrix);
            mImageViewMatrix.postConcat(mBaoheMatrix);
            mImageViewMatrix.postConcat(mLightMatrix);

            colorFilter = new ColorMatrixColorFilter(mImageViewMatrix);//再把该mImageViewMatrix作为参数传入来实例化ColorMatrixColorFilter
        }


        paint.setColorFilter(colorFilter);//并把该过滤器设置给画笔
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);//传如baseBitmap表示按照原图样式开始绘制，将得到是复制后的图片
        changeImage.setImageBitmap(copyBitmap);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
