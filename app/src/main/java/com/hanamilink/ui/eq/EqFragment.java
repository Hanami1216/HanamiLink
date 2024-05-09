package com.hanamilink.ui.eq;

import static com.hanamilink.util.AppUtil.getContext;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hanamilink.R;
import com.hanamilink.data.adapter.EqSeekBarAdapter;
import com.hanamilink.data.model.EqInfo;
import com.hanamilink.ui.Hanami_BaseFragment;
import com.hanamilink.ui.widget.EqWaveView;
import com.hanamilink.ui.widget.RotatingView;

import java.util.ArrayList;
import java.util.List;


public class EqFragment extends Hanami_BaseFragment implements RotatingView.OnValueChangeListener {
    private static final boolean SEND_CMD_REALTIME = false;
    private RotatingView rotatBass;
    private RotatingView rotatMain;
    private RotatingView rotatHigh;
    //private EqWaveView wvFreq;
    private TextView tvEqModeSelectName;
    private Button btnEqReset;
    private Button btnEqAdvancedSet;
    private Button btnEqMode;
    private EqSeekBarAdapter mEqSeekBarAdapter;



    private final int MSG_NO_SUPPORT_HIGH_AND_BASS = -13;

    private EqInfo mEqInfo ;
    private long sendVolTime;//记录音量命令发送的最后时间


    public static EqFragment newInstance() {
        return new EqFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eq, container, false);
        rotatBass = view.findViewById(R.id.rotat_bass);
        rotatMain = view.findViewById(R.id.rotat_main);
        rotatHigh = view.findViewById(R.id.rotat_height);
        rotatMain.setOnValueChangeListener(this);
        rotatBass.setOnValueChangeListener(this);
        rotatHigh.setOnValueChangeListener(this);

        tvEqModeSelectName = view.findViewById(R.id.tv_eq_mode_select_name);
        btnEqReset = view.findViewById(R.id.btn_eq_reset);
        btnEqAdvancedSet = view.findViewById(R.id.btn_eq_advanced_setting);
        btnEqMode = view.findViewById(R.id.btn_eq_mode);

        RecyclerView rvVsbs = view.findViewById(R.id.rv_vsbs);
        mEqSeekBarAdapter = new EqSeekBarAdapter(new ArrayList<>(), (index, eqInfo, end) -> {
            if (end || SEND_CMD_REALTIME) {
                eqInfo.setMode(6);

            }
        });
        rvVsbs.setAdapter(mEqSeekBarAdapter);
        rvVsbs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rotatMain.setValue(0, 25, 0);

        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        filter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");//监听手机输出设备变化

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }






    //更新手机音量
    private void updateVolumeUiFromPhone() {
        AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }







    private void changeRotateViewStyle(RotatingView view, boolean isOpen) {
        if (isOpen) {
            view.setContentStartColor(R.color.color_rotating_view_start);
            view.setContentEndColor(R.color.color_rotating_view_end);
            view.setContentTextColor(R.color.black_242424);
            view.setIndicatorImage(R.drawable.ic_rotatview_indicator_sup);
            view.setClickable(true);
        } else {
            view.setContentStartColor(R.color.gray_CECECE);
            view.setContentEndColor(R.color.gray_CECECE);
            view.setContentTextColor(R.color.gray_CECECE);
            view.setIndicatorImage(R.drawable.ic_rotatview_indicator_nol);
            view.setClickable(false);
        }
        view.invalidate();
    }


    private void disableEqViewIfBan(boolean ban) {
        changeRotateViewStyle(rotatHigh, !ban);
        changeRotateViewStyle(rotatBass, !ban);
        changeRotateViewStyle(rotatMain, !ban);
        btnEqMode.setClickable(!ban);
        btnEqReset.setClickable(!ban && mEqInfo.getMode() == 6);
        btnEqAdvancedSet.setClickable(!ban);

        tvEqModeSelectName.setTextColor(getResources().getColor(ban ? R.color.gray_959595 : R.color.black_242424));
        tvEqModeSelectName.setCompoundDrawablesWithIntrinsicBounds(0, 0, ban ? R.drawable.ic_eq_icon_up_disable : R.drawable.ic_eq_icon_up, 0);
        btnEqAdvancedSet.setTextColor(getResources().getColor(ban ? R.color.gray_959595 : R.color.black_242424));
        btnEqReset.setSelected(!ban);
        mEqSeekBarAdapter.setBan(ban);

    }

    private void resetHighAndBassRotateView() {
        rotatHigh.setValue(MSG_NO_SUPPORT_HIGH_AND_BASS);
        rotatBass.setValue(MSG_NO_SUPPORT_HIGH_AND_BASS);
        changeRotateViewStyle(rotatHigh, false);
        changeRotateViewStyle(rotatBass, false);
    }



    @Override
    public void change(RotatingView view, int value, boolean end) {

    }
}
