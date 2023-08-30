package com.hanamilink.ble;

public class CMD {

    public static int getHeader() {
        return 0xAB;
    }

//    public static int getTailer() {
//        return 0xef;
//    }

    public static int getShortHigh(int value) {
        if (value <= 0xFF) {
            return 0x00;
        }
        return ((value & 0xFF00) >> 8);
    }

    public static int getShortLower(int value) {
        return (value & 0x00FF);
    }

    public static int getIntByHighAndLower(byte high,byte lower) {
        return (((high & 0xff) << 8) + (lower & 0xff)) & 0xFFFF;
    }

    public static int getIntByHighToLower(byte b0,byte b1,byte b2,byte b3) {
        return (((b0 & 0xff) << 24) +((b1 & 0xff) << 16) +((b2 & 0xff) << 8) + (b3 & 0xff)) & 0xFFFFFFFF;
    }

    /**
     * 校验码
     *
     * @param src
     * @return
     */
    public static int getCheckSum(byte[] src) {
        int checkSum = 0;
        for (int i = 0; i < src.length - 1; i++) {
            checkSum += src[i] & 0xFF;
        }
        return checkSum;
    }

    /**
     * 校验码
     *
     * @param src
     * @return
     */
    public static int getCheckSum(byte[] src, int startIndex, int endIndex) {
        int checkSum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            checkSum += src[i] & 0xFF;
        }
        return checkSum;
    }

    /**
     * 校验码
     *
     * @param src
     * @return
     */
    public static boolean getCheckSumAndLenValid(byte[] src,int minLen, int startIndex, int endIndex,int checkSum) {
        if(src.length < minLen)
        {
            return false;
        }
//        if(getCheckSum(src,startIndex,endIndex) != checkSum)
//        {
//            return false;
//        }
        return true;
    }


    public static byte[] getQueryEQMusicValue() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x00;//音乐
        data[i++] = (byte) 0x00;//音乐
        data[i++] = (byte) getCheckSum(data);
        return data;

    }


    public static byte[] getQueryEQMicAndMusicValue() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x02;//
        data[i++] = (byte) getCheckSum(data);
        return data;

    }

    public static byte[] getCmd4GetAllEQVolume() {//原厂不支持，中芯龙支持

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;
        data[i++] = (byte) 0xFF;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    /**
     * type:
     * 音乐音量[0]  值[16:0]  NONE  NONE
     * 音乐低音[1]  [24:0]  NONE  NONE
     * 音乐高音[2]  值[24:0]  NONE  NONE
     * EQ 音效[3]    值[5:0]  NONE    NONE
     */
    public static byte[] setSoundEffect(int type, int value) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x00;//音乐
        data[i++] = (byte) (type & 0xFF);//
        data[i++] = (byte) (value & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;

    }

    public static byte[] setSoundEffectEQ8(int[] values) {

        byte[] data = null;
        if(values != null && values.length==8)
        {
            int length = 14;
            int lengthOfCMD = length - 3;
            int i = 0;
            data = new byte[length];
            data[i++] = (byte) getHeader();
            data[i++] = (byte) lengthOfCMD;
            data[i++] = (byte) 0x07;//音效控制命令
            data[i++] = (byte) 0x00;//音乐
            data[i++] = (byte) 0x04;//EQ[4]

            for(int n = 0; n < values.length;n++)
            {
                data[i++] = getSoundEffectEQ4Value(values[n] & 0xFF);
            }

            data[i++] = (byte) getCheckSum(data);

        }

        return data;

    }

    /**
     * MIC 音效
     * @param values
     * @return
     */
    public static byte[] setMICEffectEQ(int[] values) {

        byte[] data = null;
        if(values != null && values.length==4)
        {
            int length = 10;
            int lengthOfCMD = length - 3;
            int i = 0;
            data = new byte[length];
            data[i++] = (byte) getHeader();
            data[i++] = (byte) lengthOfCMD;
            data[i++] = (byte) 0x07;//音效控制命令
            data[i++] = (byte) 0x01;//MIC 音效
            data[i++] = (byte) 0x05;//MIC 音效

            for(int n = 0; n < values.length;n++)
            {
                data[i++] = getSoundEffectEQ4Value(values[n] & 0xFF);
            }

            data[i++] = (byte) getCheckSum(data);

        }

        return data;

    }

    private static byte getSoundEffectEQ4Value(int value)
    {
        if(value>=0)
        {
            return (byte)(value & 0xFF);
        }else
        {

            byte b = (byte)(Math.abs(value));

            b = (byte)(~b + 1);

            return b;

        }
    }

    public static byte[] setMicEffect(int type, int value) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制
        data[i++] = (byte) 0x01;//麦克风
        data[i++] = (byte) (type & 0xFF);//
        data[i++] = (byte) (value & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;

    }


    public static byte[] setMicFirst(boolean isOpen) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制
        data[i++] = (byte) 0x01;//麦克风优先
        data[i++] = (byte) 0x01;//
        data[i++] = (byte) ((isOpen?1:0) & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;

    }

    /**
     * 设置魔音开关
     */
    public static byte[] setMICMagicDSPPower(boolean isOn) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x00;//魔音
        data[i++] = (byte) 0x00;//魔音开关
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置魔音类型
     */
    public static byte[] setMICMagicDSPType(int magicType) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x00;//魔音
        data[i++] = (byte) 0x01;//魔音类型
        data[i++] = (byte) (magicType & 0xFF);
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置魔音类型
     */
    public static byte[] setMICMagicDSPValue(int value) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x00;//魔音
        data[i++] = (byte) 0x02;//魔音参数0-14
        data[i++] = (byte) (value & 0xFF);//0-14
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置混响
     */
    public static byte[] setMICDSPEchoPower(boolean isOn) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x01;//混响
        data[i++] = (byte) 0x00;//开关
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置混响衰减
     */
    public static byte[] setMICDSPEchoValue(int value) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x01;//混响
        data[i++] = (byte) 0x01;//混响衰减
        data[i++] = (byte) (value & 0xFF);//0-14
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置混响间隔
     */
    public static byte[] setMICDSPEchoIntervalValue(int value) {

        byte[] data = null;

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x01;//混响
        data[i++] = (byte) 0x02;//混响间隔
        data[i++] = (byte) (value & 0xFF);//0-14
        data[i++] = (byte) getCheckSum(data);

        return data;

    }

    /**
     * 设置防啸叫开关
     */
    public static byte[] setMICDSPAntiHowlingPower(boolean isOn) {

        byte[] data = null;

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x07;//音效控制命令
        data[i++] = (byte) 0x01;//音乐
        data[i++] = (byte) 0x04;//音效DSP
        data[i++] = (byte) 0x02;//防啸叫开关
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) getCheckSum(data);

        return data;

    }




    /**
     * Query System Mode Type
     * @return
     */
    public static byte[] getQuerySystemModeType() {

        int length = 4;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x05;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    /**
     * Query System Status
     * @return
     */
    public static byte[] getQuerySystemStatusType() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//
        data[i++] = (byte) 0x01;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    /**
     * Enter BT Music Mode
     * @return
     */
    public static byte[] getBTMusicSystemModeSet() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x06;//
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) 0x00;//BT Music Mode
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * Enter U Disk Mode
     * @return
     */
    public static byte[] getUSystemModeSet() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x06;//
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) 0x03;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * Enter TF Card Mode
     * @return
     */
    public static byte[] getTFCardSystemModeSet() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x06;//
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) 0x04;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * Enter Aux Mode
     * @return
     */
    public static byte[] getAUXSystemModeSet() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x06;//
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) 0x01;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * Enter FM Mode
     * @return
     */
    public static byte[] getFMSystemModeSet() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x06;//
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) 0x02;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getFMCurrentFrequencyInfo() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x05;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    public static byte[] getFMFrequencySet(int value) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x02;//
        data[i++] = (byte) getShortLower(value);//value 低位
        data[i++] = (byte) getShortHigh(value);//value 高位
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getSetFMRadioIndex(int index) {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x01;//
        data[i++] = (byte) index;//index
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    public static byte[] getFMScanOpen() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x03;//
        data[i++] = (byte) 0x01;//开关[1:0]
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getFMScanClose() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x03;//
        data[i++] = (byte) 0x00;//开关[1:0]
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getQueryFMRadios() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x08;//FM
        data[i++] = (byte) 0x04;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * get total of music in U Disk or sdcard.
     * @return
     */
    public static byte[] getMusicTotal() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//
        data[i++] = (byte) 0x02;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * get music list with index(start-->end) in U Disk or sdcard.
     * @return
     */
    public static byte[] getMusicListWithIndex(int start,int end) {

        int length = 9;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//
        data[i++] = (byte) 0x00;//
        data[i++] = (byte) getShortLower(start);//
        data[i++] = (byte) getShortHigh(start);//
        data[i++] = (byte) getShortLower(end);//
        data[i++] = (byte) getShortHigh(end);//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getCmd4GetPlayProgress() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x06;//播放
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getCmd4SetPlayProgress(int seconds) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x06;//播放
        data[i++] = (byte) getShortLower(seconds);//当前播放时 间[6]
        data[i++] = (byte) getShortHigh(seconds);;//当前播放时 间[6]
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    public static byte[] getCmd4SetPlayStatus(boolean isPlay) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x03;//播放状态[3]
        data[i++] = (byte) (isPlay?0x02:0x01);//暂停 [1]，播放[2]
        data[i++] = (byte) 0x00;
        data[i++] = (byte) 0x00;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    public static byte[] getCmd4SetMainVolume(int volume) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x03;//播放状态[3]
        data[i++] = (byte) 0x00;//
        data[i++] = (byte) 0x01;
        data[i++] = (byte) volume;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    public static byte[] getCmd4SetPlayIndex(int index) {

        int length = 7;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x05;//播放序号[5]
        data[i++] = (byte) getShortLower(index);
        data[i++] = (byte) getShortHigh(index);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //模式:正常(0)，单曲循环 (1)，文件夹循环(2)，随机(3)
    public static byte[] getCmd4SetPlayMode(int index) {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x04;
        data[i++] = (byte) index;//循环模式[4]
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //模式:正常(0)，单曲循环 (1)，文件夹循环(2)，随机(3)
    public static byte[] getCmd4PlayMode() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x04;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    //音乐控制:快进
    public static byte[] getCmd4PlayForward() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x07;
        data[i++] = (byte) 0;//快进
        data[i++] = (byte) getCheckSum(data);
        return data;
    }
    //音乐控制:快退
    public static byte[] getCmd4PlayBackward() {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x09;//播放
        data[i++] = (byte) 0x07;
        data[i++] = (byte) 1;//快退
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    //外音:
    public static byte[] getCmd4AUXStatus() {

        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0B;//
        data[i++] = (byte) 0x00;//
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //录音:
    public static byte[] getCmd4Record(boolean isRecording) {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0A;//
        data[i++] = (byte) 0x00;
        data[i++] = (byte) (isRecording?0x01:0x00);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    //灯控:获取灯控状态:
    public static byte[] getCmd4LightStatus() {
        int length = 5;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x00;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //灯控:设置开关灯
    public static byte[] getCmd4LightPower(boolean isOn) {

        int length = 6;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x01;//
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    //灯控:设置RGB颜色(转盘)
    public static byte[] getCmd4LightRGB(int red, int green, int blue) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x02;//
        data[i++] = (byte) (red & 0xFF);
        data[i++] = (byte) (green & 0xFF);
        data[i++] = (byte) (blue & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //灯控:亮度,
    public static byte[] getCmd4LightBrightness(int brightValue) {

        int length = 6;
        int lengthOfCMD = length-3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x03;//
        data[i++] = (byte) (brightValue & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    //灯控:自定义颜色选择  红[0],橙[1],黄[2],绿[3],蓝[4],紫[5],
    public static byte[] getCmd4LightColorSelect(int colorIndex) {

        int length = 6;
        int lengthOfCMD = length-3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x04;//
        data[i++] = (byte) (colorIndex & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    //灯控:自定义场景选择  [11:0]
    public static byte[] getCmd4LightSceneSelect(int sceneIndex) {

        int length = 6;
        int lengthOfCMD = length-3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0D;
        data[i++] = (byte) 0x04;//
        data[i++] = (byte) (sceneIndex & 0xFF);
        data[i++] = (byte) getCheckSum(data);
        return data;
    }


    /**
     * 当前闹钟状态及设置初始化时间[0]
     * @return
     */
    public static byte[] getCmd4TimerCurrentStatusAndTimeInit(int year,int month,int day,int hour,int min ,int sec) {

        int length = 11;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x00;
        data[i++] = (byte) year;
        data[i++] = (byte) month;
        data[i++] = (byte) day;
        data[i++] = (byte) hour;
        data[i++] = (byte) min;
        data[i++] = (byte) sec;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * 灯定时[1]
     * @return
     */
    public static byte[] getCmd4SetTimerOfLight(boolean isOn,int hourForOn,int minForOn,int hourForOff,int minForOff) {

        int length = 10;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x01;
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) hourForOn;
        data[i++] = (byte) minForOn;
        data[i++] = (byte) hourForOff;
        data[i++] = (byte) minForOff;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * 音乐定时[2]
     * @return
     */
    public static byte[] getCmd4SetTimerOfMusic(boolean isOn,int hourForOn,int minForOn,int hourForOff,int minForOff) {

        int length = 10;
        int lengthOfCMD = 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x02;
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) hourForOn;
        data[i++] = (byte) minForOn;
        data[i++] = (byte) hourForOff;
        data[i++] = (byte) minForOff;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * 闹钟1[3]
     * @return
     */
    public static byte[] getCmd4SetTimerOfAlarm1(boolean isOn,int hour,int min) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x03;
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) hour;
        data[i++] = (byte) min;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * 闹钟2[4]
     * @return
     */
    public static byte[] getCmd4SetTimerOfAlarm2(boolean isOn,int hour,int min) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x04;
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) hour;
        data[i++] = (byte) min;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

    /**
     * 闹钟3[5]
     * @return
     */
    public static byte[] getCmd4SetTimerOfAlarm3(boolean isOn,int hour,int min) {

        int length = 8;
        int lengthOfCMD = length - 3;
        int i = 0;
        byte[] data = new byte[length];
        data[i++] = (byte) getHeader();
        data[i++] = (byte) lengthOfCMD;
        data[i++] = (byte) 0x0C;
        data[i++] = (byte) 0x05;
        data[i++] = (byte) (isOn?0x01:0x00);
        data[i++] = (byte) hour;
        data[i++] = (byte) min;
        data[i++] = (byte) getCheckSum(data);
        return data;
    }

}
