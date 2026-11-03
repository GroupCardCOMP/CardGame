package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

/** this is for local registration and account creation */
public final class UserAccounts {

    private static final Path USER_FILE = Paths.get(
        /*saved in user.home*/
            System.getProperty("user.home"), "CardGameUsers", "users.txt");
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private UserAccounts() { }

    private static void initialize() throws IOException {
        Files.createDirectories(USER_FILE.getParent());
    }

    /** Displays the new-account dialog and saves the account if valid. */
    public static void register() {
        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Choose a username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Choose a password:"));
        panel.add(passwordField);

        int choice = JOptionPane.showConfirmDialog(
                null, panel, "Create New Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        if (username.isEmpty() || username.contains("\t")
                || username.contains("\n") || password.length == 0) {
            Arrays.fill(password, '\0');
            showMessage("Please enter a valid username and a password.");
            return;
        }

        try {
            initialize();
            if (usernameExists(username)) {
                Arrays.fill(password, '\0');
                showMessage("That username is already taken. Please choose another.");
                return;
            }

            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            byte[] passwordHash = hashPassword(password, salt);
            String record = username + "\t"
                    + Base64.getEncoder().encodeToString(salt) + "\t"
                    + Base64.getEncoder().encodeToString(passwordHash)
                    + System.lineSeparator();

            Files.writeString(USER_FILE, record,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            showMessage("Account created! You can now select Old Acc to log in.");
        } catch (Exception ex) {
            Arrays.fill(password, '\0');
            showMessage("Could not create account: " + ex.getMessage());
        }
    }

    /** Displays the login dialog. Returns true only for valid credentials. */
    public static boolean login() {
        try {
            initialize();
            if (!Files.exists(USER_FILE) || Files.size(USER_FILE) == 0) {
                showMessage("No accounts found. Select New Acc to create one first.");
                return false;
            }
        } catch (IOException ex) {
            showMessage("Could not access account storage: " + ex.getMessage());
            return false;
        }

        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int choice = JOptionPane.showConfirmDialog(
                null, panel, "Log In",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return false;
        }

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        if (username.isEmpty() || password.length == 0) {
            Arrays.fill(password, '\0');
            showMessage("Please enter both your username and password.");
            return false;
        }

        try {
            List<String> records = Files.readAllLines(USER_FILE);
            for (String record : records) {
                String[] fields = record.split("\\t", -1);
                if (fields.length != 3 || !fields[0].equalsIgnoreCase(username)) {
                    continue;
                }

                byte[] salt = Base64.getDecoder().decode(fields[1]);
                byte[] savedHash = Base64.getDecoder().decode(fields[2]);
                byte[] enteredHash = hashPassword(password, salt);
                boolean authenticated = MessageDigest.isEqual(savedHash, enteredHash);
                Arrays.fill(password, '\0');

                if (authenticated) {
                    showMessage("Login successful. Welcome, " + fields[0] + "!");
                    return true;
                }
                showMessage("Incorrect password.");
                return false;
            }
            Arrays.fill(password, '\0');
            showMessage("Username not found. Please create an account first.");
        } catch (Exception ex) {
            Arrays.fill(password, '\0');
            showMessage("Could not log in: " + ex.getMessage());
        }
        return false;
    }

    private static boolean usernameExists(String username) throws IOException {
        if (!Files.exists(USER_FILE)) {
            return false;
        }
        for (String record : Files.readAllLines(USER_FILE)) {
            String[] fields = record.split("\\t", -1);
            if (fields.length == 3 && fields[0].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
            Arrays.fill(password, '\0');
        }
    }

    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Card Game", JOptionPane.INFORMATION_MESSAGE);
    }
}