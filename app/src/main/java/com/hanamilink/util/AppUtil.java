package com.hanamilink.util;

import android.content.Context;

import com.hanamilink.MainApplication;

public class AppUtil {
    private static String tag = "AppUtil";

    public static Context getContext() {
        return MainApplication.getApplication();
    }

    public static int getScreenWidth(Context context) {
        if (context == null) return 0;
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String freqValueToFreqShowText(int value) {
        int qian = value / 1000;
        if (qian > 0) {
            return qian + "K";
        } else {
            return value + "";
        }

    }
}
