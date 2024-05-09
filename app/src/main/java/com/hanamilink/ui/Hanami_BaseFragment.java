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
    protected String TAG = this.getClass().getSimpleName();
    private Bundle bundle;
    private OnFragmentLifeCircle mOnFragmentLifeCircle;

    public Hanami_BaseFragment() {
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mOnFragmentLifeCircle != null) {
            this.mOnFragmentLifeCircle.onActivityCreated(this, this.getActivity());
        }

    }

    public void onDestroyView() {
        if (this.mOnFragmentLifeCircle != null) {
            this.mOnFragmentLifeCircle.onDestroyView(this, this.getActivity());
        }

        super.onDestroyView();
    }

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

    public void setOnFragmentLifeCircle(OnFragmentLifeCircle lifeCircle) {
        this.mOnFragmentLifeCircle = lifeCircle;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

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
