package com.hanamilink.data.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class EqBarAdapter extends BaseQuickAdapter<EqSeekBarBean, BaseViewHolder> {
    private int selectMode = -1;  // 选择模式
    private final ValueChange mValueChange;  // 值变化监听器
    private boolean mHasHoverView = false;  // 是否有悬停视图
    private EqInfo mEqInfo = new EqInfo();  // 均衡器信息
    private boolean ban;  // 禁用标志

    public EqBarAdapter(int layoutResId, ValueChange mValueChange) {
        super(layoutResId);
        this.mValueChange = mValueChange;
    }

    public EqBarAdapter(int layoutResId, @Nullable List<EqSeekBarBean> data, ValueChange mValueChange) {
        super(layoutResId, data);
        this.mValueChange = mValueChange;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, EqSeekBarBean eqSeekBarBean) {
        if (!getData().isEmpty()) {
            // 计算宽度
            int itemWidth = calculateItemWidth(getContext(), getData().size(), getRecyclerView().getWidth());
            ViewGroup relativeLayout = baseViewHolder.getView(R.id.cl_main);
            ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();

            if (itemWidth != 0) {
                params.width = itemWidth;
                relativeLayout.setLayoutParams(params);
            }
        }

        VerticalSeekBarView verticalSeekBarView = baseViewHolder.getView(R.id.vsb_eq);
        verticalSeekBarView.setText(eqSeekBarBean.getFreq());  // 设置频率文本
        verticalSeekBarView.setValue(eqSeekBarBean.getValue());  // 设置SeekBar值
        verticalSeekBarView.setIndex(eqSeekBarBean.getIndex());  // 设置索引
        verticalSeekBarView.setEnable(!ban);  // 设置是否启用
        this.mEqInfo.getValue()[eqSeekBarBean.getIndex()] = (byte) eqSeekBarBean.getValue();  // 更新均衡器信息

        verticalSeekBarView.setValueListener((value, end) -> {
            if (getItemPosition(eqSeekBarBean) < 0) {
                return;
            }
            eqSeekBarBean.setValue(value);  // 更新数据项的值
            this.mEqInfo.getValue()[eqSeekBarBean.getIndex()] = (byte) eqSeekBarBean.getValue();  // 更新均衡器信息
            mValueChange.onChange(eqSeekBarBean.getIndex(), this.mEqInfo, end);  // 触发值变化监听器
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
     * 值变化监听接口。
     */
    public interface ValueChange {
        void onChange(int index, EqInfo eqInfo, boolean end);
    }

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
}
