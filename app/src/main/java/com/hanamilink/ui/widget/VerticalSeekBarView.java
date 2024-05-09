package com.hanamilink.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.hanamilink.R;


public class VerticalSeekBarView extends View {

    // 用于绘制文本内容的画笔
    private TextPaint mTextPaint;
    private TextPaint mLabelTextPaint;

    // 用于绘制选中进度条的画笔和普通进度条的画笔
    private Paint mSelectedProgressPaint;
    private Paint mProgressPaint;

    // 用于显示在控件上的文本内容
    private String mText = "125";

    // 当前数值
    private int currentValue = 0;

    // 用于记录上一个触摸点的位置
    private PointF mLastPoint = new PointF();

    // 用于监听数值变化的监听器
    public ValueListener mValueListener;

    // 用于监听悬停状态变化的监听器
    public HoverListener mHoverListener;

    // 控件是否可用的标识变量
    private boolean enable = false;


    // 未选中状态下的滑块图像
    private Bitmap thumbImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_eq_btn_slider_nor);

    // 选中状态下的滑块图像
    private Bitmap thumbSelectedImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_eq_btn_slider_sel);

    // 标签的图像
    private Bitmap labelImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_eq_sb_labal_bg);


    // 控件是否处于活动状态
    private boolean mActive;

    // 控件的索引值
    private int index = 0;

    // 控件所能表示的最小值
    private int min = -8;

    // 控件所能表示的最大值
    private int max = 8;


    // 设置控件的索引值
    public void setIndex(int index) {
        this.index = index;
    }

    // 构造函数，用于在代码中创建控件实例
    public VerticalSeekBarView(Context context) {
        super(context);
        init(null, 0);
    }

    // 构造函数，用于在布局文件中使用控件时实例化控件
    public VerticalSeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    // 构造函数，用于在布局文件中使用控件时实例化控件，且应用自定义样式
    public VerticalSeekBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {
        // 初始化文本画笔
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(sp2px(12));  // 设置文本大小为 12sp
        mTextPaint.setColor(getContext().getResources().getColor(R.color.gray_8B8B8B));  // 设置文本颜色为灰色

        // 初始化标签文本画笔
        mLabelTextPaint = new TextPaint();
        mLabelTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setTextAlign(Paint.Align.CENTER);
        mLabelTextPaint.setTextSize(sp2px(14));  // 设置文本大小为 14sp
        mLabelTextPaint.setColor(Color.WHITE);  // 设置文本颜色为白色

        // 初始化选中进度条的画笔
        mSelectedProgressPaint = new Paint();
        mSelectedProgressPaint.setStrokeCap(Paint.Cap.ROUND);  // 设置画笔结束端为圆形
        mSelectedProgressPaint.setStrokeWidth(dp2px(3));  // 设置画笔宽度为 3dp
        setEnable(true);  // 设置为可用状态

        // 初始化普通进度条的画笔
        mProgressPaint = new Paint();
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);  // 设置画笔结束端为圆形
        mProgressPaint.setStrokeWidth(dp2px(3));  // 设置画笔宽度为 3dp
        mProgressPaint.setColor(getContext().getResources().getColor(R.color.gray_E5E5E5));  // 设置普通进度条的颜色为浅灰色
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentProgressY = mLastPoint.y;  // 获取当前进度的垂直位置
        float x = getWidth() / 2f;  // 获取控件中心的水平位置
        Bitmap bitmap = isActive() ? thumbSelectedImage : thumbImage;  // 根据控件是否处于活动状态选择不同的滑块图像
        canvas.drawLine(x, getProgressStartY(), x, getProgressEndY(), mProgressPaint);  // 绘制普通的进度条线
        // 处理部分手机startY和endY相同时起点无效并自动默认为原点（0,0）的问题
        if (currentProgressY != getProgressEndY()) {
            canvas.drawLine(x, currentProgressY, x, getProgressEndY(), mSelectedProgressPaint);  // 绘制选中的进度条线
        }
        float thumbY = currentProgressY - bitmap.getHeight() / 2f;  // 计算滑块的垂直位置
        float thumbX = (getWidth() - bitmap.getWidth()) / 2f;  // 计算滑块的水平位置
        canvas.drawBitmap(bitmap, thumbX, thumbY, null);  // 绘制滑块图像
        canvas.drawText(mText, x, getHeight() - sp2px(2), mTextPaint);  // 绘制文本内容
        // 如果控件处于活动状态
        if (isActive()) {
            x = (getWidth() - labelImage.getWidth()) / 2f;
            float y = thumbY - labelImage.getHeight() + bitmap.getHeight() / 4f;
            canvas.drawBitmap(labelImage, x, y, null);  // 绘制标签的图像
            x = getWidth() / 2f;
            y = y + labelImage.getHeight() / 1.65f;
            canvas.drawText(currentValue + "", x, y, mLabelTextPaint);  // 绘制当前数值
        }
    }



    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mActive = isThumbHover(event.getX(), event.getY());
        }
        if (isActive()) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.dispatchTouchEvent(event);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }


    }


    public void setEnable(boolean enable) {
        this.enable = enable;
        mSelectedProgressPaint.setColor(!enable ? getContext().getResources().getColor(R.color.gray_bbbbbb) :
                getContext().getResources().getColor(R.color.colorPrimary));
        invalidate();
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float div = getProgressHeight() / (max - min);
        float y = (max - currentValue) / (max - min * 1.0f) * getProgressHeight() + getProgressStartY();
        if (Math.abs(mLastPoint.y - y) > div) {
            mLastPoint.set(0, getAvailableY(y));
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enable) {
            mActive = false;
            return super.onTouchEvent(event);
        }
        float y = getAvailableY(event.getY());
        boolean end = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mHoverListener != null) {
                    mHoverListener.onChange(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                y = mLastPoint.y;
            case MotionEvent.ACTION_UP:
                end = true;
                if (mHoverListener != null) {
                    mHoverListener.onChange(false);
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                mActive = false;
                break;
        }
        invalidate();
        int value = calculateValue(y);
        mLastPoint.set(event.getX(), y);
        if ((MotionEvent.ACTION_DOWN != event.getAction() && value != currentValue) || end) {
            currentValue = value;
            if (mValueListener != null) {
                mValueListener.onChange(currentValue, end);
            }
        }
        return isActive();
    }


    public void setValue(int value) {
        //去设置抖动问题
        if (isActive()) {
            //滑动状态或者值相等则忽略设置值
            return;
        }

        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        float y = (max - value) / (1.0f * max - min) * getProgressHeight() + getProgressStartY();
        mLastPoint.set(0, getAvailableY(y));
        currentValue = value;
        invalidate();
    }


    public void setValueListener(ValueListener valueListener) {
        this.mValueListener = valueListener;
    }

    public void setHoverListener(HoverListener hoverListener) {
        this.mHoverListener = hoverListener;
    }

    public interface ValueListener {
        void onChange(int value, boolean end);
    }


    public interface HoverListener {
        void onChange(boolean hover);
    }


    private boolean isThumbHover(float x, float y) {

        float currentY = mLastPoint.y;
        float bmpHalfHeight = thumbImage.getHeight() / 2f;
        return y < currentY + bmpHalfHeight && y > currentY - bmpHalfHeight;
    }


    private int calculateValue(float y) {
        float value = max - (y - getProgressStartY()) / getProgressHeight() * (max - min);
        return (int) value;
    }

    private boolean isActive() {
        return mActive;
    }

    private float getProgressHeight() {
        return getHeight() - thumbImage.getHeight() / 1.1f - getProgressStartY();
    }

    private float getProgressEndY() {
        return getProgressStartY() + getProgressHeight();
    }


    private float getProgressStartY() {
        return thumbImage.getHeight() * 1.15f;
    }

    private float getAvailableY(float y) {
        if (y < getProgressStartY()) {
            return getProgressStartY();
        } else if (y > getProgressEndY()) {
            return getProgressEndY();
        }
        return y;
    }


    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }

}
