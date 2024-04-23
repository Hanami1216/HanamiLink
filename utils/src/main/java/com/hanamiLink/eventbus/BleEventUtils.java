package com.hanamiLink.eventbus;


import android.os.Bundle;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;


public class BleEventUtils {

    /**
     * 发送空消息
     *
     * @param msgType 消息类型
     */
    public static void postEmptyMsg(int msgType) {
        Message msg = new Message();
        msg.what = msgType;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有对象的消息
     *
     * @param msgType 消息类型
     * @param obj     消息对象
     */
    public static void postMsgWithObject(int msgType, Object obj) {
        Message msg = new Message();
        msg.what = msgType;
        msg.obj = obj;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有一个整型参数和对象的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     * @param obj     消息对象
     */
    public static void postMsgWithA1Object(int msgType, int arg1, Object obj) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        msg.obj = obj;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有两个整型参数和对象的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     * @param arg2    参数2
     * @param obj     消息对象
     */
    public static void postMsgWithAObject(int msgType, int arg1, int arg2, Object obj) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有一个整型参数的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     */
    public static void postMsgWithA1(int msgType, int arg1) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有两个整型参数的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     * @param arg2    参数2
     */
    public static void postMsgWithA1A2(int msgType, int arg1, int arg2) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有Bundle参数的消息
     *
     * @param msgType 消息类型
     * @param bundle  Bundle参数
     */
    public static void postMsgWithBundle(int msgType, Bundle bundle) {
        Message msg = new Message();
        msg.what = msgType;
        msg.setData(bundle);
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有一个整型参数和Bundle参数的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     * @param bundle  Bundle参数
     */
    public static void postMsgWithA1Bundle(int msgType, int arg1, Bundle bundle) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        msg.setData(bundle);
        EventBus.getDefault().post(msg);
    }

    /**
     * 发送带有两个整型参数和Bundle参数的消息
     *
     * @param msgType 消息类型
     * @param arg1    参数1
     * @param arg2    参数2
     * @param bundle  Bundle参数
     */
    public static void postMsgWithABundle(int msgType, int arg1, int arg2, Bundle bundle) {
        Message msg = new Message();
        msg.what = msgType;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.setData(bundle);
        EventBus.getDefault().post(msg);
    }
}

