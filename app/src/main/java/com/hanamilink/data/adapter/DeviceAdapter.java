package com.hanamilink.data.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hanamiLink.ble.BleDevice;
import com.hanamiLink.utils.ToastUtil;
import com.hanamilink.R;

import java.util.List;


public class DeviceAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder>{
    public DeviceAdapter(int layoutResId, @Nullable List<BleDevice> data) {
        super(layoutResId,data);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void changeBondDevice(){
        notifyDataSetChanged();
    }

    /**
     * 根据类型设置图标
     * @param type 类型码
     * @param imageView 图标
     */
    private void getDeviceType(int type, ImageView imageView) {
        switch (type) {
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES://耳机
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET://穿戴式耳机
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE://蓝牙耳机
            case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                imageView.setImageResource(R.mipmap.icon_headset);
                break;
            case BluetoothClass.Device.Major.COMPUTER://电脑
                imageView.setImageResource(R.mipmap.icon_computer);
                break;
            case BluetoothClass.Device.Major.PHONE://手机
                imageView.setImageResource(R.mipmap.icon_phone);
                break;
            case BluetoothClass.Device.Major.HEALTH://健康类设备
                imageView.setImageResource(R.mipmap.icon_health);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER://照相机录像机
            case BluetoothClass.Device.AUDIO_VIDEO_VCR://录像机
                imageView.setImageResource(R.mipmap.icon_vcr);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO://车载设备
                imageView.setImageResource(R.mipmap.icon_car);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER://扬声器
                imageView.setImageResource(R.mipmap.icon_loudspeaker);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE://麦克风
                imageView.setImageResource(R.mipmap.icon_microphone);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO://打印机
                imageView.setImageResource(R.mipmap.icon_printer);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX://音频视频机顶盒
                imageView.setImageResource(R.mipmap.icon_top_box);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING://音频视频视频会议
                imageView.setImageResource(R.mipmap.icon_meeting);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER://显示器和扬声器
                imageView.setImageResource(R.mipmap.icon_tv);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY://游戏
                imageView.setImageResource(R.mipmap.icon_game);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR://可穿戴设备
                imageView.setImageResource(R.mipmap.icon_wearable_devices);
                break;
            default://其它
                imageView.setImageResource(R.mipmap.icon_bluetooth);
                break;
        }
    }

    @Override
    protected void setOnItemChildClick(@NonNull View v, int position) {
        super.setOnItemChildClick(v, position);
        ToastUtil.toast(this.getContext(),"点击");
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BleDevice item) {
        if (item.getNameString() == null) {
            helper.setText(R.id.tv_name, "无名");
        } else {
            helper.setText(R.id.tv_name, item.getNameString());
        }
        ImageView imageView = helper.getView(R.id.iv_device_type);
            // 出现空指针异常
            //getDeviceType(item.getDevice().getBluetoothClass().getMajorDeviceClass(), imageView);



        //蓝牙设备绑定状态判断
        switch (item.getStatus()) {
            case connected:
                helper.setText(R.id.tv_bond_state, "已配对");
                break;
            case connecting:
                helper.setText(R.id.tv_bond_state, "正在配对...");
                break;
            case disconnect:
                helper.setText(R.id.tv_bond_state, "未配对");
                break;
        }
    }
}
