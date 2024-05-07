package com.hanamilink.activity.ui.widget.eq;


import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@Keep
public class EQPlotCore {
    private FloatBuffer m_freq2plot;
    private FloatBuffer m_gain2plot;
    private long m_eqPlotCore;
    private long m_paraHolder;
    private SOSPara[] m_sosPara;
    private int[] m_centerFreqs;
    private float m_totalGain;
    private int m_nplot;

    public EQPlotCore(int nplot, int sections, int[] centerFreqs) {
        this.m_freq2plot = ByteBuffer.allocateDirect(nplot * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.m_gain2plot = ByteBuffer.allocateDirect(nplot * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        assert this.m_freq2plot.isDirect();

        this.m_centerFreqs = centerFreqs;

        assert sections == centerFreqs.length;

        this.m_sosPara = new SOSPara[sections];

        for(int i = 0; i < this.m_sosPara.length; ++i) {
            this.m_sosPara[i] = new SOSPara((float)this.m_centerFreqs[i]);
            this.m_sosPara[i].reset();
        }

        this.m_paraHolder = this.allocateParaHolder(this.m_sosPara);

        assert this.m_paraHolder != 0L;

        this.m_totalGain = 1.0F;
        this.m_nplot = nplot;
        this.m_eqPlotCore = this.allocateEQPlotCore(this.m_freq2plot, nplot, this.m_paraHolder, 3);

        assert this.m_eqPlotCore != 0L;

    }

    public void setTotalGain(float totalGain) {
        this.m_totalGain = totalGain;
    }

    public SOSPara[] getSOSPara() {
        return this.m_sosPara;
    }

    public float getEQPlotData(float[] array, int index) {
        assert array.length >= 4 * this.m_nplot - 4;

        this.getEQPlotData(this.m_gain2plot, this.m_eqPlotCore, this.m_paraHolder, index, this.m_totalGain);
        float maxGain = this.m_gain2plot.get(0);

        for(int i = 0; i < this.m_nplot; ++i) {
            maxGain = Math.max(maxGain, this.m_gain2plot.get(i));
            if (i == 0) {
                array[0] = this.m_freq2plot.get(i);
                array[1] = this.m_gain2plot.get(i);
            } else if (i == this.m_nplot - 1) {
                array[4 * this.m_nplot - 6] = this.m_freq2plot.get(i);
                array[4 * this.m_nplot - 5] = this.m_gain2plot.get(i);
            } else {
                array[4 * i - 2 + 0] = this.m_freq2plot.get(i);
                array[4 * i - 2 + 1] = this.m_gain2plot.get(i);
                array[4 * i - 2 + 2] = this.m_freq2plot.get(i);
                array[4 * i - 2 + 3] = this.m_gain2plot.get(i);
            }
        }

        return maxGain;
    }

    public void updatePara(int index, float freq, float gain) {
        this.m_sosPara[index].centerFrequency = freq;
        this.m_sosPara[index].gain = gain;
        this.updateParaFreqGain(this.m_paraHolder, index, freq, gain);
    }

    public void syncPara(int index) {
        this.nativeUpdateParaFreq(this.m_paraHolder, index, this.m_sosPara[index].centerFrequency);
        this.nativeUpdateParaGain(this.m_paraHolder, index, this.m_sosPara[index].gain);
        this.nativeUpdateParaQVal(this.m_paraHolder, index, this.m_sosPara[index].qValue);
        this.nativeUpdateParaType(this.m_paraHolder, index, this.m_sosPara[index].type);
    }

    public void updateParaFreq(int index, float freq) {
        this.m_sosPara[index].centerFrequency = freq;
        this.nativeUpdateParaFreq(this.m_paraHolder, index, freq);
    }

    public void updateParaGain(int index, float gain) {
        this.m_sosPara[index].gain = gain;
        this.nativeUpdateParaGain(this.m_paraHolder, index, gain);
    }

    public void updateParaQVal(int index, float qval) {
        this.m_sosPara[index].qValue = qval;
        this.nativeUpdateParaQVal(this.m_paraHolder, index, qval);
    }

    public void updateParaType(int index, int type) {
        this.m_sosPara[index].type = type;
        this.nativeUpdateParaType(this.m_paraHolder, index, type);
    }

    public SOSPara getPara(int index) {
        return this.m_sosPara[index];
    }

    public void releaseNativeObject() {
        this.releaseNativeObject(this.m_eqPlotCore, this.m_paraHolder);
    }

    private native long allocateParaHolder(SOSPara[] var1);

    private native long allocateEQPlotCore(FloatBuffer var1, int var2, long var3, int var5);

    private native void releaseNativeObject(long var1, long var3);

    private native void getEQPlotData(FloatBuffer var1, long var2, long var4, int var6, float var7);

    private native void updateParaFreqGain(long var1, int var3, float var4, float var5);

    private native void nativeUpdateParaFreq(long var1, int var3, float var4);

    private native void nativeUpdateParaGain(long var1, int var3, float var4);

    private native void nativeUpdateParaQVal(long var1, int var3, float var4);

    private native void nativeUpdateParaType(long var1, int var3, int var4);

    static {
        System.loadLibrary("jl_eq");
    }
}
