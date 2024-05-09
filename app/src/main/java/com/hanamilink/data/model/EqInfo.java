package com.hanamilink.data.model;

import java.util.Arrays;

public class EqInfo {
    // 均衡器模式
    private int mode;
    // 均衡器数值
    private byte[] value = new byte[10];
    // 频率信息
    private int[] freqs;
    // 是否是动态均衡
    private boolean dynamic;
    // 均衡器计数
    private int count;

    // 无参构造方法，初始化默认频率和计数
    public EqInfo() {
        //this.freqs = BluetoothConstant.DEFAULT_EQ_FREQS;
        this.count = 10;
    }

    // 接收模式和数值参数的构造方法
    public EqInfo(int mode, byte[] value) {
        //this.freqs = BluetoothConstant.DEFAULT_EQ_FREQS;
        this.count = 10;
        this.setMode(mode); // 设置模式
        this.setValue(value); // 设置数值
    }

    // 接收模式、数值和频率参数的构造方法
    public EqInfo(int mode, byte[] value, int[] freqs) {
        //this.freqs = BluetoothConstant.DEFAULT_EQ_FREQS;
        this.count = 10;
        this.setMode(mode); // 设置模式
        this.setValue(value); // 设置数值
        this.setFreqs(freqs); // 设置频率
    }

    // 设置动态均衡
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    // 获取动态均衡
    public boolean isDynamic() {
        return this.dynamic;
    }

    // 获取均衡器模式
    public int getMode() {
        return this.mode;
    }

    // 设置均衡器模式
    public void setMode(int mode) {
        this.mode = mode;
    }

    // 获取均衡器数值
    public byte[] getValue() {
        return this.value;
    }

    // 设置均衡器数值
    public void setValue(byte[] value) {
        this.value = value;
    }

    // 设置均衡器频率
    public void setFreqs(int[] freqs) {
        this.freqs = freqs;
        this.count = freqs.length;
    }

    // 获取均衡器频率
    public int[] getFreqs() {
        return this.freqs;
    }

    // 创建均衡器信息的副本
    public EqInfo copy() {
        EqInfo eqInfo = new EqInfo(this.mode, this.value, this.getFreqs()); // 创建新对象
        eqInfo.count = this.count; // 设置计数
        eqInfo.setDynamic(this.dynamic); // 设置动态均衡
        return eqInfo; // 返回新对象
    }

    // 获取均衡器计数
    public int getCount() {
        return this.count;
    }

    // 返回描述对象信息的字符串
    public String toString() {
        return "EqInfo{mode=" + this.mode + ", isNew=" + this.dynamic + ", value=" + Arrays.toString(this.value) + ", freqs=" + Arrays.toString(this.freqs) + '}';
    }
}
