package com.hanamiLink.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/*
 * 蓝牙SharedPreferences工具类
 * @description
 * SharedPreferences是Android平台上的一个轻量级存储机制，用于存储和检索应用程序的简单键值对数据。
 * 它提供了一种持久化存储的方式，可以在应用程序关闭后仍然保持数据的可用性。
 */
public class BlePrefUtil {
    private static final String TAG = BlePrefUtil.class.getSimpleName();
    private final String PREFERENCE_TAG = "ble_zhicase";
    private Context mContext;
    private static BlePrefUtil instance;


    private BlePrefUtil() {
    }

    public static BlePrefUtil getInstance() {
        if (instance == null) {
            instance = new BlePrefUtil();
        }

        return instance;
    }

    public void init(Context ctx) {
        this.mContext = ctx;
    }

    public void initDefault(Context ctx) {
        this.mContext = ctx;
    }

    private SharedPreferences getPref() {
        if (this.mContext == null) {
            Log.w(TAG, "MyPreference context should not be null");
        }

        SharedPreferences preferences = this.mContext.getSharedPreferences("ble_zhicase", Context.MODE_MULTI_PROCESS);
        return preferences;
    }

    private SharedPreferences.Editor getPrefEditor() {
        if (this.mContext == null) {
            Log.w(TAG, "MyPreference context should not be null");
        }

        SharedPreferences preferences = this.mContext.getSharedPreferences("ble_zhicase", Context.MODE_MULTI_PROCESS);
        return preferences.edit();
    }

    private SharedPreferences getPrefDefault() {
        if (this.mContext == null) {
            Log.w(TAG, "MyPreference Default context should not be null");
        }

        SharedPreferences preferences = this.mContext.getSharedPreferences("ble_zhicase", Context.MODE_MULTI_PROCESS);
        return preferences;
    }

    private SharedPreferences.Editor getPrefEditorDefault() {
        if (this.mContext == null) {
            Log.w(TAG, "MyPreference Default context should not be null");
        }

        SharedPreferences preferences = this.mContext.getSharedPreferences("ble_zhicase", Context.MODE_MULTI_PROCESS);
        return preferences.edit();
    }

    public static HashMap getData(Context context, String preferenceName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
        String temp = sharedPreferences.getString(key, "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), 0));
        HashMap data = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            data = (HashMap)ois.readObject();
        } catch (IOException | ClassNotFoundException var8) {
        }

        return data;
    }


    public static void saveData(Context context, String preferenceName, String key, HashMap data) throws Exception {
        if (data instanceof Serializable) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(data);
                String temp = new String(Base64.encode(baos.toByteArray(), 0));
                editor.putString(key, temp);
                editor.commit();
            } catch (IOException var9) {
                var9.printStackTrace();
            }

        } else {
            throw new Exception("Data must implements Serializable");
        }
    }
}
