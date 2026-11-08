
package main;

import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

/** this is for local registration and account creation */
public final class UserAccounts {

    private static final Path USER_DIRECTORY = Paths.get(
            System.getProperty("user.home"), "CardGameUsers");

    // Older accounts remain readable from the original shared file.
    private static final Path LEGACY_USER_FILE =
            USER_DIRECTORY.resolve("users.txt");

    private static Path accountFile(String username) {
        return USER_DIRECTORY.resolve(
                username.toLowerCase(java.util.Locale.ROOT) + ".txt");
    }

    private static final int CARD_COUNT = 20;
    private static final String EMPTY_CARDS = "0".repeat(CARD_COUNT);
    private static final int STARTING_COINS = 100;
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static String currentUser = null;

    private UserAccounts() { }

    private static void initialize() throws IOException {
        Files.createDirectories(USER_DIRECTORY);
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
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (!username.matches("[A-Za-z0-9_-]+")
                || username.equalsIgnoreCase("users")
                || password.length == 0) {

            Arrays.fill(password, '\0');
            showMessage("Use letters, numbers, underscores or hyphens "
                    + "for the username, and enter a password.");
            return;
        }

        try {
            initialize();

            if (usernameExists(username)) {
                Arrays.fill(password, '\0');
                showMessage("That username is already taken. "
                        + "Please choose another.");
                return;
            }

            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);

            byte[] passwordHash = hashPassword(password, salt);

            String record = username + "\t"
                    + Base64.getEncoder().encodeToString(salt) + "\t"
                    + Base64.getEncoder().encodeToString(passwordHash)
                    + System.lineSeparator()
                    + STARTING_COINS + System.lineSeparator()
                    + EMPTY_CARDS + System.lineSeparator()
                    + EMPTY_CARDS + System.lineSeparator();

            Files.writeString(accountFile(username), record,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE);

            showMessage("Account created! You can now select Old Acc to log in.");

        } catch (Exception ex) {
            Arrays.fill(password, '\0');
            showMessage("Could not create account: " + ex.getMessage());
        }
    }

    /** Displays the login dialog. Returns true for valid credentials. */
    public static boolean login() {
        try {
            initialize();
        } catch (IOException ex) {
            showMessage("Could not access account storage: "
                    + ex.getMessage());
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
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

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
            Path personalFile = accountFile(username);
            List<String> records = new java.util.ArrayList<>();

            if (Files.exists(personalFile)) {
                records.addAll(Files.readAllLines(personalFile));
            } else if (Files.exists(LEGACY_USER_FILE)) {
                records.addAll(Files.readAllLines(LEGACY_USER_FILE));
            }

            for (String record : records) {
                String[] fields = record.split("\\t", -1);

                if (fields.length != 3
                        || !fields[0].equalsIgnoreCase(username)) {
                    continue;
                }

                byte[] salt = Base64.getDecoder().decode(fields[1]);
                byte[] savedHash = Base64.getDecoder().decode(fields[2]);
                byte[] enteredHash = hashPassword(password, salt);

                boolean authenticated =
                        MessageDigest.isEqual(savedHash, enteredHash);

                Arrays.fill(password, '\0');

                if (authenticated) {
                    showMessage("Login successful. Welcome, "
                            + fields[0] + "!");
                    currentUser = username;
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
    /** Gets the current user's username. */
    public static String getCurrentUser() {
        return currentUser;
    }

    /** Gets the player's current coins. */
    public static int getCoins(String username) throws IOException {
        return Integer.parseInt(readAccount(username).get(1));
    }

    /** Updates the player's coins. */
    public static void updateCoins(String username, int coins)
            throws IOException {

        if (coins < 0) {
            throw new IllegalArgumentException(
                    "Coins cannot be negative.");
        }

        updateLine(username, 1, Integer.toString(coins));
    }

    /** Gets all 20 card slots. */
    public static int[] getCards(String username) throws IOException {
        return parseArray(readAccount(username).get(2));
    }

    /** Gets all 20 duplicate slots. */
    public static int[] getDuplicates(String username) throws IOException {
        return parseArray(readAccount(username).get(3));
    }

    /** Updates one card at index 0 through 19. */
    public static void updateCard(String username, int index, int value)
            throws IOException {

        updateArrayItem(username, 2, index, value);
    }

    /** Updates one duplicate at index 0 through 19. */
    public static void updateDuplicate(String username, int index, int value)
            throws IOException {

        updateArrayItem(username, 3, index, value);
    }

    /** Updates the entire cards array. */
    public static void updateCards(String username, int[] cards)
            throws IOException {

        updateLine(username, 2, encodeArray(cards));
    }

    /** Updates the entire duplicates array. */
    public static void updateDuplicates(String username, int[] duplicates)
            throws IOException {

        updateLine(username, 3, encodeArray(duplicates));
    }

    /** Converts a saved string into an integer array. */
    private static int[] parseArray(String saved) throws IOException {
        if (!saved.matches("[0-9]{20}")) {
            throw new IOException("Invalid card data.");
        }

        int[] result = new int[CARD_COUNT];

        for (int i = 0; i < CARD_COUNT; i++) {
            result[i] = saved.charAt(i) - '0';
        }

        return result;
    }

    /** Converts an integer array into a string of 20 digits. */
    private static String encodeArray(int[] values) {
        if (values == null || values.length != CARD_COUNT) {
            throw new IllegalArgumentException(
                    "Exactly 20 values are required.");
        }

        StringBuilder result = new StringBuilder(CARD_COUNT);

        for (int value : values) {
            if (value < 0 || value > 9) {
                throw new IllegalArgumentException(
                        "Each card count must be from 0 to 9.");
            }

            result.append(value);
        }

        return result.toString();
    }

    /** Updates one position in the cards or duplicates array. */
    private static void updateArrayItem(
            String username, int line, int index, int value)
            throws IOException {

        if (index < 0 || index >= CARD_COUNT) {
            throw new IndexOutOfBoundsException(
                    "Card index must be from 0 to 19.");
        }

        if (value < 0 || value > 9) {
            throw new IllegalArgumentException(
                    "Card count must be from 0 to 9.");
        }

        int[] values = parseArray(readAccount(username).get(line));
        values[index] = value;

        updateLine(username, line, encodeArray(values));
    }

    /** Reads a player's personal text file. */
    private static List<String> readAccount(String username)
            throws IOException {

        if (username == null
                || !username.matches("[A-Za-z0-9_-]+")
                || username.equalsIgnoreCase("users")) {

            throw new IllegalArgumentException("Invalid username.");
        }

        Path file = accountFile(username);

        List<String> lines =
                Files.readAllLines(file, StandardCharsets.UTF_8);

        if (lines.isEmpty()
                || !lines.get(0).split("\\t", -1)[0]
                        .equalsIgnoreCase(username)) {

            throw new IOException(
                    "Account file does not match username.");
        }

        // Upgrade older personal account files.
        if (lines.size() == 1) {
            lines.add(Integer.toString(STARTING_COINS));
            lines.add(EMPTY_CARDS);
            lines.add(EMPTY_CARDS);

            writeAccount(file, lines);
        }

        if (lines.size() != 4) {
            throw new IOException(
                    "Account must contain four lines.");
        }

        return lines;
    }

    /** Updates one line in a player's text file. */
    private static void updateLine(
            String username, int line, String value)
            throws IOException {

        List<String> lines = readAccount(username);
        lines.set(line, value);

        writeAccount(accountFile(username), lines);
    }

    /** Saves the updated account data to the player's text file. */
    private static void writeAccount(
            Path file, List<String> lines)
            throws IOException {

        Path temporary = Files.createTempFile(
                file.getParent(), "cardgame-", ".tmp");

        try {
            Files.write(temporary, lines, StandardCharsets.UTF_8);

            try {
                Files.move(temporary, file,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);

            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                Files.move(temporary, file,
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /** Checks whether the username is already registered. */
    private static boolean usernameExists(String username)
            throws IOException {

        if (Files.exists(accountFile(username))) {
            return true;
        }

        if (Files.exists(LEGACY_USER_FILE)) {
            for (String record : Files.readAllLines(LEGACY_USER_FILE)) {
                String[] fields = record.split("\\t", -1);

                if (fields.length == 3
                        && fields[0].equalsIgnoreCase(username)) {
                    return true;
                }
            }
        }

        return false;
    }

    /** Creates a secure hash of the player's password. */
    private static byte[] hashPassword(
            char[] password, byte[] salt) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                password, salt, ITERATIONS, KEY_LENGTH);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } finally {
            spec.clearPassword();
            Arrays.fill(password, '\0');
        }
    }

    /** Displays a message to the player. */
    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(
                null, message, "Card Game",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
