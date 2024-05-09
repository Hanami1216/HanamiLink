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

    public void changeFragment(int containerId, Fragment fragment, String fragmentTag) {
        if (fragment != null && this.isAdded() && !this.isDetached()) {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!TextUtils.isEmpty(fragmentTag)) {
                fragmentTransaction.replace(containerId, fragment, fragmentTag);
            } else {
                fragmentTransaction.replace(containerId, fragment);
            }

            fragmentTransaction.addToBackStack((String)null);
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    public void changeFragment(int containerId, Fragment fragment) {
        this.changeFragment(containerId, fragment, (String)null);
    }
}
