
import java.io.*;
import java.util.Scanner;

/**
 * @author Peter John Arao
 */
public class Main {

    private static final File records = new File("C:\\TaskPerf6Files\\records.txt");
    private static final File filesDirectory = new File("C:\\TaskPerf6Files");

    public static void main(String[] args) throws IOException {
        Scanner keyboardInput = new Scanner(System.in);
        createDirectoryOnStart();
        mainSelection(keyboardInput);
    }

    /**
     * Method that asks the user if the user want's to log in or register or exit the program.
     * @param keyboardInput scanner needed for user input.
     * @throws IOException if one of the files or directory does not exist.
     */
    private static void mainSelection(Scanner keyboardInput) throws IOException {

        while (true) {
            try {
                System.out.println("WELCOME TO SIMPLE USER LOGIN | REGISTRATION");
                System.out.println(": A : Login");
                System.out.println(": B : Register");
                System.out.println(": C : Exit");
                System.out.print(">>: ");
                char choice = keyboardInput.nextLine().toUpperCase().trim().charAt(0);
                if (choice == 'A') {
                    if (login(keyboardInput)) System.out.println("SUCCESSFULLY LOGGED IN");
                }
                if (choice == 'B') registration(keyboardInput);
                if (choice == 'C') {
                    System.out.println("THANK YOU FOR USING MY PROGRAM");
                    break;
                }
            } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                System.out.println("BLANK CHOICE IS NOT ALLOWED");
            }
        }
    }
    /**
     * Method that creates a new user, checks if the username and password
     * contains invalid or special characters. Also checks if the user already exists.
     * Creates a folder for the user, inside the folder is the user credentials.
     * User credentials are also added to the records.txt
     * @param keyboardInput scanner needed for user input.
     * @throws IOException if one of the files or directory does not exist.
     */
    private static void registration(Scanner keyboardInput) throws IOException {

        String username;
        String password;
        String invalidCharacters = "~!@#$%^&*()-_=+[]{}\\|;:'\",<.>/?";

        boolean[] isInvalidCredentials = new boolean[2];

        System.out.println("REGISTER");
        System.out.print("Enter UserName: ");
        username = keyboardInput.nextLine().trim();
        System.out.print("Enter Password: ");
        password = keyboardInput.nextLine().trim();
        for (int i = 0; i < username.length(); i++) {
            // Check whether the username contains invalid or special character or not
            if (username.contains(Character.toString(invalidCharacters.charAt(i)))) {
                isInvalidCredentials[0] = true;
                break;
            }
        }
        for (int i = 0; i < password.length(); i++) {
            // Check whether the password contains invalid or special character or not
            if (password.contains(Character.toString(invalidCharacters.charAt(i)))) {
                isInvalidCredentials[1] = true;
                break;
            }
        }
        if (isInvalidCredentials[0] || isInvalidCredentials[1]) {
            if (isInvalidCredentials[0]) System.out.println("ALPHANUMERICAL USERNAMES ONLY!");
            if (isInvalidCredentials[1]) System.out.println("ALPHANUMERICAL PASSWORDS ONLY!");
        }

        byte invalidCount = 0;
        for (boolean isInvalid : isInvalidCredentials) {
            if (isInvalid) invalidCount++;
        }

        File user = new File("C:\\TaskPerf6Files\\" + username + "\\");
        if (!checkIfUserAlreadyExist(user)) {
            if (( invalidCount != 1 && invalidCount != 2)) {
                if (user.mkdir()) {
                    User newUser = new User(username, password);
                    writeToATextFile(username, new File(user.getPath() + "\\username.txt"));
                    writeToATextFile(password, new File(user.getPath() + "\\password.txt"));
                    writeToATextFile(newUser.toString(), Main.records);
                    System.out.println("USER ADDED SUCCESSFULLY");
                }
            }
        }
        else {
            System.out.println("USER ALREADY EXISTS");
        }
    }

    /**
     * Method that asks for user input on their credentials.
     * @param keyboardInput scanner needed for user input.
     * @return {@code true} if the inputted credentials matches the real credentials.
     */
    private static boolean login(Scanner keyboardInput) {
        System.out.println("LOGIN");
        System.out.print("Enter Username: ");
        String username = keyboardInput.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = keyboardInput.nextLine().trim();
        try {
            File usernameFile = new File("C:\\TaskPerf6Files\\" + username + "\\username.txt");
            File passwordFile = new File("C:\\TaskPerf6Files\\" + username + "\\password.txt");
            return checkCredentials(usernameFile, passwordFile, username, password);
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Method that reads the username and password files of the user.
     * @param username the username file of the user that contains the username.
     * @param password the password file of the user that contains the password.
     * @param usernameAttempt the username inputted by the user and will be used for equality.
     * @param passwordAttempt the password inputted by the user and will be used for equality.
     * @return {@code true} if the username and password matches the username and password from the file.
     * @throws FileNotFoundException if the credential files does not exist.
     */
    private static boolean checkCredentials(File username, File password, String usernameAttempt, String passwordAttempt) throws FileNotFoundException {
        String extractedUsername = "";
        String extractedPassword = "";
        if (username.exists() && password.exists()) {
            Scanner userNameScanner = new Scanner(username);
            Scanner passwordScanner = new Scanner(password);
            extractedUsername = userNameScanner.next();
            extractedPassword = passwordScanner.next();
        }
        if (!usernameAttempt.equals(extractedUsername) || !passwordAttempt.equals(extractedPassword)) System.out.println("INVALID USERNAME OR PASSWORD!");
        return (usernameAttempt.equals(extractedUsername) && passwordAttempt.equals(extractedPassword));
    }

    /**
     * Writes to a text file.
     * @param whatToWrite the {@code String} to be written on the file.
     * @param fileToWrite the file to be written.
     * @throws IOException if the file does not exist.
     */
    public static void writeToATextFile(String whatToWrite, File fileToWrite) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWrite, true));
        bufferedWriter.write(whatToWrite);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    /**
     * Method that creates the directory needed to store all the files.
     * @throws IOException if one of the files or directory does not exist.
     */
    private static void createDirectoryOnStart() throws IOException {
        if (!filesDirectory.exists()) {
            boolean createFilesDirectory = filesDirectory.mkdir();
            boolean createRecords = records.createNewFile();
            if (createFilesDirectory && createRecords) writeToATextFile("RECORDS", records);
        }
    }

    /**
     * Method that checks if the newUser that is being added already exists or not.
     * @param newUser the file directory of the new user.
     * @return {@code true} if the user already exists.
     */
    private static boolean checkIfUserAlreadyExist(File newUser) {
        return newUser.exists();
    }

    /**
     * Blueprint for creating Users
     */
    private static class User {
        private final String username;
        private final String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Username = " + username + "\n" +
                   "Password = " + password + "\n";
        }
    }
}
