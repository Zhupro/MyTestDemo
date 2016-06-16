package com.test.zwy.mytestdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class CCView extends View {
    final static int OnLT = 1;
    final static int OnLB = 2;
    final static int OnRT = 3;
    final static int OnRB = 4;
    int direction = OnLT;

    public void setDirection(int d) {
        direction = d;
    }

    public CCView(Context context) {
        super(context);
        init();
    }

    public CCView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CCView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (needBulid) {
            needBulid = false;
            rebulidInfo();
        }
        setMeasuredDimension((int) cw, (int) ch);
    }

    public void showInfo(JSONObject json) {

    }

    public interface OnClickItem {
        public void onClick(String title, JSONObject data);
    }

    OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
        callUpdate();
    }

    String price;

    public void setPrice(String price) {
        this.price = price;
        callUpdate();
    }

    //pin pai
    String brand;

    public void setBrand(String brand) {
        this.brand = brand;
        callUpdate();
    }

    //
    String thing;

    public void setThing(String thing) {
        this.thing = thing;
        callUpdate();
    }

    ArrayList<String> traits = new ArrayList<String>();

    public void setTraits(ArrayList<String> traits) {
        this.traits = (ArrayList<String>) traits.clone();
    }

    static class Node {
        JSONObject data;
        String title;
        boolean isFill;
    }

    ArrayList<Node> listNodes = new ArrayList<Node>();

    public void addTagText(JSONObject data, String title, boolean isFill) {
        Node node = new Node();
        node.data = data;
        node.title = title;
        node.isFill = isFill;
        listNodes.add(node);
        callUpdate();
    }

    public void clearTagText() {
        listNodes.clear();
        callUpdate();
    }

    int dp2px(float dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) (px + 0.5f);
    }

    float dp2px2(float dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    boolean hasText(String str) {
        return str != null && str.trim().length() > 0;
    }

    Bitmap bitmapText = null;
    Point pointText = new Point();
    static Bitmap bitmapCircle = null;
    static Bitmap bitmapCircle2 = null;
    Point pointCircle = new Point();
    static int cR = 0;
    static int cR1 = 0;
    static int cR2 = 0;
    static int cR3 = 0;
    int cw = 0;
    int ch = 0;
    Path path;

    //计算大小
    void rebulidInfo() {

        //画圆
        if (bitmapCircle == null) {
            cR = dp2px(17);
            cR1 = dp2px(5);
            cR2 = cR - dp2px(6);
            cR3 = cR - dp2px(3);
            bitmapCircle = Bitmap.createBitmap(cR * 2, cR * 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCircle);
            //
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Rect rect = new Rect(0, 0, cR, cR);
            RectF rectF = new RectF(rect);
            paint.setShadowLayer(dp2px(4), 0, 0, 0xffffffff);
            paint.setColor(0xbb222222);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(cR, cR, cR2, paint);
            //
            bitmapCircle2 = Bitmap.createBitmap(cR * 2, cR * 2, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmapCircle2);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(dp2px(2), 0, 0, 0xffbbbbbb);
            paint.setColor(0xff60b6b8);
            canvas.drawCircle(cR, cR, cR1, paint);
        }
        path = new Path();
        //文字
        //计算起始位置
        //间隔
        space = dp2px(3);
        path = new Path();
        px = 0;
        py = 0;
        path.moveTo(px, py);
        //
        textList.clear();
        //计算高度
        lastNotFill = true;
        isFirstNode = true;
        if (direction == OnLT || direction == OnRT) {
            addText(thing, false, null);
            addText(brand, false, null);
            addText(price, false, null);
            for (String str : traits) {
                addText(str, true, null);
            }
            for (Node node : listNodes) {
                addText(node.title, node.isFill, node.data);
            }
        } else {
            for (int i = traits.size() - 1; i >= 0; i--) {
                String str = traits.get(i);
                addText(str, true, null);
            }
            addText(price, false, null);
            addText(brand, false, null);
            addText(thing, false, null);
            for (int i = listNodes.size() - 1; i >= 0; i--) {
                Node node = listNodes.get(i);
                addText(node.title, node.isFill, node.data);
            }
        }
        //计算文字边界
        int strokeWidth = dp2px(1);
        RectF f = new RectF();
        path.computeBounds(f, true);
        cw = (int) (f.width() + cR) + strokeWidth;//边界计算不对
        ch = (int) (f.height() + cR) + strokeWidth;//边界计算不对
        //绘制文字
        bitmapText = Bitmap.createBitmap((int) f.width() + strokeWidth, (int) f.height() + strokeWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapText);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(0xffffffff);
        //paint.setStrokeCap(Paint.Cap.BUTT);
        //paint.setStrokeJoin(Paint.Join.BEVEL);
        ;
        switch (direction) {
            case OnLT: {
                pointCircle.set(0, 0);
                pointText.set(pointCircle.x + cR, pointCircle.y + cR);
                paint.setShadowLayer(dp2px(1), 1 * tanX, 1, 0x88000000);
                //canvas.translate(0,0);
            }
            break;
            case OnLB: {
                pointCircle.set(0, ch - cR * 2);
                pointText.set(pointCircle.x + cR, pointCircle.y + cR - bitmapText.getHeight());
                paint.setShadowLayer(dp2px(1), 1 * tanX, 1, 0x88000000);
                canvas.translate(0, bitmapText.getHeight());
            }
            break;
            case OnRT: {
                pointCircle.set(cw - cR * 2, 0);
                pointText.set(pointCircle.x + cR - bitmapText.getWidth(), pointCircle.y + cR);
                paint.setShadowLayer(dp2px(1), -1 * tanX, 1, 0x88000000);
                canvas.translate(bitmapText.getWidth(), 0);
            }
            break;
            case OnRB: {
                pointCircle.set(cw - cR * 2, ch - cR * 2);
                pointText.set(pointCircle.x + cR - bitmapText.getWidth(), pointCircle.y + cR - bitmapText.getHeight());
                paint.setShadowLayer(dp2px(1), -1 * tanX, 1, 0x88000000);
                canvas.translate(bitmapText.getWidth(), bitmapText.getHeight());
            }
            break;
        }
        canvas.drawPath(path, paint);

        //orx,ory 移动方向
        float tx = (OnLT == direction || OnLB == direction) ? 0 : bitmapText.getWidth();
        float ty = (OnLT == direction || OnRT == direction) ? 0 : bitmapText.getHeight();

        for (TextNode node : textList) {
            // 平移
            offsetRect(node.touchRect, tx + pointText.x, ty + pointText.y);
            canvas.drawText(node.title, node.x, node.y, node.paint);
        }

    }

    void offsetRect(RectF r, float dx, float dy) {
        float left = r.left;
        float right = r.right;
        float top = r.top;
        float bottom = r.bottom;
        if (left > right) {
            left = r.right;
            right = r.left;
        }
        if (top > bottom) {
            top = r.bottom;
            bottom = r.top;
        }
        r.left = left + dx;
        r.right = right + dx;
        r.top = top + dy;
        r.bottom = bottom + dy;

    }

    /**
     * 检查触摸点是否在文字上（或查看颜色是否大于0x00000000）
     *
     * @param x
     * @param y
     * @return
     */
    boolean isOnTextOrCircle(float x, float y) {
        // 在圆上
        if (pointCircle.x + cR >= x && pointCircle.x - cR <= x && pointCircle.y + cR >= y && pointCircle.y - cR <= y) {
            return true;
        }
        //在文字上
        if (getOnNode(x, y) != null) {
            return true;
        }
        return false;
    }

    TextNode getOnNode(float x, float y) {
        //在文字上
        for (TextNode node : textList) {
            if (node.touchRect.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    class TextNode {
        String title;
        Paint paint;
        float x;
        float y;
        RectF touchRect = new RectF();
        JSONObject data = null;
    }

    ArrayList<TextNode> textList = new ArrayList<TextNode>();

    //倾斜
    float tanX = 0.33f;

    Paint pFont = new Paint();

    float px = 0;
    float py = 0;
    boolean lastNotFill = true;
    boolean isFirstNode = true;
    float lastNodeHeight = 0;
    float space = 0;

    void addText(String text, Boolean isFill, JSONObject data) {
        if (!hasText(text)) {
            return;
        }
        float padding = 0;
        Rect rect = new Rect();
        TextNode node = new TextNode();
        textList.add(node);
        node.title = text;
        node.data = data;
        node.paint = new Paint();
        node.paint.setAntiAlias(true);//去除锯齿
        node.paint.setFilterBitmap(true);
        node.paint.setTextAlign(Paint.Align.CENTER);
        if (isFill) {
            padding = dp2px(6);
            node.paint.setTextSize(dp2px(20));
            node.paint.setColor(0xff222222);
        } else {
            padding = dp2px(10);
            node.paint.setTextSize(dp2px(26));
            node.paint.setColor(0xffeeeeee);
        }
        node.paint.getTextBounds(text, 0, text.length(), rect);
        float lineh = rect.height() + padding * 1.4f;
        float dx = lineh * tanX;
        float linew = rect.width() + padding * 2 + dx;

        //orx,ory 移动方向
        int orx = (OnLT == direction || OnLB == direction) ? 1 : -1;
        int ory = (OnLT == direction || OnRT == direction) ? 1 : -1;
        //
        if (isFirstNode && (isFill || ory < 0)) {
        //if (isFirstNode && isFill) {
            //第一个的小间隔
            float dh = cR3;
            px += dh * tanX * orx;
            py += dh * ory;
            path.lineTo(px, py);
            path.close();
            path.moveTo(px, py);
        }
        isFirstNode = false;
        //间隔
        if (isFill || !lastNotFill) {
            px += space * tanX * orx;
            py += space * ory;
            path.moveTo(px, py);
        }
        //
        Paint.FontMetricsInt fontMetrics = node.paint.getFontMetricsInt();
        node.x = px + (linew + dx) * orx / 2;
        node.y = py + lineh * ory / 2 + (-fontMetrics.bottom - fontMetrics.top) / 2;
        node.touchRect.set(px, py, px + (linew + dx) * orx, py + lineh * ory);
        //
        if (isFill) {
            //
            path.lineTo(px + dx * orx, py + lineh * ory);
            path.lineTo(px + linew * orx + dx * orx, py + lineh * ory);
            path.lineTo(px + linew * orx, py);
            path.close();
            lastNotFill = false;
        } else {

            //上一个是不填充的文字
            if (lastNotFill) {
                if (ory > 0) {
                    path.lineTo(px + dx * orx, py + lineh * ory);
                    path.lineTo(px + linew * orx + dx * orx, py + lineh * ory);
                    path.lineTo(px + dx * orx, py + lineh * ory);
                    path.close();
                } else {
                    //回去
                    path.moveTo(px - lastNodeHeight*tanX * orx, py - lastNodeHeight * ory);
                    //回来
                    path.lineTo(px, py);
                    path.lineTo(px + linew * orx + dx * orx, py);
                    path.lineTo(px, py);
                    path.close();
                }
            } else {
                if (ory > 0) {
                    //
                    path.lineTo(px + dx * orx, py + lineh * ory);
                    path.lineTo(px + linew * orx + dx * orx, py + lineh * ory);
                    path.lineTo(px + dx * orx, py + lineh * ory);
                    path.close();
                } else {
                    //
                    path.lineTo(px + +linew * orx + dx * orx, py);
                    path.close();
                }
            }
            lastNotFill = true;
        }
        px += lineh * tanX * orx;
        py += lineh * ory;
        path.moveTo(px, py);
        lastNodeHeight = lineh;

    }

    boolean needBulid = true;

    void callUpdate() {
        needBulid = true;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (needBulid) {
            needBulid = false;
            rebulidInfo();
        }
        //绘制原1
        canvas.drawBitmap(bitmapCircle, pointCircle.x, pointCircle.y, null);
        //绘制原动画
        drawAnimation(canvas);
        //绘制文字
        canvas.drawBitmap(bitmapText, pointText.x, pointText.y, null);
        //绘制原2
        canvas.drawBitmap(bitmapCircle2, pointCircle.x, pointCircle.y, null);

        //绘制焦点区域
        //drawFouch(canvas);

    }

    void drawFouch(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(0x88ff0000);
        for (TextNode node : textList) {
            // 平移
            canvas.drawRect(node.touchRect, paint);
        }
    }

    Paint aPaint = new Paint();

    {
        aPaint.setAntiAlias(true);
        aPaint.setStyle(Paint.Style.FILL);
        aPaint.setColor(0x22ffffff);
    }

    long lastDTime = System.currentTimeMillis();

    void drawAnimation(Canvas canvas) {

        long oneTime = 1500;
        float r = ((System.currentTimeMillis() - lastDTime) % oneTime / (float) oneTime) * (cR2 - cR1) + cR1;
        canvas.drawCircle(pointCircle.x + cR, pointCircle.y + cR, r, aPaint);
        postInvalidateDelayed(40);
    }

    float lasttx;
    float lastty;
    float formDoewX;
    float formDoewY;
    boolean hasMove = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().bringChildToFront(this);
            lasttx = event.getRawX();
            lastty = event.getRawY();
            formDoewX = getX();
            formDoewY = getY();
            hasMove = false;
            if (!isOnTextOrCircle(event.getX(), event.getY())) {
                return false;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            setX(event.getRawX() - lasttx + formDoewX);
            setY(event.getRawY() - lastty + formDoewY);
            if (getX() - formDoewX > dp2px(3) || getY() - formDoewY > dp2px(3)) {
                hasMove = true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP && !hasMove) {
            TextNode downNode = getOnNode(event.getX(), event.getY());
            if (downNode != null && mOnClickItem != null) {
                mOnClickItem.onClick(downNode.title, downNode.data);
            } else if (downNode == null) {

            }
        }
        return true;
    }

    void addToLayout(RelativeLayout mainlay, int x, int y) {
        rebulidInfo();

        int pw = (int) mainlay.getWidth();
        int ph = (int) mainlay.getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        this.setLayoutParams(params);
        mainlay.addView(this);
        //计算方向,位置
        if (x < pw / 2) {
            if (y < ph / 2) {
                this.setDirection(CCView.OnLT);
                setX(x);
                setY(y);
            } else {
                this.setDirection(CCView.OnLB);
                setX(x);
                setY(y - ch);
            }
        } else {
            if (y < ph / 2) {
                this.setDirection(CCView.OnRT);
                setX(x - cw);
                setY(y);
            } else {
                this.setDirection(CCView.OnRB);
                setX(x - cw);
                setY(y - ch);
            }
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (bitmapText != null) {
            bitmapText.recycle();
            bitmapText = null;
        }
    }
}
