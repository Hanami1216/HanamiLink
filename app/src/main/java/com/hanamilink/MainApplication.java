package com.hanamilink;

import android.app.Application;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    private static MainApplication sApplication;

    public static MainApplication getApplication() {
        return sApplication;
    }
}
