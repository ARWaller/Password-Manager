//Class containing Algorithms needed for encryption and hashing
package com.mycompany.passwordmanager;

// 1 Encrypting plaintext password with AES

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static SecretKey secretKey;

    /**
     * Initializes the secret key for encryption.
     * The key is generated using the AES algorithm.
     */
    public static void initKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128); // AES key size in number of bits (can be 128, 192, or 256)
        secretKey = keyGenerator.generateKey();
    }

    /**
     * Encrypts a plaintext string using AES encryption.
     *
     * @param plainText The plaintext string to encrypt.
     * @return The encrypted string in Base64 encoding.
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }
}
