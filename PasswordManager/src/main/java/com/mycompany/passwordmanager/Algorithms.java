//Class containing Algorithms needed for encryption and hashing
package com.mycompany.passwordmanager;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Algorithms {
    
    private static final String ALGORITHM = "AES";
    private static SecretKey secretKey;
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//all uppercase letters
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";//all lowercase
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";//all symbols
    private static final byte[] salt = generateSalt();

    //generates a random salt
     static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

     static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Concatenate the password and salt
            byte[] password_with_salt = concatenateArrays(password.getBytes(), salt.getBytes());

            // Hash the concatenated array
            byte[] hash = digest.digest(password_with_salt);

            // Encode the hash in base64 for storage
            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //used to conactenate password with salt
     static byte[] concatenateArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
     
    public static String generatePassword(String salt) {
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
        return hashPassword(passwordBuilder.toString(),salt);
    }
   
    /**
     * Initializes the secret key for encryption.The key is generated using the AES algorithm.
     * @throws java.lang.Exception
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
     * @throws java.lang.Exception
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }
    
    //This method retrieves a secret key stored in keystore.jceks file
    //The key has already been generated and stored in the file
    //it offers a secure way to store the key by authorizing users who want to access the file
    //To access the file you must first create an evironment variable called "keystore_password"
    //The value of this variable must be the correct password to be authorized
    //For best security practices make sure to delete this environment variable after use
    public static SecretKey getKey(){
    SecretKey secretKey = null;

    try {
        // Load the keystore
        String keystorePath = "src/main/resources/com/mycompany/passwordmanager/keystore.jceks";
        String keystorePassword = System.getenv("keystore_password");
        
        KeyStore keystore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keystore.load(fis, keystorePassword.toCharArray());
        }

        // You can now use the keystore for cryptographic operations
        String alias = "mykey";
        String keyPassword = System.getenv("keystore_password");
        java.security.Key key = keystore.getKey(alias, keyPassword.toCharArray());

        // Check if the retrieved key is a SecretKey
        if (key instanceof SecretKey) {
            secretKey = (SecretKey) key;
        } else {
            throw new UnsupportedOperationException("The retrieved key is not a SecretKey.");
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return secretKey;
}

}


