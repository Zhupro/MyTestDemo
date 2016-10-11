package com.test.zwy.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.zwy.myapp.helpApi;
import com.test.zwy.mytestdemo.R;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FERO010 on 2016/9/29.
 */

public class GuidePage extends AppCompatActivity {

    private static final int[] imageId = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3};

    @Bind(R.id.guideViewPager)
    ViewPager guideViewPager;
    @Bind(R.id.guideButton)
    Button guideButton;
    @Bind(R.id.guide_ll_point)
    LinearLayout llPointGroup;//引导页圆点；
    @Bind(R.id.view_red_point)
    View viewRedPoint;//底部跟随图片而动的红色point；
    @Bind(R.id.guideSkip)
    TextView guideSkip;

    private ArrayList<ImageView> imageList;
    private int mPointWidth;                   //两点间的距离；


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        fullScreen();
        setContentView(R.layout.guidepage);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        imageList = new ArrayList<ImageView>();
        for (int i = 0; i < imageId.length; i++) {
            ImageView imageView = new ImageView(this);
            //imageView.setImageResource(imageId[i]);//设置引导页北京
            imageList.add(imageView);//讲背景添加到集合中
        }

        //初始化底部原点
        for (int i = 0; i < imageId.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_grey);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(helpApi.dip2px(10), helpApi.dip2px(10));

            if (i > 0) {
                params.leftMargin = helpApi.dip2px(10);//设置圆点间的距离
            }
            point.setLayoutParams(params);//设置圆点的大小
            llPointGroup.addView(point);//将圆点添加到布局中
        }
        guideViewPager.setAdapter(new GuideAdapter());
        guideViewPager.addOnPageChangeListener(new GuidePageListener());

        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApi.setFirstUseCode();
                Intent i = new Intent();
                i.setClass(GuidePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//              使用下行代码代替上行，但要改变版本号为14以上；
//              llPointGroup.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
//              获取两个
                mPointWidth = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
            }
        });

        guideSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApi.setFirstUseCode();
                Intent i = new Intent();
                i.setClass(GuidePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
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

    @OnClick({R.id.guideViewPager, R.id.guideButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guideViewPager:
                break;
            case R.id.guideButton:
                break;
        }
    }


    class GuideAdapter extends PagerAdapter {

        //获取页卡数量；
        @Override
        public int getCount() {
            return imageId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化界面；
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //避免因为图片大小导致内存溢出
            ImageView imageView = imageList.get(position);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            InputStream is = getResources().openRawResource(imageId[position]);
            Bitmap tmpbmp = BitmapFactory.decodeStream(is, null, opt);
            imageView.setImageBitmap(tmpbmp);
            ((ViewGroup) container).addView(imageView);
            return imageList.get(position);
        }

        //移除页卡；
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewGroup) container).removeView((View) object);
        }
    }

    class GuidePageListener implements ViewPager.OnPageChangeListener {

        //滑动事件；1、 当前位置（0,1,2）；2、偏移的百分比；3、偏移的距离；
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int lenth = (int) (mPointWidth * positionOffset + position * mPointWidth);
            //获取当前红点的布局参数，该view的父布局是RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
            params.leftMargin = lenth;//实时更新小红点与第一个灰点的间距
            viewRedPoint.setLayoutParams(params);//重新给小红点设置布局参数，效果是可以看到红点在两个灰点之间移动；
        }

        //被选中页面，当滑动到最后一个页面时才显示“开始体验”按钮；
        @Override
        public void onPageSelected(int position) {
            if (position == imageId.length - 1) {
                guideButton.setVisibility(View.VISIBLE);
            } else {
                guideButton.setVisibility(View.GONE);
            }
        }


        //滑动状态变化
        @Override
        public void onPageScrollStateChanged(int state) {


        }
    }

}
