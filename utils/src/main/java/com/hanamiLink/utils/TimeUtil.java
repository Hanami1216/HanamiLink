package com.hanamiLink.utils;

public class TimeUtil {

    private final  static String TAG = TimeUtil.class.getSimpleName();

    public static String getTimeFromSecs(int secs)
    {
        int min = (int)Math.floor(secs / 60.0);
        int sec = secs % 60;
        int hour = min / 60;
        if(hour > 0)
        {
            min = min % 60;
            //Log.e(TAG,"secs:"+secs+ "  hour:"+hour+ "  min:"+min +"   sec:"+sec);
            return String.format("%02d:%02d:%02d",hour,min,sec);
        }else
        {
            //Log.e(TAG,"secs:"+secs+ "  min:"+min +"   sec:"+sec);
            return String.format("%02d:%02d",min,sec);
        }

    }

}
