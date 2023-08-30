package com.hanamiLink.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static HashMap<Integer, String> serviceTypes = new HashMap();
    private static HashMap<Integer, String> charPermissions;
    private static HashMap<Integer, String> charProperties;
    private static HashMap<Integer, String> descPermissions;

    public Utils() {
    }

    public static String getServiceType(int type) {
        return (String)serviceTypes.get(type);
    }

    public static String getCharPermission(int permission) {
        return getHashMapValue(charPermissions, permission);
    }

    public static String getCharPropertie(int property) {
        return getHashMapValue(charProperties, property);
    }

    public static String getDescPermission(int property) {
        return getHashMapValue(descPermissions, property);
    }

    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
        String result = (String)hashMap.get(number);
        if (TextUtils.isEmpty(result)) {
            List<Integer> numbers = getElement(number);
            result = "";

            for(int i = 0; i < numbers.size(); ++i) {
                result = result + (String)hashMap.get(numbers.get(i)) + "|";
            }
        }

        return result;
    }

    private static List<Integer> getElement(int number) {
        List<Integer> result = new ArrayList();

        for(int i = 0; i < 32; ++i) {
            int b = 1 << i;
            if ((number & b) > 0) {
                result.add(b);
            }
        }

        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src != null && src.length > 0) {
            for(int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(" ").append(0).append(hv);
                } else {
                    stringBuilder.append(" ").append(hv);
                }
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    public static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for(int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte)(toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    private static int toByte(char c) {
        byte b = (byte)"0123456789ABCDEF".indexOf(c);
        return b;
    }

    static {
        serviceTypes.put(0, "PRIMARY");
        serviceTypes.put(1, "SECONDARY");
        charPermissions = new HashMap();
        charPermissions.put(0, "UNKNOW");
        charPermissions.put(1, "READ");
        charPermissions.put(2, "READ_ENCRYPTED");
        charPermissions.put(4, "READ_ENCRYPTED_MITM");
        charPermissions.put(16, "WRITE");
        charPermissions.put(32, "WRITE_ENCRYPTED");
        charPermissions.put(64, "WRITE_ENCRYPTED_MITM");
        charPermissions.put(128, "WRITE_SIGNED");
        charPermissions.put(256, "WRITE_SIGNED_MITM");
        charProperties = new HashMap();
        charProperties.put(1, "BROADCAST");
        charProperties.put(128, "EXTENDED_PROPS");
        charProperties.put(32, "INDICATE");
        charProperties.put(16, "NOTIFY");
        charProperties.put(2, "READ");
        charProperties.put(64, "SIGNED_WRITE");
        charProperties.put(8, "WRITE");
        charProperties.put(4, "WRITE_NO_RESPONSE");
        descPermissions = new HashMap();
        descPermissions.put(0, "UNKNOW");
        descPermissions.put(1, "READ");
        descPermissions.put(2, "READ_ENCRYPTED");
        descPermissions.put(4, "READ_ENCRYPTED_MITM");
        descPermissions.put(16, "WRITE");
        descPermissions.put(32, "WRITE_ENCRYPTED");
        descPermissions.put(64, "WRITE_ENCRYPTED_MITM");
        descPermissions.put(128, "WRITE_SIGNED");
        descPermissions.put(256, "WRITE_SIGNED_MITM");
    }
}
