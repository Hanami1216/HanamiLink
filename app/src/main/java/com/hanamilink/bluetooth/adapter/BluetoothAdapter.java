package com.hanamilink.bluetooth.adapter;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanamilink.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {

    private List<BluetoothDevice> data;

    public BluetoothAdapter(List<BluetoothDevice> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_list, parent, false);
        return new BluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothViewHolder holder, int position) {
        BluetoothDevice item = data.get(position);
        holder.convert(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BluetoothViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvBondState;
        private ImageView ivDeviceType;

        public BluetoothViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvBondState = itemView.findViewById(R.id.tv_bond_state);
            ivDeviceType = itemView.findViewById(R.id.iv_device_type);
            itemView.setOnClickListener(v -> {
                // Handle item click event
            });
        }

        @SuppressLint("MissingPermission")
        public void convert(BluetoothDevice item) {
            if (item.getName() == null) {
                tvName.setText("无名");
            } else {
                tvName.setText(item.getName());
            }

            getDeviceType(item.getBluetoothClass().getMajorDeviceClass(), ivDeviceType);

            switch (item.getBondState()) {
                case BluetoothDevice.BOND_BONDED:
                    tvBondState.setText("已配对");
                    break;
                case BluetoothDevice.BOND_BONDING:
                    tvBondState.setText("正在配对...");
                    break;
                case BluetoothDevice.BOND_NONE:
                    tvBondState.setText("未配对");
                    break;
            }
        }

        private void getDeviceType(int type, ImageView imageView) {
            switch (type) {
                case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                case BluetoothClass.Device.Major.AUDIO_VIDEO:
                    imageView.setImageResource(R.mipmap.icon_headset);
                    break;
                case BluetoothClass.Device.Major.COMPUTER:
                    imageView.setImageResource(R.mipmap.icon_computer);
                    break;
                case BluetoothClass.Device.Major.PHONE:
                    imageView.setImageResource(R.mipmap.icon_phone);
                    break;
                case BluetoothClass.Device.Major.HEALTH:
                    imageView.setImageResource(R.mipmap.icon_health);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                    imageView.setImageResource(R.mipmap.icon_vcr);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                    imageView.setImageResource(R.mipmap.icon_car);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                    imageView.setImageResource(R.mipmap.icon_loudspeaker);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                    imageView.setImageResource(R.mipmap.icon_microphone);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                    imageView.setImageResource(R.mipmap.icon_printer);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                    imageView.setImageResource(R.mipmap.icon_top_box);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                    imageView.setImageResource(R.mipmap.icon_meeting);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                    imageView.setImageResource(R.mipmap.icon_tv);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                    imageView.setImageResource(R.mipmap.icon_game);
                    break;
                case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                    imageView.setImageResource(R.mipmap.icon_wearable_devices);
                    break;
                default:
                    imageView.setImageResource(R.mipmap.icon_bluetooth);
                    break;
            }
        }
    }
}

