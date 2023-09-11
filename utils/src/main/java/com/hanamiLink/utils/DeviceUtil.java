package com.hanamiLink.utils;

import android.content.Context;
import android.provider.Settings;
;

/**
 * Created by lanyanbo on 2018/6/30.
 */

public class DeviceUtil {

    /**
     * ANDROID_ID似乎是获取Device ID的一个好选择，
     * 但它也有缺陷：在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c 。
     * 同时刷机，或者重置ANDROID_ID的值都会变化。
     * @param ctx
     * @return
     */
    public static String getDeviceId(Context ctx)
    {
        String ANDROID_ID = Settings.System.getString(ctx.getContentResolver(), Settings.System.ANDROID_ID);

        return ANDROID_ID;
    }


//    /**
//     * 当某个时候Ble设备连接时生成的密码，
//     * @return
//     */
//    public static byte[] getTempDeviceIdForSave()
//    {
//        int len = 6;
//        //byte[] data = new byte[len];
//
//        long t = System.currentTimeMillis();
//        String tHex = Long.toHexString(t);
//
//        byte[] data = Utils.hexStringToByte(tHex);
//
//        return data;
//    }

}
