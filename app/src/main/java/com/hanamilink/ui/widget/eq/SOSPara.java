package com.hanamilink.ui.widget.eq;


import androidx.annotation.Keep;

@Keep
public class SOSPara {
    private float m_defaultFreq; // 存储默认频率
    public int type; // 滤波器类型
    public float centerFrequency; // 中心频率
    public float qValue; // Q 值
    public float gain; // 增益
    public boolean enable = true; // 是否启用，默认为true

    // 以指定频率为参数的构造函数
    public SOSPara(float freq) {
        this.m_defaultFreq = freq; // 初始化默认频率
        this.centerFrequency = freq; // 初始化中心频率
    }

    // 无参数的构造函数，将默认频率初始化为 0.0
    public SOSPara() {
        this.m_defaultFreq = 0.0F;
    }

    // 重置参数
    public void reset() {
        this.type = 2; // 类型设为2
        this.centerFrequency = this.m_defaultFreq; // 中心频率重置为默认频率
        this.gain = 0.0F; // 增益设为 0.0
        this.qValue = 1.5F; // Q 值设为 1.5
        this.enable = true; // 启用状态设为 true
    }

    // 复制另一个 SOSPara 对象的参数值
    public void copy(SOSPara rhs) {
        this.type = rhs.type; // 复制类型
        this.centerFrequency = rhs.centerFrequency; // 复制中心频率
        this.gain = rhs.gain; // 复制增益
        this.qValue = rhs.qValue; // 复制 Q 值
        this.enable = rhs.enable; // 复制启用状态
    }
}

