
import java.io.*;
import java.awt.*;
import java.net.URI;
import java.util.Scanner;
import jdk.jfr.Description;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URISyntaxException;
import java.util.function.Predicate;
import java.util.concurrent.TimeUnit;

/**
 * @author Peter John Arao
 */
@Description("jdk version: 17")
public class UserLogin {
    public static final String osName = System.getProperty("os.name", "").toUpperCase();
    public static final File filesDirectory = new File(getOsDirectory());

    private static final File records = new File(filesDirectory + (osName.startsWith("WINDOWS") ? "\\records.txt" : osName.startsWith("LINUX") ? "/records.txt" : ""));
    public static void main(String[] args) throws IOException, InterruptedException {
        if (filesDirectory.toString().equals("UNKNOWN") && records.toString().equals("UNKNOWN")) System.out.println(Color.RED + "THE PROGRAM IS NOT COMPATIBLE WITH YOUR CURRENT OPERATING SYSTEM\n" + Color.RESET);
        else {
            createDirectoryOnStart();
            mainSelection(new Scanner(System.in));
        }
        System.out.println(Color.BLUE + "THANK YOU " + Color.YELLOW + "FOR USING " + Color.PURPLE + "MY PROGRAM");
    }

    /**
     * Method that asks the user if the user want's to log in or register or exit the program.
     * @param keyboardInput scanner needed for user input.
     * @throws IOException if one of the files or directory does not exist.
     */
    private static void mainSelection(Scanner keyboardInput) throws IOException, InterruptedException {
        while (true) {
            try {
                System.out.println(Color.PURPLE + """
                        \s
                        ██╗   ██╗███████╗███████╗██████╗     ██╗      ██████╗  ██████╗ ██╗███╗   ██╗
                        ██║   ██║██╔════╝██╔════╝██╔══██╗    ██║     ██╔═══██╗██╔════╝ ██║████╗  ██║
                        ██║   ██║███████╗█████╗  ██████╔╝    ██║     ██║   ██║██║  ███╗██║██╔██╗ ██║
                        ██║   ██║╚════██║██╔══╝  ██╔══██╗    ██║     ██║   ██║██║   ██║██║██║╚██╗██║
                        ╚██████╔╝███████║███████╗██║  ██║    ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║
                         ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝    ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝ \s
                        """);
                final String LETTER_A = Color.BLUE + " A " + Color.RESET;
                final String LETTER_B = Color.YELLOW + " B " + Color.RESET;
                final String LETTER_C = Color.RED + " C " + Color.RESET;

                System.out.println(Color.YELLOW + ":" + LETTER_A + Color.YELLOW + ":" + Color.BLUE + " Login");
                System.out.println(Color.YELLOW + ":" + LETTER_B + Color.YELLOW + ":" + Color.YELLOW + " Register");
                System.out.println(Color.YELLOW + ":" + LETTER_C + Color.YELLOW + ":" + Color.RED + " Exit");
                System.out.print(Color.PURPLE + ">>: ");
                String choice = keyboardInput.nextLine().toUpperCase().trim();
                if (choice.equals("A") || choice.equals("B")|| choice.equals("C")) {
                    if (choice.equals("A")) {
                        if (login(keyboardInput)) {
                            System.out.println(Color.BLUE + "SUCCESSFULLY LOGGED IN" + Color.RESET);
                            loading();
                            Desktop.getDesktop().browse(new URI("https://youtu.be/q-Y0bnx6Ndw"));
                        } else throw new InvalidUserNameOrPasswordException("INVALID USERNAME OR PASSWORD");
                    }
                    if (choice.equals("B")) registration(keyboardInput);
                    if (choice.equals("C")) break;
                }
                else {
                    if (choice.isEmpty()) throw new BlankResponseException("BLANK CHOICE IS NOT ALLOWED");
                    else if (isNumber(choice)) throw new NumberResponseException("NUMBER RESPONSE IS NOT ALLOWED");
                    else if (containsSpecialCharacter(choice)) throw new SpecialCharacterResponseException("SPECIAL CHARACTER RESPONSE NOT ALLOWED");
                    throw new InvalidLetterResponseException("PLEASE CHOOSE:       A or B or C       ONLY");
                }
            } catch (RuntimeException | UserNotFoundException runtimeException) {
                System.out.println(Color.RED + runtimeException.getMessage() + Color.RESET);
                loading();
            } catch (URISyntaxException | InterruptedException exception) {
                System.out.println(Color.RED + exception.getMessage() + Color.RESET);
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
    private static void registration(Scanner keyboardInput) throws IOException, InterruptedException {
        try {
            String username;
            String password;
            boolean invalidCredentials = false;
            System.out.println(Color.YELLOW + """
                    ██████╗ ███████╗ ██████╗ ██╗███████╗████████╗███████╗██████╗\s
                    ██╔══██╗██╔════╝██╔════╝ ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
                    ██████╔╝█████╗  ██║  ███╗██║███████╗   ██║   █████╗  ██████╔╝
                    ██╔══██╗██╔══╝  ██║   ██║██║╚════██║   ██║   ██╔══╝  ██╔══██╗
                    ██║  ██║███████╗╚██████╔╝██║███████║   ██║   ███████╗██║  ██║
                    ╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝\s
                    """);
            System.out.print(Color.GREEN + "Enter Username: ");
            username = keyboardInput.nextLine().trim();
            System.out.print("Enter Password: " + Color.RESET);
            password = keyboardInput.nextLine().trim();

            if (containsSpecialCharacter(username) || containsSpecialCharacter(password)) {
                throw new InvalidUserNameOrPasswordException("USERNAME OR PASSWORD SHOULD NOT CONTAIN ANY SPECIAL CHARACTER");
            }
            else if (username.isEmpty() || password.isEmpty()) throw new BlankResponseException("PLEASE INSERT YOUR LOGIN CREDENTIALS PROPERLY");
            else {
                File user = new File(filesDirectory + "\\" + (osName.startsWith("WINDOWS") ? "\\" + username + "\\" : osName.startsWith("LINUX") ? "/" +  username + "/" : ""));
                if (checkIfUserAlreadyExist.test(user)) throw new UserAlreadyExistsException("USER ALREADY EXISTS");
                else {
                    if (!invalidCredentials){
                        if (user.mkdir()) {
                            User newUser = new User(username, password);
                            writeToATextFile(username, new File(user.getPath() + "\\username.txt"));
                            writeToATextFile(password, new File(user.getPath() + "\\password.txt"));
                            writeToATextFile(newUser.toString(), UserLogin.records);
                            System.out.println(Color.BLUE + "USER ADDED SUCCESSFULLY" + Color.RESET);
                            loading();
                        }
                    }
                }
            }
        } catch (RuntimeException runtimeException) {
            System.out.println(Color.RED + runtimeException.getMessage() + Color.RESET);
            loading();
        }
    }

    /**
     * Method that asks for user input on their credentials.
     * @param keyboardInput scanner needed for user input.
     * @return {@code true} if the inputted credentials matches the real credentials.
     */
    private static boolean login(Scanner keyboardInput) throws UserNotFoundException {
        System.out.println(Color.BLUE + """
                \s
                ██╗      ██████╗  ██████╗ ██╗███╗   ██╗
                ██║     ██╔═══██╗██╔════╝ ██║████╗  ██║
                ██║     ██║   ██║██║  ███╗██║██╔██╗ ██║
                ██║     ██║   ██║██║   ██║██║██║╚██╗██║
                ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║
                ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝\s
                """);
        System.out.print(Color.GREEN + "Enter Username: ");
        String username = keyboardInput.nextLine().trim();
        System.out.print("Enter Password: " + Color.RESET);
        String password = keyboardInput.nextLine().trim();
        File usernameFile = new File(filesDirectory + (osName.startsWith("WINDOWS") ? "\\" + username + "\\username.txt" : osName.startsWith("LINUX") ? "/" +  username + "/username.txt" : ""));
        File passwordFile = new File(filesDirectory + (osName.startsWith("WINDOWS") ? "\\" + username + "\\password.txt" : osName.startsWith("LINUX") ? "/" +  username + "/password.txt" : ""));
        try {
            return checkCredentials(usernameFile, passwordFile, username, password);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(Color.RED + fileNotFoundException.getMessage() + Color.RESET);
        }
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
    public static boolean checkCredentials(File username, File password, String usernameAttempt, String passwordAttempt) throws FileNotFoundException {
        String extractedUsername = "";
        String extractedPassword = "";
        if (username.exists() && password.exists()) {
            Scanner userNameScanner = new Scanner(username);
            Scanner passwordScanner = new Scanner(password);
            extractedUsername = userNameScanner.next();
            extractedPassword = passwordScanner.next();
        }
        return (username.exists() && password.exists()) && (usernameAttempt.equals(extractedUsername) && passwordAttempt.equals(extractedPassword));
    }

    /**
     * Writes to a text file.
     * Creates a new line so that the text will not stay at the first line and be overwritten.
     * @param whatToWrite the {@code String} to be written on the file.
     * @param fileToWrite the file to be written.
     * @throws IOException if the file does not exist.
     */
    private static void writeToATextFile(String whatToWrite, File fileToWrite) throws IOException {
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
            if (createFilesDirectory && createRecords) writeToATextFile(":       RECORDS       :", records);
        }
    }

    /**
     * Predicate that checks if the newUser that is being added already exists or not by checking the users folders.
     * Returns {@code true} if the user already exists.
     */
    private static final Predicate<File> checkIfUserAlreadyExist = File::exists;

    /**
     * Method that returns {@code true} or {@code false} based on the {@code String} passed in.
     * @param stringInput the {@code String} that might contain any special character.
     * @return {@code true} if the {@code String} passed in contains any special character.
     */
    private static boolean containsSpecialCharacter(String stringInput) {
        Pattern pattern = Pattern.compile("[^a-z\\d^*&@]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(stringInput);
        return matcher.find();
    }

    /**
     * Method that returns {@code true} or {@code false} based on the {@code String} passed in.
     * @param numberString the {@code String} that might contain all numbers
     * @return {@code true} if the {@code String} can be parsed to an Integer.
     */
    private static boolean isNumber(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * A record makes the code clean. getters, setters and other methods
     * are automatically generated as well us hashcode, toString and equals method.
     * I made my own implementation of toString() method.
     */
    private record User(String username, String password) {
        @Override
        public String toString() {
            return "Username = " + username + "\n" +
                   "Password = " + password + "\n";
        }
    }

    /**
     * class used for exception handling if the user already exists.
     * This class extends the {@code RuntimeException}
     */
    private static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * class used for exception handling if the user was not found.
     * This class extends the {@code FileNotFoundException}
     */
    private static class UserNotFoundException extends FileNotFoundException {
        public UserNotFoundException() {
            super("USER DOES NOT EXIST");
        }
    }
    /**
     * class used for exception handling on invalid username or password logins.
     * This class extends the {@code RuntimeException}
     */
    private static class InvalidUserNameOrPasswordException extends RuntimeException {
        public InvalidUserNameOrPasswordException(String message) {
            super(message);
        }
    }

    /**
     * class used for exception handling on answers that are not a, b, or c.
     * This class extends the {@code RuntimeException}
     */
    private static class InvalidLetterResponseException extends RuntimeException{
        public InvalidLetterResponseException(String message) {
            super(message);
        }
    }

    /**
     * class used for exception handling on blank response.
     * This class extends the {@code RuntimeException}
     */
    private static class BlankResponseException extends RuntimeException {
        public BlankResponseException(String message) {
            super(message);
        }
    }

    /**
     * class used for exception handling on number response.
     * This class extends the {@code RuntimeException}
     */
    private static class NumberResponseException extends RuntimeException {
        public NumberResponseException(String message) {
            super(message);
        }
    }

    /**
     * class used for exception handling on special character answers.
     * This class extends the {@code RuntimeException}
     */
    private static class SpecialCharacterResponseException extends RuntimeException {
        public SpecialCharacterResponseException(String message) {
            super(message);
        }
    }

    /**
     * Method that prints a trailing dot after a print text.
     * @throws InterruptedException if the thread is interrupted.
     */
    private static void loading() throws InterruptedException {
        System.out.print("REDIRECTING");
        for (int i = 0; i < 3; i++) {
            TimeUnit.MILLISECONDS.sleep(600);
            System.out.print(".");
        }
        System.out.println();
    }

    /**
     * Method that returns a directory where the files should be stored.
     * The directory will be particular to the operating system directory on which this program was executed.
     * @return a directory denoted by a {@code String}. This directory returned will hold all the files for this program.
     */
    public static String getOsDirectory() {
        return osName.startsWith("WINDOWS") ? getUserDirectory() + "\\TaskPerf6Files\\" : osName.startsWith("LINUX") ? getUserDirectory() + "/TaskPerf6Files/" : "UNKNOWN";
    }

    /**
     * Method that gets the home directory of the current logged-in user.
     * @return a {@code String} that contains the home directory of the user.
     */
    private static String getUserDirectory() {
        return System.getProperty("user.home");
    }
    /**
     * class that contains the instance variables that can be used to color text stream.
     */
    private static class Color {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String BLUE = "\u001B[34m";
        public static final String YELLOW = "\u001B[33m";
        public static final String PURPLE = "\u001B[35m";
    }
}
