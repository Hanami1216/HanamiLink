package com.hanamilink.data.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hanamilink.R;
import com.hanamilink.data.model.EqInfo;
import com.hanamilink.data.model.EqSeekBarBean;
import com.hanamilink.ui.widget.VerticalSeekBarView;
import com.hanamilink.ui.widget.eq.ValueUtil;
import com.hanamilink.util.AppUtil;


import java.util.ArrayList;
import java.util.List;


public final class EqSeekBarAdapter extends BaseQuickAdapter<EqSeekBarBean, BaseViewHolder> {
    private int selectMode = -1;
    private ValueChange mValueChange;
    private boolean mHasHoverView = false;
    private EqInfo mEqInfo = new EqInfo();

    private boolean ban;


    @SuppressLint("NotifyDataSetChanged")
    public void setBan(boolean ban) {
        this.ban = ban;
        notifyDataSetChanged();
    }

    public EqSeekBarAdapter(List<EqSeekBarBean> list, ValueChange valueChange) {
        super(R.layout.item_eq_seekbar, list);
        this.mValueChange = valueChange;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, EqSeekBarBean item) {
        if (!getData().isEmpty()) {
            int itemWidth = 0;
            ViewGroup relativeLayout = (ViewGroup) helper.getView(R.id.cl_main);
            ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
            //小于7段
            if (getData().size() < 7) {
                if (getRecyclerView().getWidth() != 0) {
                    itemWidth = getRecyclerView().getWidth() / getData().size();  // 根据RecyclerView宽度计算每个条目的宽度
                } else {
                    itemWidth = (AppUtil.getScreenWidth(getContext()) - ValueUtil.dp2px(getContext(), 60)) / getData().size();  // 使用屏幕宽度减去固定像素和根据数量计算每个条目的宽度
                }
            } else {
                //超过6段
                params.width = ValueUtil.dp2px(getContext(), 50);  // 如果大于等于7段，设置布局参数的宽度为50dp
            }
            //如果计算到的宽度是0，则忽略
            if (itemWidth != 0) {
                params.width = itemWidth;  // 设置布局参数的宽度为计算得到的宽度
                relativeLayout.setLayoutParams(params);  // 将计算得到的布局参数设置给RelativeLayout
            }
        }
        VerticalSeekBarView verticalSeekBarView = helper.getView(R.id.vsb_eq);
        verticalSeekBarView.setText(item.getFreq());  // 设置垂直SeekBar对应位置的频率文本
        verticalSeekBarView.setValue(item.getValue());  // 设置垂直SeekBar对应位置的值
        verticalSeekBarView.setIndex(item.getIndex());  // 设置垂直SeekBar的索引
        verticalSeekBarView.setEnable(!ban);  // 设置垂直SeekBar是否可用
        this.mEqInfo.getValue()[item.getIndex()] = (byte) item.getValue();  // 更新mEqInfo中对应位置的值

        // 设置垂直SeekBar数值变化监听器
        verticalSeekBarView.setValueListener((value, end) -> {
            if (getItemPosition(item) < 0) {
                return;  // 如果当前条目不在适配器中的位置小于0，则返回
            }
            item.setValue(value);  // 更新条目对应的值
            this.mEqInfo.getValue()[item.getIndex()] = (byte) item.getValue();  // 更新mEqInfo中对应位置的值
            mValueChange.onChange(item.getIndex(), this.mEqInfo, end);  // 调用值变化回调接口
        });

        // 设置垂直SeekBar悬停监听器
        verticalSeekBarView.setHoverListener(hover -> mHasHoverView = hover);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void select(int selectMode) {
        this.selectMode = selectMode;
        notifyDataSetChanged();
    }

    public boolean hasHoverView() {
        return mHasHoverView;
    }

    public void updateSeekBar(EqInfo eqInfo) {
        this.mEqInfo = eqInfo;
        List<EqSeekBarBean> barBeans = new ArrayList<>();
        for (int i = 0; i < eqInfo.getValue().length && i < eqInfo.getFreqs().length; i++) {
            EqSeekBarBean eqSeekBarBean = new EqSeekBarBean(i, AppUtil.freqValueToFreqShowText(eqInfo.getFreqs()[i]), eqInfo.getValue()[i]);
            barBeans.add(eqSeekBarBean);
        }
        setNewInstance(barBeans);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateSeekBar(int[] value) {
        //有拖动时不更新
        if (mHasHoverView) {
            return;
        }
        List<EqSeekBarBean> list = getData();
        //先检测是否有数据变化，有变化才更新。没有就不更新，防止recycleView view缓存导致的抖动问题
        int changeCount = 0;
        int changeIndex = -1;
        for (int i = 0; i < list.size() && i < value.length; i++) {
            if (value[i] != list.get(i).getValue()) {
                changeIndex = i;
                changeCount++;
            }
            list.get(i).setValue(value[i]);
        }
        if (changeCount > 1) {
            notifyDataSetChanged();
        } else if (changeIndex != -1) {
            notifyItemChanged(changeIndex);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void reset() {
        //List<EqSeekBarBean> list = getData();
        ////先检测是否有数据变化，有变化才更新。没有就不更新，防止recycleView view缓存导致的抖动问题
        //for (EqSeekBarBean barBean : list) {
        //    barBean.setValue(0);
        //}
        //notifyDataSetChanged();
    }

    public int[] getValues() {
        int[] values = new int[getData().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = getData().get(i).getValue();
        }
        return values;
    }


    public interface ValueChange {
        void onChange(int index, EqInfo eqInfo, boolean end);
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
