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
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//all uppercase letters
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";//all lowercase
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";//all symbols

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

    //This method generates a random salt, works with hashPassword method
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * hashes a password with salt.
     *
     * @param password     The plaintext password to be hashed.
     * @param salt         The salt that will added on to the hash
     * @return 
     */
    private static String hashPassword(String password) {
        byte[] salt = generateSalt();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Concatenate the password and salt
            byte[] password_with_salt = concatenateArrays(password.getBytes(), salt);

            // Hash the concatenated array
            byte[] hash = digest.digest(password_with_salt);

            // Encode the hash in base64 for storage
            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //used to conactenate password array with salt array, works with hasPassword method
    private static byte[] concatenateArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static String generatePassword() {
        // Combine all possible characters
        String allChars = UPPERCASE_CHARS + LOWERCASE_CHARS + NUMBERS + SPECIAL_CHARS;

        // Use a secure rng
        SecureRandom random = new SecureRandom();

        // StringBuilder to store the newly generated password
        StringBuilder passwordBuilder = new StringBuilder();

        // Generate the password by randomly selecting characters
        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        //return generated hashed password
        return hashPassword(passwordBuilder.toString());
    }
}
