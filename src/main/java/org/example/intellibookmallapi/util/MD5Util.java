package org.example.intellibookmallapi.util;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 */
public class MD5Util {
    
    /**
     * MD5加密
     * @param s 待加密字符串
     * @param charset 字符编码
     * @return 加密后的字符串
     */
    public static String MD5Encode(String s, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes(charset));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
    
    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            ret.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[b & 0x0f]);
        }
        return ret.toString();
    }
}
