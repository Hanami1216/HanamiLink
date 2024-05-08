package com.hanamilink.activity.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 自定义的RecyclerView，用于在特定条件下控制包含它的ViewPager2的滑动行为,
 * 根据用户的手势操作，在特定条件下禁止或允许所在的 ViewPager2 对象的滑动处理
 */
public class ViewPager2RecycleView extends RecyclerView {

    private float mStartX;  // 记录触摸事件的起始X坐标
    private MotionEvent mDownEvent = null;  // 记录按下事件的MotionEvent对象

    public ViewPager2RecycleView(@NonNull Context context) {
        super(context);
    }

    public ViewPager2RecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPager2RecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写dispatchTouchEvent方法，用于在特定条件下控制ViewPager2的滑动行为
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN) {  // 当按下时
            mDownEvent = e;  // 记录按下事件的MotionEvent对象
            mStartX = e.getX();  // 记录触摸事件的起始X坐标
            setViewPager2InputEnable(false); // 禁止ViewPager2的滑动处理
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {  // 当抬起或取消时
            setViewPager2InputEnable(true);  // 允许ViewPager2的滑动处理
            mDownEvent = null;  // 清空按下事件的MotionEvent对象
        } else if (action == MotionEvent.ACTION_MOVE) {  // 当移动时
            // 判断第一次移动是否需要禁止ViewPager2的滑动
            if (mDownEvent != null) {
                float x = e.getX();
                float div = x - mStartX;
                boolean canScrollRight = canScrollHorizontally(1);  // 是否可以水平向右滚动
                boolean canScrollLeft = canScrollHorizontally(-1);  // 是否可以水平向左滚动
                if (div < 0) {  // 如果手指向左移动
                    setViewPager2InputEnable(!canScrollRight);  // 根据内容是否能够向右滚动来设置是否允许ViewPager2的滑动处理
                } else {  // 如果手指向右移动
                    setViewPager2InputEnable(!canScrollLeft);  // 根据内容是否能够向左滚动来设置是否允许ViewPager2的滑动处理
                }
                mDownEvent = null;  // 清空按下事件的MotionEvent对象
            }

        }
        return super.dispatchTouchEvent(e);
    }

    /**
     * 设置是否允许ViewPager2的用户输入
     */
    private void setViewPager2InputEnable(boolean enable) {
        ViewPager2 viewPager2 = getViewPager2();
        if (viewPager2 != null) {
            viewPager2.setUserInputEnabled(enable);  // 设置ViewPager2的用户输入状态
        }
    }

    /**
     * 查找包含当前RecyclerView的父级视图中是否包含ViewPager2对象
     */
    private ViewPager2 getViewPager2() {
        ViewParent parent = this;
        while ((parent = parent.getParent()) != null) {
            if (parent instanceof ViewPager2) {  // 如果父级视图是ViewPager2
                return (ViewPager2) parent;  // 返回这个ViewPager2对象
            }
        }
        return null;

    }
}

