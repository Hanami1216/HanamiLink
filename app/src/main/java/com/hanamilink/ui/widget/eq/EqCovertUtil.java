package com.hanamilink.ui.widget.eq;

public class EqCovertUtil {
    int w, h; // 定义 w 和 h 两个整型变量，用来表示宽度和高度
    float divX; // 表示 x 轴的刻度单位
    float divY; // 表示 y 轴的刻度单位
    float startY; // 表示起始 Y 坐标
    float startX; // 表示起始 X 坐标

    float startFreq = 20; // 表示起始频率，默认为 20 Hz
    float endFreq = 22000; // 表示结束频率，默认为 22000 Hz
    float padding = 0; // 内边距，默认为 0

    public void setStartAndEndFreq(float startFreq, float endFreq) {
        this.startFreq = Math.max(startFreq, 20); // 设置起始频率为传入值和 20 之间的较大值
        this.endFreq = Math.min(endFreq, 22000); // 设置结束频率为传入值和 22000 之间的较小值
        initData(); // 调用 initData 方法进行数据初始化
    }

    private void initData() {
        // 计算 x 轴的总长度
        float xL = (float) (Math.log10(endFreq) - Math.log10(startFreq));
        divX = (w - 2 * padding) / xL; // 计算出两个频率间的 x 轴刻度宽度
        startX = (float) (-Math.log10(startFreq) * divX) + padding; // 计算起始 X 坐标

        divY = (h / -24f); // 计算两个数据点之间的 y 轴刻度高度
        startY = -12 * divY; // 根据范围从 -20 到 20 计算起始 Y 坐标
    }

    public EqCovertUtil(int w, int h) {
        this.w = w; // 初始化宽度
        this.h = h; // 初始化高度
        initData(); // 调用 initData 方法进行数据初始化
    }

    public float px2sx(float x) {
        return (float) (Math.log10(x) * divX) + startX; // 将频率值转换为 x 轴上的坐标
    }

    public float py2sy(float y) {
        return y * divY + startY; // 将增益值转换为 y 轴上的坐标
    }

    public float[] pPoint2SPoint(float[] data) {
        float max = 0; // 初始化最大值
        float min = 0; // 初始化最小值
        float[] result = new float[data.length]; // 初始化结果数组
        for (int i = 0; i < result.length; i += 2) { // 循环遍历每个数据点
            result[i] = px2sx(data[i]); // 将频率数据转换为 x 坐标
            result[i + 1] = py2sy(data[i + 1]); // 将增益数据转换为 y 坐标
            max = Math.max(max, data[i + 1]); // 更新最大值
            min = Math.min(min, data[i + 1]); // 更新最小值
        }
        return result; // 返回转换后的坐标数组
    }

    public void setPadding(float padding) {
        this.padding = padding; // 设置内边距
    }
}
