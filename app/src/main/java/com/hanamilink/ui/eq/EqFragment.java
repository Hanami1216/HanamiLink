package com.hanamilink.ui.eq;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hanamilink.data.adapter.EqSeekBarAdapter;
import com.hanamilink.data.model.EqInfo;
import com.hanamilink.databinding.FragmentEqBinding;
import com.hanamilink.ui.Hanami_BaseFragment;
import com.hanamilink.ui.widget.RotatingView;

import java.util.ArrayList;

public class EqFragment extends Hanami_BaseFragment implements RotatingView.OnValueChangeListener {



    private FragmentEqBinding binding;

    private EqSeekBarAdapter mEqSeekBarAdapter;

    private EqInfo mEqInfo;

    private static final boolean SEND_CMD_REALTIME = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding =  FragmentEqBinding.inflate(inflater, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mEqSeekBarAdapter = new EqSeekBarAdapter(new ArrayList<>(), (index, eqInfo, end) -> {

            });
        }
        binding.rvVsbs.setAdapter(mEqSeekBarAdapter);
        binding.rvVsbs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mEqInfo = new EqInfo(0, new byte[10]);
        mEqInfo.setFreqs(new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        mEqInfo.setValue(new byte[]{0,0,0,0,0,0,0,0,0,0});

        mEqSeekBarAdapter.updateSeekBar(mEqInfo);
        Log.d(TAG, "onCreateView: 创建eqinfo");
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void change(RotatingView view, int value, boolean end) {

    }
    /**
     * @param null:
     * @return null
     * @description 三个按钮的点击事件
     */
    private final View.OnClickListener mOnClickListener = v -> {
        //if (v == btnEqMode) {
        //    EqModeDialog eqModeDialog = EqModeDialog.newInstance(eqInfo -> setEqInfo(eqInfo));
        //    eqModeDialog.show(getChildFragmentManager(), EqModeDialog.class.getCanonicalName());
        //} else if (v == btnEqReset) {
        //    List<EqInfo> list = EqCacheUtil.getPresetEqInfo().getEqInfos();
        //    EqInfo eqInfo = list.get(0).copy();
        //    eqInfo.setMode(6);
        //    eqInfo.setValue(new byte[eqInfo.getValue().length]);
        //    setEqInfo(eqInfo);
        //} else if (v == btnEqAdvancedSet) {
        //    if (!mRCSPController.isDeviceConnected()) {
        //        ToastUtil.showToastShort(getString(R.string.first_connect_device));
        //        return;
        //    }
        //    CommonActivity.startCommonActivity(getActivity(), EqAdvancedSetFragment.class.getCanonicalName());
        //}
    };

    private void updateEqInfo(@NonNull EqInfo eqInfo) {
        Log.d(TAG, "set updateEqInfo -->" + eqInfo.toString());
        this.mEqInfo = eqInfo;
        // 频响曲线 模式设定
        //tvEqModeSelectName.setText(getResources().getStringArray(R.array.eq_mode_list)[mEqInfo.getMode()]);
        // 频响曲线数据设置
        //wvFreq.setData(ValueUtil.bytes2ints(eqInfo.getValue(), eqInfo.getValue().length));
        //wvFreq.setFreqs(eqInfo.getFreqs());
        mEqSeekBarAdapter.updateSeekBar(eqInfo.copy());

        //boolean isBan = mRCSPController.getDeviceInfo() != null && mRCSPController.getDeviceInfo().isBanEq();

        //boolean enableResetBtn = mEqInfo.getMode() == 6 && !isBan;
        //判断是否全部值都是0，如果是则禁止点击
        //if (enableResetBtn) {
        //    boolean allZero = true;
        //    for (int v : eqInfo.getValue()) {
        //        if (v != 0) {
        //            allZero = false;
        //            break;
        //        }
        //    }
        //    enableResetBtn = !allZero;
        //}
        //btnEqReset.setSelected(enableResetBtn);
        //btnEqReset.setClickable(enableResetBtn);
    }
}
