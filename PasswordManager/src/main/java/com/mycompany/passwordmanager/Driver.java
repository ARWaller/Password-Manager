//Driver for taking in username and password, encrypting password, and loading in database

public class PasswordManagerApp {
    public static void main(String[] args) {
        try {
            // Initialize the encryption key
            EncryptionUtil.initKey();

            // Encrypt a password
            String username = "user123";
            String plainPassword = "mypassword";
            String encryptedPassword = EncryptionUtil.encrypt(plainPassword);

            // Store the encrypted password in the database
            DatabaseManager.insertPassword(username, encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
