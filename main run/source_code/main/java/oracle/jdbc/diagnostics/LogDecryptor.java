/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class LogDecryptor {
    public static void main(String[] stringArray) throws Exception {
        String string = stringArray[0];
        String string2 = stringArray[1];
        String string3 = stringArray[2];
        System.out.println("Key File : " + string);
        System.out.println("Log File : " + string2);
        LogDecryptor logDecryptor = new LogDecryptor();
        logDecryptor.decrypt(string, string2, string3);
    }

    private void decrypt(String string, String string2, String string3) throws Exception {
        PrivateKey privateKey = this.getPrivateKey(string, "RSA");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, privateKey);
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(string2));
        byte[][] byArray = this.readSessionAndIV(dataInputStream);
        byte[] byArray2 = cipher.doFinal(Base64.getDecoder().decode(byArray[0]));
        byte[] byArray3 = cipher.doFinal(Base64.getDecoder().decode(byArray[1]));
        this.decryptLog(dataInputStream, byArray2, byArray3, string3);
    }

    private void decryptLog(DataInputStream dataInputStream, byte[] byArray, byte[] byArray2, String string) throws Exception {
        throw new Error("LogDecryptor not supported");
    }

    private byte[][] readSessionAndIV(DataInputStream dataInputStream) throws Exception {
        byte[] byArray = new byte[dataInputStream.readInt()];
        byte[] byArray2 = new byte[dataInputStream.readInt()];
        dataInputStream.read(byArray);
        dataInputStream.read(byArray2);
        return new byte[][]{byArray, byArray2};
    }

    private PrivateKey getPrivateKey(String string, String string2) throws Exception {
        File file = new File(string);
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        byte[] byArray = new byte[(int)file.length()];
        dataInputStream.readFully(byArray);
        dataInputStream.close();
        String string3 = new String(byArray);
        String string4 = string3.replace("-----BEGIN PRIVATE KEY-----\n", "");
        string4 = string4.replace("-----END PRIVATE KEY-----", "");
        byte[] byArray2 = Base64.getMimeDecoder().decode(string4);
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(byArray2);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pKCS8EncodedKeySpec);
    }
}

