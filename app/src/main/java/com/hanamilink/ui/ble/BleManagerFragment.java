package com.hanamilink.ui.ble;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hanamilink.databinding.FragmentBleManagerBinding;


public class BleManagerFragment extends Fragment {

    private FragmentBleManagerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 创建 BleManagerViewModel 实例来处理 BLE 设备通信逻辑
        BleManagerViewModel bleManagerViewModel =
                new ViewModelProvider(this).get(BleManagerViewModel.class);

        // 使用 Data Binding 将 Fragment 的布局文件与视图关联起来
        binding = FragmentBleManagerBinding.inflate(inflater, container, false);
        // 获取与 FragmentNotificationsBinding 关联的根视图

        // 返回根视图作为 Fragment 的视图
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
