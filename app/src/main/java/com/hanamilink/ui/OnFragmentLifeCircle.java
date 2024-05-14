package com.hanamilink.ui;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public abstract class OnFragmentLifeCircle {

    // 当 Activity 调用 onActivityCreated 生命周期回调时执行的方法
    public void onActivityCreated(Fragment fragment, Activity activity) {
    }

    // 当 Fragment 生命周期进入 onDestroyView 状态时执行的方法
    public void onDestroyView(Fragment fragment, Activity activity) {
    }

    // 当 Fragment 生命周期进入显示状态时执行的方法
    public void onShow(Fragment fragment, Activity activity) {
    }

    // 当 Fragment 生命周期进入隐藏状态时执行的方法
    public void onHidden(Fragment fragment, Activity activity) {
    }
}
