package com.ricky.pm.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liqi on 16/9/27.
 */
public class EncryptUtil {

    /**
     * Encode str by SHA1
     * @param str
     * @return
     */
    public static String encryptSHA1(String str) {
        String sha1Str;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] b = md.digest();
            sha1Str =  byte2string(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            sha1Str =  null;
        }
        return sha1Str;
    }

    private static String byte2string(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) hs = hs + "0" + stmp;
            else hs = hs + stmp;
        }
        return hs;
    }

    public static void main(String[] args){
        String s = new String("111111");



    }
}
