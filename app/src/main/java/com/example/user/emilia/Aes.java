package com.example.user.emilia;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Aes {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt_3des(String clearText, String secretKey) {
        try {
            byte[] bytePass = secretKey.getBytes("utf-8");
            byte[] byteKey = Arrays.copyOf(bytePass, 24);
            // System.out.println(DatatypeConverter.printHexBinary(byteKey));

            SecretKey key = new SecretKeySpec(byteKey, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] byteText = clearText.getBytes("utf-8");
            byte[] buf = cipher.doFinal(byteText);

            byte[] byteBase64 = Base64.getEncoder().encode(buf);
            String data = new String(byteBase64);

            return data;
        }
        catch(Exception ex) {
            return ex.getMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt_aes128(String clearText, String secretKey, String initVector) {
        try {
            byte[] bytePass = secretKey.getBytes("utf-8");
            byte[] byteV = initVector.getBytes("utf-8");

            byte[] byteKey = Arrays.copyOf(bytePass, 16);
            byte[] byteIV = Arrays.copyOf(byteV, 16);
            // System.out.println(DatatypeConverter.printHexBinary(byteKey));
            // System.out.println(DatatypeConverter.printHexBinary(byteIV));

            SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(byteIV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            byte[] byteText = clearText.getBytes("utf-8");
            byte[] buf = cipher.doFinal(byteText);

            byte[] byteBase64 = Base64.getEncoder().encode(buf);
            String data = new String(byteBase64);

            return data;
        }
        catch(Exception ex) {
            return ex.getMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt_aes256(String clearText, String secretKey, String initVector) {
        try {
            byte[] bytePass = secretKey.getBytes("utf-8");
            byte[] byteV = initVector.getBytes("utf-8");

            byte[] byteKey = Arrays.copyOf(bytePass, 32);
            byte[] byteIV = Arrays.copyOf(byteV, 16);
            // System.out.println(DatatypeConverter.printHexBinary(byteKey));
            // System.out.println(DatatypeConverter.printHexBinary(byteIV));

            SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(byteIV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            byte[] byteText = clearText.getBytes("utf-8");
            byte[] buf = cipher.doFinal(byteText);

            byte[] byteBase64 = Base64.getEncoder().encode(buf);
            String data = new String(byteBase64);

            return data;
        }
        catch(Exception ex) {
            return ex.getMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String clearText, String secretKey, String encryption) {
        String initVector = "InitVector";
        String cipher = "";
        if(encryption.equals("3des")){
            cipher = encrypt_3des(clearText, secretKey);
        }else if(encryption.equals("aes2")){
            cipher = encrypt_aes256(clearText, secretKey, initVector);
        }else{
            cipher = encrypt_aes128(clearText, secretKey, initVector);
        }
        return cipher;
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
