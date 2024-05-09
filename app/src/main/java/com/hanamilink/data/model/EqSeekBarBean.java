package com.hanamilink.data.model;


// EqSeekBarBean 类
public class EqSeekBarBean {
    // 频率
    private String freq;
    // 索引
    private int index;
    // 数值
    private int value;

    // 无参构造函数
    public EqSeekBarBean() {
    }

    // 带参构造函数，用来初始化对象时设置频率、索引和数值
    public EqSeekBarBean(int index, String freq, int value) {
        this.freq = freq;
        this.value = value;
        this.index = index;
    }

    // 获取频率的方法
    public String getFreq() {
        return freq;
    }

    // 设置频率的方法
    public void setFreq(String freq) {
        this.freq = freq;
    }

    // 获取数值的方法
    public int getValue() {
        return value;
    }

    // 设置数值的方法
    public void setValue(int value) {
        this.value = value;
    }

    // 设置索引的方法
    public void setIndex(int index) {
        this.index = index;
    }

    // 获取索引的方法
    public int getIndex() {
        return index;
    }
}
