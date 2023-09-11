package com.hanamiLink.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

public class ServiceUtils {

    private static final String TAG = ServiceUtils.class.getSimpleName();
    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            String className = runningService.get(i).service.getClassName().toString();
            //Log.e(TAG,"className:"+className + "    ServiceName:"+ServiceName);
            if (className.equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}
