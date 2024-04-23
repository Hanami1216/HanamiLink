package com.hanamiLink.eventbus;

import android.os.Bundle;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;


public class EBUtil {


	public static void postEmptyMsg(int msgwhat){
		Message msg = new Message();
		msg.what = msgwhat;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithObject(int msgwhat,Object obj){
        Message msg = new Message();
		msg.what = msgwhat;
		msg.obj = obj;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithA1Object(int msgwhat,int arg1,Object obj){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		msg.obj = obj;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithAObject(int msgwhat,int arg1,int arg2,Object obj){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.obj = obj;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithA1(int msgwhat,int arg1){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithA1A2(int msgwhat,int arg1,int arg2){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithBundle(int msgwhat,Bundle bundle){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.setData(bundle);
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithA1Bundle(int msgwhat,int arg1,Bundle bundle){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		msg.setData(bundle);
		EventBus.getDefault().post(msg);
	}

	public static void postMsgWithABundle(int msgwhat,int arg1,int arg2,Bundle bundle){
		Message msg = new Message();
		msg.what = msgwhat;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		msg.setData(bundle);
		EventBus.getDefault().post(msg);
	}


}
