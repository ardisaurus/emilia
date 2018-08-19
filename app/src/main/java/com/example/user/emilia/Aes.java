package com.example.user.emilia;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Aes {

    private static final String[][] S_BOX = {
            {"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
            {"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"},
            {"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"},
            {"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"},
            {"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"},
            {"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"},
            {"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},
            {"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},
            {"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},
            {"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"},
            {"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},
            {"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},
            {"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},
            {"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},
            {"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},
            {"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"}};

    private static final String[][] R_CON = {
            {"8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A"},
            {"2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39"},
            {"72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A"},
            {"74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8"},
            {"AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF"},
            {"C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC"},
            {"83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B"},
            {"36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3"},
            {"7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94"},
            {"33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20"},
            {"40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35"},
            {"6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F"},
            {"25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04"},
            {"08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63"},
            {"C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD"},
            {"61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D"}};

    private static String[][] userKey = new String[4][4];
    private static String[][] userText = new String[4][4];

    public static String[][] allKeysMatrix = new String[4][44];

    static String[][] GMatrix =
            {
                    {"02", "03", "01", "01"},
                    {"01", "02", "03", "01"},
                    {"01", "01", "02", "03"},
                    {"03", "01", "01", "02"}
            };

    public static String toHex(String arg) {

        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    public static String unHex(String arg) {
        String str = "";
        for (int i = 0; i < arg.length(); i += 2) {
            String s = arg.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            str = str + (char) decimal;
        }
        return str;
    }

    public static String encryptaes(String a, String b) {
        String key = toHex(a);
        String plaintext = toHex(b);
        String cipher = "";
        int i = 0;
        int j = 0;
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 4; row++) {
                userKey[row][column] = key.substring(i, i + 2);
                i = i + 2;
            }
        }
        generateAllKeys();
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 4; row = row + 1) {
                userText[row][column] = plaintext.substring(j, j + 2);
                j = j + 2;
            }
        }
        String[][] keyHex = new String[4][4];
        int WCol = 0;
        int roundCounter = 0;
        while (WCol < 44) {
            for (int cols = 0; cols < 4; cols++, WCol++) {
                for (int row = 0; row < 4; row++) {
                    keyHex[row][cols] = allKeysMatrix[row][WCol];
                }
            }
            if (roundCounter != 10) {
                roundCounter++;
                userText = aesStateXor(userText, keyHex);
                userText = aesNibbleSub(userText);
                userText = aesShiftRow(userText);
                if (roundCounter != 10)
                    userText = aesMixColumn(userText);
            } else
                userText = aesStateXor(userText, keyHex);
        }
        for (int cols = 0; cols < 4; cols++) {
            for (int row = 0; row < 4; row++) {
                cipher = cipher + userText[row][cols];
            }
        }
        return cipher;
    }

    public static void generateAllKeys() {
        for (int row = 0; row < 4; row = row + 1) {
            System.arraycopy(userKey[row], 0, allKeysMatrix[row], 0, 4);
        }
        String[][] temporaryMatrix;
        for (int column = 4; column < 44; column++) {
            if (column % 4 != 0) {
                for (int row = 0; row < 4; row++) {
                    allKeysMatrix[row][column] = XOR(allKeysMatrix[row][column - 4],
                            allKeysMatrix[row][column - 1]);
                }
            } else {
                temporaryMatrix = new String[1][4];
                temporaryMatrix[0][0] = allKeysMatrix[1][column - 1];
                temporaryMatrix[0][1] = allKeysMatrix[2][column - 1];
                temporaryMatrix[0][2] = allKeysMatrix[3][column - 1];
                temporaryMatrix[0][3] = allKeysMatrix[0][column - 1];
                for (int i = 0; i < 1; i++) {
                    for (int j = 0; j < 4; j++) {
                        temporaryMatrix[i][j] = aesSbox(temporaryMatrix[i][j]);
                    }
                }
                int r = column / 4;
                temporaryMatrix[0][0] = XOR(aesRcon(r), temporaryMatrix[0][0]);
                for (int row = 0; row < 4; row++) {
                    allKeysMatrix[row][column] = XOR(allKeysMatrix[row][column - 4],
                            temporaryMatrix[0][row]);
                }
            }
        }
    }

    public static String[][] aesStateXor(String[][] sHex, String[][] keyHex) {
        String XORMatrix[][] = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                XORMatrix[i][j] = XOR(sHex[i][j], keyHex[i][j]);
            }
        }
        return XORMatrix;
    }

    public static String[][] aesNibbleSub(String[][] pText) {
        String nibbleSubValues[][] = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nibbleSubValues[i][j] = aesSbox(pText[i][j]);
            }
        }
        return nibbleSubValues;
    }

    public static String[][] aesShiftRow(String[][] inHex) {
        String[][] outHex = new String[4][4];
        int count = 4;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i > 0) {
                    outHex[i][(j + count) % 4] = inHex[i][j];
                } else {
                    outHex[i][j] = inHex[i][j];
                }
            }
            count--;
        }
        return outHex;
    }

    protected static String mul2(String inHex) {
        inHex = Integer.toBinaryString(Integer.parseInt(inHex, 16));
        int inHexLenght = 8 - (inHex.length());
        String padding = new String();
        for (int i = 0; i < inHexLenght; i++) {
            padding += "0";
        }
        String in = padding.concat(inHex);
        String hex = Integer.toHexString(27);
        String shiftedBinary = Integer.toBinaryString(Integer.parseInt(in, 2) << 1);

        if (shiftedBinary.length() > 8)
            shiftedBinary = shiftedBinary.substring(1);
        String afterShift = Integer.toHexString(Integer.parseInt(shiftedBinary, 2));

        if (in.substring(0, 1).equals("1")) {
            return XOR(afterShift, hex);
        } else
            return afterShift;
    }

    protected static String mul3(String inHex) {
        return XOR(mul2(inHex), inHex);
    }

    protected static String[][] aesMixColumn(String[][] inHex) {
        String sum;
        String output[][] = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sum = "0";
                for (int k = 0; k < 4; k++) {
                    switch (GMatrix[i][k]) {
                        case "01":
                            sum = XOR(sum, inHex[k][j]);
                            break;
                        case "02":
                            sum = XOR(sum, mul2(inHex[k][j]));
                            break;
                        case "03":
                            sum = XOR(sum, mul3(inHex[k][j]));
                            break;
                    }
                }

                output[i][j] = sum;
            }
        }
        return output;
    }

    private static String XOR(String val1, String val2) {
        int Value1 = Integer.parseInt(val1, 16);
        int Value2 = Integer.parseInt(val2, 16);
        int exclusiveOutput = Value1 ^ Value2;
        String hexResult = Integer.toHexString(exclusiveOutput);
        return hexResult.length() == 1 ? ("0" + hexResult) : hexResult;
    }

    public static String aesSbox(String inSBox) {
        int firstDigitInt = Integer.parseInt(inSBox.substring(0, 1), 16);
        int secondDigitInt = Integer.parseInt(inSBox.substring(1, 2), 16);
        String outSBox = S_BOX[firstDigitInt][secondDigitInt];
        return outSBox;
    }

    public static String aesRcon(int inR_CON) {

        String outR_CON = R_CON[0][inR_CON];
        return outR_CON;
    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {

        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            System.out.println(key.length);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            System.out.println(key.length);
            System.out.println(new String(key, "UTF-8"));
            key = myKey.getBytes("UTF-8");
            secretKey = new SecretKeySpec(key, "AES");

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String encrypt(String strToEncrypt, String key) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return (Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")), Base64.NO_WRAP));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String key) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return (new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.NO_WRAP))));

        } catch (Exception e) {

            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
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
