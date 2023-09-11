package com.hanamiLink.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class PrefUtil {

	private final static String TAG = PrefUtil.class.getSimpleName();

	private final String PREFERENCE_TAG = "music_ab";

	private Context mContext;

	private final static String PREFERENCE_TAG_PUBLIC = "music_ab_public";

	private static HashMap dataPublic = new HashMap();

	private static PrefUtil instance;

	private PrefUtil(){

	}

	public static PrefUtil getInstance()
	{
		if (instance == null) {
			instance = new PrefUtil();
		}
		return instance;
	}

	//
	public void init(Context ctx)
	{
		mContext = ctx;
		//changeSPLocation();
	}



	private SharedPreferences getPref()
	{
		if(mContext == null) {
			Log.w(TAG, "MyPreference context should not be null");
		}
		//initPathIfNotExist();
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_TAG, Context.MODE_MULTI_PROCESS);
		return preferences;
	}

	private Editor getPrefEditor()
	{
		if(mContext == null) {
			Log.w(TAG, "MyPreference context should not be null");
		}
		//initPathIfNotExist();
		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_TAG, Context.MODE_MULTI_PROCESS);
		return preferences.edit();
	}




	private SharedPreferences getPrefDefault()
	{
		if(mContext == null) {
			Log.w(TAG, "MyPreference Default context should not be null");
		}

		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_TAG, Context.MODE_MULTI_PROCESS);
		return preferences;
	}

	private Editor getPrefEditorDefault()
	{
		if(mContext == null) {
			Log.w(TAG, "MyPreference Default context should not be null");
		}

		SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_TAG, Context.MODE_MULTI_PROCESS);

		return preferences.edit();

	}

	public static HashMap getData(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
		String temp = sharedPreferences.getString(key, "");
		ByteArrayInputStream bais =  new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
		HashMap data = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			data = (HashMap) ois.readObject();
		} catch (IOException e) {
		}catch(ClassNotFoundException e1) {

		}
		return data;
	}

	/**
	 * @param data
	 */
	public static void saveData(Context context, String preferenceName,String key,HashMap data) throws Exception {
		if(data instanceof Serializable) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(data);//把对象写到流里
				String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
				editor.putString(key, temp);
				editor.commit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			throw new Exception("Data must implements Serializable");
		}
	}

//	/**
//	 * 是否是第一次使用，默认是true
//	 * @return
//	 */
//	public Boolean getFirstTimeWithVersionCode()
//	{
//		String versionCode = ActivityUtil.getVersionCode(mContext);
//		String versionCodeSaved = getPrefDefault().getString("versionCode",null);
//
//		//Boolean isFirstTime = getPrefDefault().getBoolean("isFirstTime",true);
//
//		if(versionCode != null && versionCodeSaved != null && versionCodeSaved.equals(versionCode))
//		{
//			return false;
//		}else
//		{
//			return true;
//		}
//	}
//
//	/**
//	 * 是否是第一次使用，默认是true
//	 * @return
//	 */
//	public boolean setFirstTimeWithVersionCode()
//	{
//
//		String versionCode = ActivityUtil.getVersionCode(mContext);
//
//		return getPrefEditorDefault().putString("versionCode", versionCode).commit();
//	}


	public static void putInt(Context context, String preferenceName,String key,int value)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String preferenceName,String key,int defValue)
	{
		SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
		int temp = sharedPreferences.getInt(key, defValue);
		return temp;
	}

	public static void savePublicDataValue(Context context, String key, int value)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TAG_PUBLIC, context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();

		dataPublic.put(key,value);
	}

	public static void savePublicDataValue(Context context, String key, String value)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TAG_PUBLIC, context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
		dataPublic.put(key,value);
	}

	public static void savePublicDataValue(Context context, String key, int[][] arr)
	{
		SharedPreferences prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0;i < arr.length;i++) {

			int[] arr1 = arr[i];
			JSONArray jsonArray1 = new JSONArray();
			for (int j = 0;j < arr1.length;j++) {
				jsonArray1.put(arr1[j]);
			}
			jsonArray.put(jsonArray1);
		}
		Editor editor = prefs.edit();
		editor.putString(key,jsonArray.toString());
		editor.commit();
	}


	public static void loadPublicData(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TAG_PUBLIC, context.MODE_PRIVATE);
		Map m = sharedPreferences.getAll();
		dataPublic.clear();
		dataPublic.putAll(m);
	}

	public static int getIntFromPublicData(String key,int defValue)
	{

		Integer o = (Integer)dataPublic.get(key);
		if(o == null)
		{
			return defValue;
		}
		return o.intValue();
	}

	public static String getStringFromPublicData(String key,String defValue)
	{

		String o = (String)dataPublic.get(key);
		if(o == null)
		{
			return defValue;
		}
		return o;
	}


}
