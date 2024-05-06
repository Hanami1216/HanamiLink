package com.hanamilink.activity.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hanamilink.databinding.FragmentNotificationsBinding;


public class BleManagerFragment extends Fragment {

    private FragmentNotificationsBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 创建 BleManagerViewModel 实例来处理 BLE 设备通信逻辑
        BleManagerViewModel bleManagerViewModel =
                new ViewModelProvider(this).get(BleManagerViewModel.class);

        // 使用 Data Binding 将 Fragment 的布局文件与视图关联起来
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        // 获取与 FragmentNotificationsBinding 关联的根视图
        View root = binding.getRoot();

        // 从 FragmentNotificationsBinding 获取名为 textNotifications 的 TextView
        final TextView textView = binding.textNotifications;

        // 观察 BleManagerViewModel 中的文本数据变化，并更新到 textNotifications TextView
        bleManagerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 返回根视图作为 Fragment 的视图
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
