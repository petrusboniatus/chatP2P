package server.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Security {
    private static final int iterations = 64;
    private static final int keyLength = 512;

    private Security() {
    }

    public static boolean checkPassword(String password, String storedHash) {
        BASE64Decoder dc = new BASE64Decoder();

        try {
            String[] parts = storedHash.split("\\$");
            byte[] encrypted = dc.decodeBuffer(parts[0]);
            byte[] seed = dc.decodeBuffer(parts[1]);
            byte[] newHash = hashPassword(password.toCharArray(), seed);
            return slowEquals(newHash, encrypted);
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public static String encrypt(String password) {
        BASE64Encoder dc = new BASE64Encoder();
        byte[] salt = new byte[64];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(salt);
        byte[] hash = hashPassword(password.toCharArray(), salt);
        return dc.encode(hash) + "$" + dc.encode(salt);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;

        for (int i = 0; i < a.length && i < b.length; ++i) {
            diff |= a[i] ^ b[i];
        }

        return diff == 0;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            SecretKeyFactory e = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = e.generateSecret(spec);
            return key.getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException var5) {
            throw new RuntimeException(var5);
        }
    }
}
