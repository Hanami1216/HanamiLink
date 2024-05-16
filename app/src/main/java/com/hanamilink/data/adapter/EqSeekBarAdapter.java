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

/**
 * 自定义适配器类，用于管理和显示均衡器SeekBar的数据。
 */
public final class EqSeekBarAdapter extends BaseQuickAdapter<EqSeekBarBean, BaseViewHolder> {
    private int selectMode = -1;  // 选择模式
    private final ValueChange mValueChange;  // 值变化监听器
    private boolean mHasHoverView = false;  // 是否有悬停视图
    private EqInfo mEqInfo = new EqInfo();  // 均衡器信息
    private boolean ban;  // 禁用标志

    /**
     * 构造函数，初始化适配器。
     *
     * @param list 数据列表
     * @param valueChange 值变化监听器
     */
    public EqSeekBarAdapter(List<EqSeekBarBean> list, ValueChange valueChange) {
        super(R.layout.item_eq_seekbar, list);
        this.mValueChange = valueChange;
    }

    /**
     * 设置禁用标志，并刷新所有条目。
     *
     * @param ban 禁用标志
     */
    public void setBan(boolean ban) {
        this.ban = ban;
        notifyItemRangeChanged(0, getItemCount());  // 刷新所有条目而不是整个数据集(提高性能)
    }

    /**
     * 绑定数据到视图。
     *
     * @param helper 视图持有者
     * @param item 数据项
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, EqSeekBarBean item) {
        if (!getData().isEmpty()) {
            int itemWidth = calculateItemWidth(getContext(), getData().size(), getRecyclerView().getWidth());
            ViewGroup relativeLayout = (ViewGroup) helper.getView(R.id.cl_main);
            ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();

            if (itemWidth != 0) {
                params.width = itemWidth;
                relativeLayout.setLayoutParams(params);
            }
        }

        VerticalSeekBarView verticalSeekBarView = helper.getView(R.id.vsb_eq);
        verticalSeekBarView.setText(item.getFreq());  // 设置频率文本
        verticalSeekBarView.setValue(item.getValue());  // 设置SeekBar值
        verticalSeekBarView.setIndex(item.getIndex());  // 设置索引
        verticalSeekBarView.setEnable(!ban);  // 设置是否启用
        this.mEqInfo.getValue()[item.getIndex()] = (byte) item.getValue();  // 更新均衡器信息

        verticalSeekBarView.setValueListener((value, end) -> {
            if (getItemPosition(item) < 0) {
                return;
            }
            item.setValue(value);  // 更新数据项的值
            this.mEqInfo.getValue()[item.getIndex()] = (byte) item.getValue();  // 更新均衡器信息
            mValueChange.onChange(item.getIndex(), this.mEqInfo, end);  // 触发值变化监听器
        });

        verticalSeekBarView.setHoverListener(hover -> mHasHoverView = hover);  // 设置悬停监听器
    }

    /**
     * 计算每个条目的宽度。
     *
     * @param context 上下文
     * @param itemCount 条目数
     * @param recyclerViewWidth RecyclerView宽度
     * @return 每个条目的宽度
     */
    private int calculateItemWidth(Context context, int itemCount, int recyclerViewWidth) {
        int itemWidth = 0;
        if (itemCount < 7) {
            if (recyclerViewWidth != 0) {
                itemWidth = recyclerViewWidth / itemCount;
            } else {
                itemWidth = (AppUtil.getScreenWidth(context) - ValueUtil.dp2px(context, 60)) / itemCount;
            }
        } else {
            itemWidth = ValueUtil.dp2px(context, 50);
        }
        return itemWidth;
    }

    /**
     * 设置选择模式，并刷新数据。
     *
     * @param selectMode 选择模式
     */
    @SuppressLint("NotifyDataSetChanged")
    public void select(int selectMode) {
        this.selectMode = selectMode;
        notifyDataSetChanged();
    }

    /**
     * 判断是否有悬停视图。
     *
     * @return 是否有悬停视图
     */
    public boolean hasHoverView() {
        return mHasHoverView;
    }

    /**
     * 更新均衡器的SeekBar数据。
     *
     * @param eqInfo 均衡器信息对象
     */
    public void updateSeekBar(@NonNull EqInfo eqInfo) {
        // 将传入的均衡器信息对象赋值给类的成员变量mEqInfo
        this.mEqInfo = eqInfo;

        // 创建一个新的列表用于存储转换后的均衡器SeekBar数据
        List<EqSeekBarBean> barBeans = new ArrayList<>();

        // 遍历均衡器信息对象中的值数组和频率数组
        for (int i = 0; i < eqInfo.getValue().length && i < eqInfo.getFreqs().length; i++) {
            // 根据当前索引创建一个EqSeekBarBean对象，并添加到barBeans列表中
            EqSeekBarBean eqSeekBarBean = new EqSeekBarBean(i, AppUtil.freqValueToFreqShowText(eqInfo.getFreqs()[i]), eqInfo.getValue()[i]);
            barBeans.add(eqSeekBarBean);
        }

        // 将新创建的均衡器SeekBar数据列表设置为适配器的数据源
        setNewInstance(barBeans);
    }


    /**
     * 更新SeekBar的值。
     *
     * @param value 新的值数组
     */
    public void updateSeekBar(int[] value) {
        if (mHasHoverView) {
            return;
        }
        List<EqSeekBarBean> list = getData();
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

    /**
     * 重置所有SeekBar的值。
     */
    @SuppressLint("NotifyDataSetChanged")
    public void reset() {
        List<EqSeekBarBean> list = getData();
        for (EqSeekBarBean barBean : list) {
            barBean.setValue(0);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取所有SeekBar的值。
     *
     * @return 值数组
     */
    public int[] getValues() {
        int[] values = new int[getData().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = getData().get(i).getValue();
        }
        return values;
    }

    /**
     * 值变化监听接口。
     */
    public interface ValueChange {
        void onChange(int index, EqInfo eqInfo, boolean end);
    }

    /**
     * 获取屏幕宽度。
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
