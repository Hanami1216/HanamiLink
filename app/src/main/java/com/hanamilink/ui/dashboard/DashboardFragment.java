package com.hanamilink.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanamilink.data.adapter.EqSeekBarAdapter;
import com.hanamilink.data.model.EqInfo;
import com.hanamilink.data.model.EqSeekBarBean;
import com.hanamilink.databinding.FragmentEqBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentEqBinding binding;

    private EqSeekBarAdapter mEqSeekBarAdapter;

    private static final boolean SEND_CMD_REALTIME = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        // 假设有一个名为 dataList 的 List<EqSeekBarBean> 数据列表
        binding =  FragmentEqBinding.inflate(inflater, container, false);
        mEqSeekBarAdapter = new EqSeekBarAdapter(getEqSeekBarData(), (index, eqInfo, end) -> {

        });
        binding.rvVsbs.setAdapter(mEqSeekBarAdapter);
        binding.rvVsbs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        return binding.getRoot();
    }
    // 创建均衡器数据列表
    private List<EqSeekBarBean> getEqSeekBarData() {
        List<EqSeekBarBean> dataList = new ArrayList<>();
        // 这里可以根据需要添加均衡器数据
        for (int i = 0; i < 10; i++) { // 示例添加10个均衡器条目
            EqSeekBarBean item = new EqSeekBarBean(i, "Freq" + i, 0); // 设置频率和初始数值
            dataList.add(item);
        }
        return dataList;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
