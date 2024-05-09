package com.hanamilink.ui.widget.eq;


import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class ValueUtil {
    // 字符数组，用于十六进制转换
    private static final char[] mChars = "0123456789ABCDEF".toCharArray();
    // 十六进制字符串
    private static final String mHexStr = "0123456789ABCDEF";
    // 包含中文数字字符的数组
    private static char[] chnNum = new char[]{'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    // 用于保存中文数字字符和对应的值的映射关系
    private static HashMap<Character, Integer> table = new HashMap();

    public ValueUtil() {
    }

    // 将dp值转换为像素值
    public static int dp2px(Context context, int dp) {
        if (context == null) {
            throw new RuntimeException("context is null");
        } else {
            return (int) TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
        }
    }

    // 将sp值转换为像素值
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

    // 格式化时间（毫秒转换为"hh:mm"形式）
    public static String formatTime(int time) {
        if (time <= 0) {
            return "00:00";
        } else {
            StringBuilder sb = new StringBuilder();
            time /= 1000;
            if (time / 3600 > 0) {
                sb.append(String.format(Locale.CHINA, "%02d", time / 3600));
                sb.append(":");
            }

            if (time / 60 > 0) {
                sb.append(String.format(Locale.CHINA, "%02d", time % 3600 / 60));
                sb.append(":");
            } else {
                sb.append("00:");
            }

            sb.append(String.format(Locale.CHINA, "%02d", time % 60));
            return sb.toString();
        }
    }

    // 检查输入的字符串是否为合法的中国手机号码
    public static boolean isMobileNum(String phoneNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(14[0-9]))\\d{8}$");
        return p.matcher(phoneNum).matches();
    }

    // 检查输入的字符串是否为合法的十六进制字符串
    public static boolean checkHexStr(String sHex) {
        String sTmp = sHex.trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = sTmp.length();
        if (iLen > 1 && iLen % 2 == 0) {
            for (int i = 0; i < iLen; ++i) {
                if (!"0123456789ABCDEF".contains(sTmp.substring(i, i + 1))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 将字符串转换为十六进制字符串
    public static String str2HexStr(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = null;
        try {
            bs = str.getBytes("GBK");
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }
        if (bs == null) {
            return "";
        } else {
            byte[] var3 = bs;
            int var4 = bs.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                byte b = var3[var5];
                sb.append(mChars[(b & 255) >> 4]);
                sb.append(mChars[b & 15]);
            }
            return sb.toString().trim();
        }
    }

    // 将十六进制字符串转换为字符串
    public static String hexStr2Str(String hexStr) {
        hexStr = hexStr.trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0;
        for (int i = 0; i < bytes.length; ++i) {
            iTmp = "0123456789ABCDEF".indexOf(hexs[2 * i]) << 4;
            iTmp |= "0123456789ABCDEF".indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 255);
        }
        String result = "";
        try {
            result = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }
        return result;
    }

    // 将字节数组转换为十六进制字符串
    public static String byte2HexStr(byte[] b, int iLen) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; ++n) {
            sb.append(mChars[(b[n] & 255) >> 4]);
            sb.append(mChars[b[n] & 15]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    // 将字节数组转换为十六进制字符串
    public static String byte2HexStr(byte[] b) {
        return b == null ? "" : byte2HexStr(b, b.length);
    }

    // 将int数组转换为十六进制字符串
    public static String int2HexStr(int[] b, int iLen) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; ++n) {
            sb.append(mChars[((byte) b[n] & 255) >> 4]);
            sb.append(mChars[(byte) b[n] & 15]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    // 将字节数组转换为字符串
    public static String bytes2String(byte[] b, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    // 将十六进制字符串转换为字节数组
    public static byte[] hexStr2Bytes(String src) {
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = src.length() / 2;
        byte[] ret = new byte[iLen];
        for (int i = 0; i < iLen; ++i) {
            int m = i * 2 + 1;
            int n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 255);
        }
        return ret;
    }

    // 将字符串转换为Unicode编码
    public static String strToUnicode(String strText) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < strText.length(); ++i) {
            char c = strText.charAt(i);
            String strHex = Integer.toHexString(c);
            if (c > 128) {
                str.append("\\u");
            } else {
                str.append("\\u00");
            }
            str.append(strHex);
        }
        return str.toString();
    }

    // 将Unicode编码转换为字符串
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; ++i) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            int iTmp = Integer.valueOf(s.substring(2, 4), 16) << 8 | Integer.valueOf(s.substring(4), 16);
            str.append(new String(Character.toChars(iTmp)));
        }
        return str.toString();
    }

    // 将int值转换为十六进制格式的字符串
    public static String intToHexString(int num) {
        return String.format("%02x", num);
    }

    // 将int值转换为byte
    public static byte intToByte(int num) {
        return (byte) num;
    }

    // 将byte转换为无符号整数
    public static int byteToInt(byte b) {
        return b & 255;
    }

    // 将byte转换为十六进制格式的字符串
    public static String byteToHexString(byte b) {
        return intToHexString(b & 255);
    }

    // 将int值转换为字节数组
    public static byte[] intToBytes(int n) {
        byte[] b = new byte[]{(byte) (n & 255), (byte) (n >> 8 & 255), (byte) (n >> 16 & 255), (byte) (n >> 24 & 255)};
        return b;
    }

    // 将两个字节转换为整数
    public static int bytesToInt(byte h, byte l) {
        int result = (255 & h) << 8;
        result += 255 & l;
        return result;
    }

    // 将short值转换为字节数组
    public static byte[] shortToBytes(short n) {
        byte[] b = new byte[]{(byte) (n & 255), (byte) (n >> 8 & 255)};
        return b;
    }

    // 将中文数字字符串转换为对应的数字
    public static int cnStrToNumber(String str) {
        str = str.replace("零", "");
        if ((Integer) table.get(str.charAt(0)) >= 10) {
            str = "一" + str;
        }
        int value = 0;
        int sectionNum = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            int v = (Integer) table.get(c);
            if (v != 10 && v != 100 && v != 1000) {
                if (i == chars.length - 1) {
                    value += v;
                } else {
                    sectionNum = v;
                }
            } else {
                value += sectionNum * v;
            }
        }
        return value;
    }

    // 将int值转换为长度为2的byte数组
    public static byte[] int2byte2(int res) {
        byte[] targets = new byte[]{(byte) (res >> 8 & 255), (byte) (res & 255)};
        return targets;
    }

    // 将字符串转换为long型数字
    public static long stringNumToNum(String num) {
        long result = 0L;
        if (!TextUtils.isEmpty(num) && TextUtils.isDigitsOnly(num)) {
            result = Long.valueOf(num);
        }
        return result;
    }

    // 将字节数组转换为整型数组
    public static int[] bytes2ints(byte[] bytes, int len) {
        int[] result = new int[len];
        for (int i = 0; i < len; ++i) {
            result[i] = bytes[i];
        }
        return result;
    }

    // 将整型数组转换为字节数组
    public static byte[] ints2bytes(int[] ints, int len) {
        byte[] result = new byte[len];
        for (int i = 0; i < len; ++i) {
            result[i] = (byte) ints[i];
        }
        return result;
    }

    // 将浮点型数组转换为整型数组
    public static int[] floats2ints(float[] floats) {
        int[] result = new int[floats.length];
        for (int i = 0; i < floats.length; ++i) {
            result[i] = (int) floats[i];
        }
        return result;
    }

    static {
        // 初始化table，设置中文数字字符和对应的值的映射关系
        for (int i = 0; i < chnNum.length; ++i) {
            table.put(chnNum[i], i);
        }
        // 设置'十'、'百'、'千'对应的值
        table.put('十', 10);
        table.put('百', 100);
        table.put('千', 1000);
    }
}
