package com.hanamilink.ui;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public abstract class OnFragmentLifeCircle {

    public void onActivityCreated(Fragment fragment, Activity activity) {
    }

    public void onDestroyView(Fragment fragment, Activity activity) {
    }

    public void onShow(Fragment fragment, Activity activity) {
    }

    public void onHidden(Fragment fragment, Activity activity) {
    }
}
