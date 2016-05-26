package cn.mark.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by yaoping on 2016/5/18.
 */
public class DeviceUtil {
    private static final String MD5 = "MD5";
    private static final String UTF8 = "UTF-8";

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getMD5Value(String value) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance(MD5).digest(value.getBytes(UTF8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
