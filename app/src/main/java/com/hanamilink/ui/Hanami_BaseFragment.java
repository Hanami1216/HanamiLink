package com.hanamilink.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Hanami_BaseFragment extends Fragment {
    // 声明一个基础Fragment类
    protected String TAG = this.getClass().getSimpleName();
    private Bundle bundle;
    private OnFragmentLifeCircle mOnFragmentLifeCircle;

    // 基础Fragment类的构造函数
    public Hanami_BaseFragment() {
    }

    // 当视图创建时调用，初始化Fragment的一些操作
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mOnFragmentLifeCircle != null) {
            this.mOnFragmentLifeCircle.onActivityCreated(this, this.getActivity());
        }
    }

    // 当视图销毁时调用，执行一些清理操作
    public void onDestroyView() {
        if (this.mOnFragmentLifeCircle != null) {
            this.mOnFragmentLifeCircle.onDestroyView(this, this.getActivity());
        }

        super.onDestroyView();
    }

    // 当Fragment显示状态发生改变时调用
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (this.mOnFragmentLifeCircle != null) {
            if (hidden) {
                this.mOnFragmentLifeCircle.onHidden(this, this.getActivity());
            } else {
                this.mOnFragmentLifeCircle.onShow(this, this.getActivity());
            }
        }
    }

    // 设置Fragment生命周期回调接口
    public void setOnFragmentLifeCircle(OnFragmentLifeCircle lifeCircle) {
        this.mOnFragmentLifeCircle = lifeCircle;
    }

    // 获取bundle数据
    public Bundle getBundle() {
        return this.bundle;
    }

    // 设置bundle数据
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }


    /**
     * 在给定的容器中切换 Fragment
     *
     * @param containerId   用于容纳新 Fragment 的布局容器的 ID
     * @param fragment      要切换到的新 Fragment
     * @param fragmentTag   将要分配给新 Fragment 的标记，可以为null
     */
    public void changeFragment(int containerId, Fragment fragment, String fragmentTag) {
        // 确保 fragment 不为 null，当前 Fragment 已经添加到 activity 中，且当前 Fragment 未被分离
        if (fragment != null && this.isAdded() && !this.isDetached()) {
            // 获取当前 Fragment 的 ChildFragmentManager，并使用它启动一个新的 FragmentTransaction
            FragmentManager fragmentManager = this.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // 根据是否存在 fragmentTag，使用 FragmentTransaction 的 replace 方法来替换容器中的 Fragment
            if (!TextUtils.isEmpty(fragmentTag)) {
                fragmentTransaction.replace(containerId, fragment, fragmentTag);
            } else {
                fragmentTransaction.replace(containerId, fragment);
            }

            // 将事务添加到后退栈
            fragmentTransaction.addToBackStack(null);
            // 提交事务进行切换，并允许状态丢失
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
